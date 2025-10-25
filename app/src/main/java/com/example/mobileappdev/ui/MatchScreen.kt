package com.example.mobileappdev.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mobileappdev.data.MatchProfile
import com.example.mobileappdev.ui.match.MatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchScreen(
    onBackClick: () -> Unit,
    role: String
) {
    val vm = remember(role) { MatchViewModel(role) }
    val ui by vm.ui.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (role == "student") "Match with Tutors" else "Connect with Students") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {
            FilterRow(
                role = role,
                selectedSubject = ui.filters.subject,
                onSubjectChange = vm::setSubject,
                maxPrice = ui.filters.maxPrice,
                onMaxPriceChange = vm::setMaxPrice
            )

            Spacer(Modifier.height(8.dp))

            when {
                ui.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                ui.profiles.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No candidates found.")
                }
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(ui.profiles, key = { it.uid }) { p ->
                        MatchCard(
                            profile = p,
                            liked = ui.liked.contains(p.uid),
                            matched = ui.matches.contains(p.uid),
                            onLikeToggle = { vm.toggleLike(p.uid) },
                            onOpen = { /* TODO: navigate to profile detail */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterRow(
    role: String,
    selectedSubject: String?,
    onSubjectChange: (String?) -> Unit,
    maxPrice: Double?,
    onMaxPriceChange: (Double?) -> Unit
) {
    val allSubjects = listOf("Math","Physics","Chemistry","Biology","English","History")
    var subjectExpanded by remember { mutableStateOf(false) }

    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.weight(1f)) {
            OutlinedButton(onClick = { subjectExpanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(selectedSubject ?: "Subject")
            }
            DropdownMenu(expanded = subjectExpanded, onDismissRequest = { subjectExpanded = false }) {
                DropdownMenuItem(text = { Text("Any") }, onClick = { onSubjectChange(null); subjectExpanded = false })
                allSubjects.forEach { s ->
                    DropdownMenuItem(text = { Text(s) }, onClick = { onSubjectChange(s); subjectExpanded = false })
                }
            }
        }

        Spacer(Modifier.width(8.dp))

        if (role == "student") {
            var slider by remember(maxPrice) { mutableStateOf((maxPrice ?: 30.0).toFloat()) }
            Column(Modifier.weight(1f)) {
                Text("Max €/h: ${slider.toInt()}", style = MaterialTheme.typography.labelMedium)
                Slider(
                    value = slider,
                    onValueChange = { slider = it },
                    onValueChangeFinished = { onMaxPriceChange(slider.toDouble()) },
                    valueRange = 10f..60f
                )
            }
        }
    }
}

@Composable
private fun MatchCard(
    profile: MatchProfile,
    liked: Boolean,
    matched: Boolean,
    onLikeToggle: () -> Unit,
    onOpen: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onOpen)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            val initials = profile.displayName.split(" ")
                .filter { it.isNotBlank() }.map { it.first() }.take(2).joinToString("")
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) { Text(initials, fontWeight = FontWeight.Bold) }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(profile.displayName, style = MaterialTheme.typography.titleMedium)
                val subtitle = buildString {
                    append(profile.level)
                    profile.city?.let { append(" • $it") }
                    if (profile.role == "tutor" && profile.hourlyPrice != null) append(" • €${profile.hourlyPrice}/h")
                }
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    profile.subjects.joinToString(),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (matched) {
                    Text("🎉 It's a match!", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                }
            }

            IconButton(onClick = onLikeToggle) {
                if (liked) Icon(Icons.Filled.Favorite, contentDescription = "Unlike")
                else Icon(Icons.Filled.FavoriteBorder, contentDescription = "Like")
            }
        }

        if (profile.about.isNotBlank()) {
            HorizontalDivider()
            Text(
                text = profile.about,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

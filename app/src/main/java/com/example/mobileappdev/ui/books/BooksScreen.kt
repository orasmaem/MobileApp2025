package com.example.mobileappdev.ui.books

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mobileappdev.data.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksScreen(onBack: () -> Unit, initialSubject: String = "mathematics") {
    val vm = remember { BooksViewModel() }
    var ui by remember { mutableStateOf<BooksState>(BooksState.Idle) }
    var subject by remember { mutableStateOf(initialSubject) }

    LaunchedEffect(Unit) { vm.load(subject) { ui = it } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Books: $subject") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { vm.load(subject) { ui = it } }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Reload")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = { vm.load(subject) { ui = it } }) { Text("Search") }
            }

            Spacer(Modifier.height(12.dp))

            when (val s = ui) {
                is BooksState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                is BooksState.Error -> Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(s.message, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { vm.load(subject) { ui = it } }) { Text("Try again") }
                }
                is BooksState.Success -> {
                    if (s.items.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No books found.")
                        }
                    } else {
                        BooksList(items = s.items)
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun BooksList(items: List<Book>) {
    LazyColumn(
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = { it.id }) { book ->
            ElevatedCard(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = book.coverUrl,
                        contentDescription = book.title,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(book.title, style = MaterialTheme.typography.titleMedium,
                            maxLines = 2, overflow = TextOverflow.Ellipsis)
                        Text(book.authors, style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1, overflow = TextOverflow.Ellipsis)
                        if (!book.year.isNullOrBlank()) {
                            Text(book.year, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}

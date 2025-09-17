# Greenhorn & Don

An app which connects tutors and students in a way where they can see eachothers' profiles (field of study, level of education, location, hourly price). Both the tutors and students can choose whom they would like to work with.

# Roles

* Project Leader / Manager – Elerin Lõhmus
* Researcher – Kevin Sammalkivi
* Editor – Mihkel Orasmäe
* Lead Developer / Builder – Jaakob-Jaan Avvo
* Presenter – Imre Saks

# Planned Features

1.Profile Creation & Browsing

  -Tutors: field of study, education level, location, hourly price, availability

  -Students: learning goals, preferred subjects, budget, location

2.Search & Filter

  -By subject, price range, location, tutor/student level

3.Matchmaking / “Like” System

  -Both sides must express interest before chatting

4.In-App Chat / Messaging

  -Secure chat with file sharing (PDFs, notes, assignments)

5.Booking & Scheduling

  -Built-in calendar to book lessons, send reminders

6.Payment Integration

  -Hourly rate payments, commission to platform

7.Rating & Reviews

  -Students rate tutors; tutors can also rate students

8.Push Notifications

  -Lesson reminders, new matches, payment confirmations

9.Profile Verification

  -Document uploads (e.g., ID, degree verification for tutors)


# Tools & Frameworks

IDE & Language: Android Studio + Kotlin

UI Layer: Jetpack Compose (recommended) or XML layouts

Navigation & State Management: Jetpack Navigation Component, ViewModel + LiveData/Flow

Data & Storage: Room (local DB), Firebase Firestore (cloud + real-time sync)

Authentication & Verification: Firebase Auth, optional KYC service (Onfido, etc.)

Chat & Realtime Features: Firebase Realtime Database / Firestore OR Socket.IO backend

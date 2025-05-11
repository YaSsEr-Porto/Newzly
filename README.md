# 📰 Newzly - Android News App

Newzly is a modern Android news application that displays breaking news headlines and full article previews with a sleek, expandable card interface inspired by Dribbble designs.
Built using the MVVM architecture, it supports network loading, real-time article viewing, and a rich user experience.

## ✨ Features

- MVVM Architecture with `ViewModel` and `LiveData`
- Expandable article cards using `InboxRecyclerView`
- Full article preview layout without WebView
- `ViewBinding` for all layouts (no `findViewById`)
- Asynchronous image loading with `Glide`
- Graceful error handling and loading states
- Back-press support with article collapse animation

## 🏗️ Tech Stack

- Language: Kotlin
- Architecture: MVVM
- UI: RecyclerView + InboxRecyclerView
- View Management: ViewBinding
- Networking: Retrofit + Gson
- Image Loading: Glide
- Coroutine-based async handling

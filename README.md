# BrainX Movie App (KMP)

A **Kotlin Multiplatform (KMP)** movie app built with **Compose Multiplatform**, sharing UI and business logic across **Android** and **iOS**. The app uses TMDB-style search and detail flows with a clear layered architecture.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Architecture Overview](#architecture-overview)
- [Module Details](#module-details)
- [How Key Flows Work](#how-key-flows-work)
- [Build & Run](#build--run)
- [Configuration](#configuration)
- [Adding a New Screen](#adding-a-new-screen)
- [Conventions & Rules](#conventions--rules)

---

## Tech Stack

| Area | Technology |
|------|------------|
| **UI** | Compose Multiplatform (Material 3), Jetpack Compose Navigation (type-safe routes) |
| **DI** | Koin (core, compose, viewModel) |
| **Networking** | Ktor Client (OkHttp on Android, Darwin on iOS), Kotlinx Serialization |
| **Async** | Kotlin Coroutines, Flow |
| **Images** | Coil 3 (multiplatform, Ktor engine) |
| **Local storage** | DataStore (preferences), Room (optional; present in shared) |
| **Min versions** | Android: API 24, JVM 21; iOS: via Compose Multiplatform |

Dependency versions are centralized in `gradle/libs.versions.toml`.

---

## Project Structure

```
BrainX Ticket Tribe/
├── composeApp/                    # Android & iOS app; shared Compose UI
│   ├── src/
│   │   ├── commonMain/             # Shared Kotlin + Compose (screens, nav, DI, theme)
│   │   ├── androidMain/            # MainActivity, MainApplication, AndroidManifest, theme XML
│   │   └── iosMain/                # MainViewController (ComposeUIViewController)
│   └── build.gradle.kts
│
├── shared/
│   ├── core/
│   │   ├── bootstrapDI/            # Koin bootstrap; aggregates all DI modules (no direct infra)
│   │   ├── domain/                 # Use cases, repository interfaces, DTOs, Resource/result types
│   │   └── datasource/            # Repository implementations, API config, Ktor/DataStore wiring
│   ├── ktorNetwork/                # Ktor client setup, interceptors, safe network calls
│   ├── roomDatabase/              # Room DB (entities, DAOs, platform-specific factory)
│   ├── localDatastore/            # DataStore preferences (expect/actual per platform)
│   └── utilsExtensions/            # Shared utilities (navigation, constants, Compose helpers)
│
├── iosApp/                         # Xcode project; embeds Compose framework, calls MainViewController
├── gradle/
│   └── libs.versions.toml          # Version catalog (deps + plugins)
├── settings.gradle.kts
└── build.gradle.kts
```

- **composeApp**: Single Compose app; `commonMain` holds all shared UI, navigation, and app-level DI. Platform code only for entry points and platform-specific APIs.
- **shared**: Pure Kotlin multiplatform libraries. No Compose in `domain`/`datasource`/`ktorNetwork`/`roomDatabase`/`localDatastore`; only `composeApp` and `utilsExtensions` use Compose where needed.

---

## Architecture Overview

- **UI layer** (Compose): Screens, ViewModels, navigation, theme. Lives in `composeApp`.
- **Domain layer**: Use cases and repository interfaces; no framework dependencies. Lives in `shared/core/domain`.
- **Data layer**: Repository implementations, API client, local DB/DataStore. Lives in `shared/core/datasource`, `shared/ktorNetwork`, `shared/roomDatabase`, `shared/localDatastore`.
- **DI**: Composed in `bootstrapDI` from `datasource` + `domain` + app-level modules (ViewModels, dispatchers). Compose app only talks to `bootstrapDI` and `domain`; it must not depend on `ktorNetwork`, `room_database`, or `local_datastore` directly (enforced by a Gradle task in `bootstrapDI`).

Data flow:

```
UI (Compose) → ViewModel → UseCase → Repository (interface in domain, impl in datasource)
                                    → Ktor / Room / DataStore
```

Navigation is type-safe: routes are serializable data (e.g. `AppRoutes.Detail(mediaDataModelJson)`), and the nav graph uses `toRoute<>()` and typed composable destinations.

---

## Module Details

### composeApp

- **commonMain**
  - **`com.brainx.ticket_tribe`**: App entry (`App.kt`), `MovieAppTheme`, root `Scaffold` and `AppNavHostGraph`.
  - **`di`**: `initKoin()` (combines `coroutineDispatchersModule`, `viewModelModule`, and modules from `bootstrapDI`), `ViewModelsModule`, `CoroutineDispatcherModule`.
  - **`presentation/navigation`**: `AppRoutes` (sealed + `@Serializable`), `AppNavHostGraph` (NavHost, composable destinations, ViewModel via `koinViewModel`).
  - **`presentation/screens/**`**: Per-screen packages with `ui/`, `viewmodel/`, `ui_state/`, `ui_events/`, `ui_intents/`.
  - **`presentation/theme`**: Material 3 theme (color, type, shape, dimens).
  - **`presentation/ui_components`**: Reusable components (buttons, text, text fields, list items like `MovieCard`, `MovieCarousal`, `MoviePoster`).
- **androidMain**: `MainActivity` (Compose `setContent` + `enableEdgeToEdge`), `MainApplication` (calls `initKoin { androidContext(this@MainApplication) }`), `AndroidManifest.xml`, `res/values/theme.xml` (status bar, etc.).
- **iosMain**: `MainViewController()` = `ComposeUIViewController` that calls `initKoin()` then `App()`.

### shared/core/bootstrapDI

- Aggregates DI: `coroutineDispatchersModule` + `viewModelModule` (from composeApp) + `datasourceModuleProvider` + `domainModule`.
- **Does not** depend on `ktor_network`, `room_database`, or `local_datastore`; it only depends on `datasource`, which in turn pulls in those. A custom Gradle task fails the build if bootstrapDI (or its sources) import those infra packages directly.

### shared/core/domain

- **Repository interfaces** (e.g. `MovieRepository`): search, get video, etc.; expose `Flow<NetworkResultState<...>>`.
- **Use cases** (e.g. `SearchMultiUseCase`, `GetVideoUseCase`): wrap repository calls and map to `Resource` (Loading/Success/Error) for the UI.
- **DTOs / models**: Network DTOs and mappers used by use cases and UI (e.g. `SearchMultiMovieDto`, `MediaDTO`).
- **Result types**: `NetworkResultState` (Success / Error / SuccessWithErrorData), `Resource` (Loading / Success / Error).
- **DI**: `domainModule` = `useCaseModule`; use cases get repositories from Koin.

### shared/core/datasource

- **Repository implementations** (e.g. `MovieRepositoryImp`): implement domain repository interfaces, call Ktor APIs and optionally Room/DataStore.
- **DI**: `datastoreModulesProvider` = list of (network token, datastore prefs, movie config, repository module) + Room module + Ktor module + DataStore platform module. This list is what `bootstrapDI`’s `datasourceModuleProvider` exposes.
- **Config**: API key and access token are provided via Koin named bindings (see `NetworkModules.kt` / `movieConfigModule`); keep secrets out of version control (e.g. BuildConfig or env).

### shared/ktorNetwork

- Ktor `HttpClient` setup: engine (OkHttp/Darwin), JSON, logging, auth. Platform-specific engine is in `expect`/`actual` (`KtorPlatformModule`).
- Safe network call wrapper and error handling used by repository layer.

### shared/roomDatabase

- Room database, entities, DAOs. Platform-specific `DatabaseFactory` for Android vs iOS.

### shared/localDatastore

- DataStore Preferences; platform-specific creation and optional DI in `DataStorePlatformModule`.

### shared/utilsExtensions

- Shared helpers: navigation extensions (e.g. `safeNavToNextScreen`, `toModel`), constants (`ExtConstants`), Compose utilities (e.g. ripple-less click, consume effects), coroutine dispatcher qualifiers used in DI.

---

## How Key Flows Work

### App startup

1. **Android**: `MainApplication.onCreate()` → `initKoin { androidContext(this@MainApplication) }` → `MainActivity` → `setContent { App() }`.
2. **iOS**: App launches → Swift/ObjC calls `MainViewController()` → inside ComposeUIViewController, `initKoin()` then `App()`.
3. **App()**: Wraps content in `MovieAppTheme`, creates `rememberNavController()`, and sets `AppNavHostGraph` as the only content of a `Scaffold`. Start destination is `AppRoutes.MainHome`.

### Navigation

- **Routes**: Defined in `AppRoutes` (sealed class, all `@Serializable`). Examples: `MainHome`, `Detail(mediaDataModelJson)`, `VideoPlayer(id, mediaType)`.
- **Nav graph**: In `AppNavHostGraph`, each destination is a `composable<RouteType> { ... }`. ViewModels are obtained with `koinViewModel<MainHomeScreenViewModel>()`. Navigation to detail uses `navController.safeNavToNextScreen(AppRoutes.Detail(...))`. Detail screen gets args via `backStackEntry.toRoute<AppRoutes.Detail>()` and uses a shared DTO (e.g. `MediaDTO`) for the payload.

### Screen pattern (e.g. Main Home)

- **UiState**: Single data class (e.g. `MainHomeScreenUiState`: searchText, searchResponse, isLoading). Marked `@Stable` where appropriate.
- **UiIntents**: Sealed interface for user actions (e.g. `OnSearchTextUpdate`, `OnSearchButtonIntent`, `OnMovieItemClick`, `OnTriggerPagination`). ViewModel’s `onIntent()` handles them.
- **UiEvents**: One-off events from ViewModel to UI (e.g. `Navigate.MoveToDetail(media)`). Exposed as `Channel<MainHomeScreenUiEvents>.receiveAsFlow()`; UI subscribes and navigates or shows feedback.
- **ViewModel**: Holds `StateFlow<UiState>`, runs use cases on a background dispatcher (injected via Koin), updates state and sends events. No Compose or Android imports in shared ViewModels.

### Data flow (search example)

1. User types and taps search → intents → `MainHomeScreenViewModel.onIntent(OnSearchButtonIntent)`.
2. ViewModel calls `searchMultiUseCase.invoke(searchText, page)` (with optional reset).
3. Use case calls `movieRepository.search(...)` (Ktor), maps `NetworkResultState` to `Resource`, exposes `Flow<Resource<SearchMultiMovieDto>>`.
4. ViewModel collects the flow (on IO dispatcher), updates `_state` (e.g. searchResponse, isLoading) and on success may send `MoveToDetail` event.
5. UI observes `viewModel.state` and `viewModel.eventFlow`, recomposes on state and navigates on events (e.g. via `ConsumeUIEffects` or equivalent).

---

## Build & Run

### Prerequisites

- **JDK 21**
- **Android Studio** (latest stable) or **IntelliJ** with Android plugin for Android builds
- **Xcode** (for iOS) and **CocoaPods** (if used by the iOS app)
- **Kotlin** and **AGP** versions as in `libs.versions.toml`

### Android

```bash
./gradlew :composeApp:assembleDebug
```

Run on device/emulator from Android Studio or:

```bash
./gradlew :composeApp:installDebug
```

### iOS

1. Build the Compose framework (from project root):

   ```bash
   ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
   ```

   Or use the iOS run task that Xcode uses (same Gradle task that the Xcode project is configured to call).

2. Open `iosApp/iosApp.xcodeproj` in Xcode, select target/device or simulator, then Run.

The iOS app uses the shared framework built from `composeApp` and presents the Compose UI via `MainViewController()`.

---

## Configuration

- **App ID / Bundle ID**
  - **Android**: `composeApp/build.gradle.kts` → `namespace` and `defaultConfig.applicationId` (e.g. `com.brainx.movie_app`).
  - **iOS**: `iosApp/Configuration/Config.xcconfig` → `PRODUCT_BUNDLE_IDENTIFIER` (e.g. `com.brainx.movie_app` or with `$(TEAM_ID)`).
- **API keys / tokens**: In datasource DI (e.g. `movieConfigModule`). Prefer environment variables or a local config not committed to git.
- **Theming**: Status bar and base theme on Android are in `composeApp/src/androidMain/res/values/theme.xml`. Compose theme (colors, typography, shapes) is in `composeApp/src/commonMain/.../presentation/theme/`.

---

## Adding a New Screen

1. **Route**: Add a new `@Serializable` route in `AppRoutes` (e.g. `data class MyScreen(val id: String) : AppRoutes()`).
2. **UI state/events/intents**: Under `presentation/screens/my_screen/`, add `ui_state/`, `ui_events/`, `ui_intents/` and the main composable in `ui/`.
3. **ViewModel**: Create `MyScreenViewModel` in `viewmodel/`, take use cases/dispatchers via constructor (Koin).
4. **DI**: Register the ViewModel in `ViewModelsModule` (in composeApp) and any new use case in `domain`’s use case module.
5. **Navigation**: In `AppNavHostGraph`, add a `composable<AppRoutes.MyScreen> { ... }` and use `koinViewModel<MyScreenViewModel>()`, and from other screens call `navController.safeNavToNextScreen(AppRoutes.MyScreen(...))`.
6. **Optional**: If the screen needs new data, add a use case in `domain` and a repository method (interface in domain, implementation in datasource).

---

## Conventions & Rules

- **Layers**: UI (composeApp) → domain → datasource. No UI or infra imports in domain; no direct ktor/room/datastore imports in composeApp or bootstrapDI.
- **DI**: All modules are assembled in `initKoin()`; Android adds `androidContext`. ViewModels are created with `koinViewModel<>()` in the nav graph.
- **Navigation**: Type-safe only; pass serializable args (e.g. JSON string for complex objects) and decode in the destination.
- **Threading**: Use cases and repository calls run on IO/default dispatchers; ViewModels inject `CoroutineDispatcher` and use `flowOn(ioDispatcher)` and `viewModelScope.launch` as needed.
- **State**: One main state object per screen; prefer `MutableStateFlow.update { }` and single emission for events via `Channel`.
- **Naming**: Packages and classes follow the existing pattern (e.g. `*UiState`, `*UiEvents`, `*UiIntents`, `*ViewModel`, `*Screen`).

---

This README reflects the current structure and behavior of the project. For dependency versions and exact module names, refer to `settings.gradle.kts`, `gradle/libs.versions.toml`, and each module’s `build.gradle.kts`.

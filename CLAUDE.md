# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
./gradlew assembleDebug      # Build debug APK
./gradlew assembleRelease    # Build release APK
./gradlew installDebug       # Build and install debug APK on connected device
./gradlew test               # Run unit tests
./gradlew connectedAndroidTest  # Run instrumented tests on device
```

APK output: `app/build/outputs/apk/`

## Build Requirements

- **Java 17** (Gradle JVM must be Java 17 — set in `.idea/gradle.xml` via `gradleJvm`)
- **Gradle 8.13** / **AGP 8.13.2**
- **compileSdk 36**, **targetSdk 34**, **minSdk 26**
- Realm plugin applied at root `build.gradle` level (`io.realm:realm-gradle-plugin:10.19.0`)

## Architecture

**MVVM with AndroidX Data Binding** — `app/src/main/java/com/kandaidea/mobilegis/`

- `MainActivity.java` — Large central activity (~51KB) handling map rendering, overlay interactions, drawer navigation, and camera
- `MyApplication.java` — Initializes Realm
- `View/` — UI Activities (Login, Search, UserLocations, Settings, Feedback, ContactUs)
- `viewmodel/` — ViewModels with business logic for each View (package is lowercase — the capital-V `ViewModel` name conflicted with the AndroidX class in data binding code generation)
- `DataModel/` — Data layer:
  - `Constants.java` — Server base URL, overlay type IDs, default map coordinates
  - `LayerManager.java` — HashMap-based overlay manager (overlays keyed by type ID)
  - `Retrofit/` — `API.java` (interface), `RetrofitClientInstance.java` (singleton), `RetrofitMethods.java` (wrapped calls)
  - `Realm/` — `RealmUserOverlays.java` for local persistence of user-drawn overlays
  - `Models/` — DTOs for API requests/responses
- `Adapers/` — RecyclerView adapters (note: package name has typo — "Adapers" not "Adapters")

## Key Libraries

| Library | Version | Purpose |
|---------|---------|---------|
| OSMDroid | 6.1.20 | OpenStreetMap rendering |
| OSMBonusPack | 6.5.1 (local AAR in `/libs`) | Routing (OSRM), KML support |
| Realm | 10.19.0 | Local database for overlays |
| Retrofit2 + RxJava2 | 3.0.0 / 2.2.21 | Network calls |
| Dagger2 | 2.59.2 | Dependency injection |
| QuadFlask colorpicker | 0.0.15 | Color picker (via JitPack) |

## Backend API

Base URL defined in `Constants.java`: `http://192.168.0.25/AppServer/KService.svc/`

Key endpoints (all POST):
- `/Login` — authentication, returns token
- `/GetPrimaryResult` / `/GetFinalResult` — two-step location search
- `/GetAddress` — reverse geocoding
- `/GetSector` — geographic sectors displayed as overlays
- `/MySendUserLocations`, `/SendFile` — upload user data
- `/SendFeedback` — user feedback submission

## Map Overlay System

Overlays are managed through `LayerManager` (HashMap keyed by overlay type ID). Overlay type constants are in `Constants.java`. `CalculateOverlay.java` handles geometric calculations; `OverlayString.java` handles serialization. User-drawn overlays are persisted to Realm via `RealmUserOverlays`. `UserOverlayAdapter` drives the drawer layer list.

## AndroidX Migration Notes

The project was migrated from the old `com.android.support` library to AndroidX. Key things to be aware of:
- `android.enableJetifier=true` is set in `gradle.properties` to handle transitive support-library dependencies from older libs (osmbonuspack)
- The `viewmodel/` package name is lowercase — changing it back to `ViewModel` will break data binding code generation

# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
./gradlew build              # Build the project
./gradlew assembleDebug      # Build debug APK
./gradlew assembleRelease    # Build release APK
./gradlew installDebug       # Build and install debug APK on connected device
./gradlew test               # Run unit tests
./gradlew connectedAndroidTest  # Run instrumented tests on device
```

APK output: `app/build/outputs/apk/`

## Architecture

**MVVM with Data Binding** — `app/src/main/java/com/kandaidea/mobilegis/`

- `MainActivity.java` — Main map screen; large central activity (~51KB) that handles map rendering, overlay interactions, drawer navigation, and camera
- `MyApplication.java` — App entry point; initializes Realm and ACRA crash reporting
- `View/` — UI Activities (Login, Search, UserLocations, Settings, Feedback, ContactUs)
- `ViewModel/` — ViewModels with business logic for each View
- `DataModel/` — Data layer:
  - `Constants.java` — All app-wide constants: server base URL, overlay type IDs, default map coordinates
  - `LayerManager.java` — HashMap-based overlay manager for the map (replaces earlier list-based approach)
  - `Retrofit/` — Network layer: `API.java` (interface), `RetrofitClientInstance.java` (singleton), `RetrofitMethods.java` (wrapped calls)
  - `Realm/` — Local persistence for user-drawn overlays via `RealmUserOverlays.java`
  - `Models/` — DTOs for API requests/responses
- `Adapers/` — RecyclerView adapters (note: package name has typo — "Adapers" not "Adapters")

## Key Libraries

| Library | Purpose |
|---------|---------|
| OSMDroid 6.0.2 | OpenStreetMap rendering |
| OSMBonusPack 6.5.1 (local AAR in `/libs`) | Routing (OSRM), KML support |
| Retrofit2 + RxJava2 | Network calls |
| Realm 5.0.1 | Local database for overlays |
| Dagger2 | Dependency injection |
| ACRA | Crash reporting → `ReportDialogActivity` |
| Chroma | Color picker for overlay styling |

## Backend API

Base URL defined in `Constants.java`: `http://192.168.0.25/AppServer/KService.svc/`

Key endpoints (all POST):
- `/Login` — authentication, returns token
- `/GetPrimaryResult` / `/GetFinalResult` — two-step search
- `/GetAddress` — reverse geocoding
- `/GetSector` — geographic sectors displayed as overlays
- `/MySendUserLocations`, `/SendFile` — upload user data
- `/SendFeedback`, `/ReportIssue` — feedback and crash reporting

## Map Overlay System

Overlays are managed through `LayerManager` (HashMap keyed by overlay type ID). Overlay type constants are defined in `Constants.java`. `CalculateOverlay.java` handles geometric calculations; `OverlayString.java` handles serialization. User-drawn overlays are persisted to Realm via `RealmUserOverlays`. The `UserOverlayAdapter` handles the drawer list of layers.

## Data Binding

Data Binding is enabled. Layout files in `res/layout/` use `<layout>` tags. ViewModels expose `ObservableField` / `LiveData` for binding.

## Configuration Notes

- **compileSdk / targetSdk:** 28 (Android 9), **minSdk:** 15
- **Java 1.8** source/target compatibility
- Realm plugin applied at project level (`build.gradle` root)
- Local AAR dependency: `app/libs/osmbonuspack_v6.5.1.aar`

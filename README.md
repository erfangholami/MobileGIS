# MobileGIS

An Android GIS application for viewing, drawing, and managing geospatial data on interactive maps powered by OpenStreetMap.

## Features

- **Interactive Map** — OpenStreetMap-based map with multiple configurable tile sources
- **Draw Overlays** — Draw polygons and polylines with custom colors and stroke width; place markers; save layers to local database
- **Layer Management** — Toggle, rename, and delete saved overlay layers from a side drawer
- **Search** — Two-step location search (primary results → detailed result with geometry); results displayed on map
- **User Locations** — Save, manage, and export GPS-tracked locations; sync to server
- **Navigation** — Go-to coordinate, find directions via OSRM routing
- **Camera & Photos** — Capture photos linked to map locations; take map screenshots
- **Sector Display** — Load and render geographic sectors from the backend as map overlays
- **Feedback** — In-app feedback form submitted to server

## Architecture

MVVM with AndroidX Data Binding.

```
app/src/main/java/com/kandaidea/mobilegis/
├── MainActivity.java          # Main map screen
├── MyApplication.java         # App init (Realm)
├── View/                      # Activities (Login, Search, UserLocations, Settings, Feedback, ContactUs)
├── viewmodel/                 # ViewModels for each Activity
├── DataModel/
│   ├── Constants.java         # Server URL, overlay type IDs, default coordinates
│   ├── LayerManager.java      # HashMap-based overlay manager
│   ├── Retrofit/              # API interface, HTTP client, wrapped calls
│   ├── Realm/                 # Local overlay persistence
│   └── Models/                # DTOs
└── Adapers/                   # RecyclerView adapters
```

## Tech Stack

| Library | Version | Purpose |
|---------|---------|---------|
| OSMDroid | 6.1.20 | Map rendering |
| OSMBonusPack | 6.5.1 | Routing, KML support |
| Realm | 10.19.0 | Local database |
| Retrofit2 + RxJava2 | 3.0.0 / 2.2.21 | Networking |
| Dagger 2 | 2.59.2 | Dependency injection |

## Requirements

- Java 17
- Android SDK 36 (compile), 34 (target), 26 (minimum)
- Gradle 8.13 / AGP 8.13.2

## Build

```bash
./gradlew assembleDebug      # Debug APK
./gradlew assembleRelease    # Release APK
./gradlew installDebug       # Build and install on connected device
```

APK output: `app/build/outputs/apk/`

## Backend

The app connects to a configurable backend server. Update `BASE_URL` in `DataModel/Constants.java` to point to your server.

API endpoints (all POST):

| Endpoint | Description |
|----------|-------------|
| `/Login` | User authentication |
| `/GetPrimaryResult` | Search by keyword |
| `/GetFinalResult` | Fetch detailed result by ID |
| `/GetAddress` | Reverse geocoding |
| `/GetSector` | Load geographic sectors |
| `/MySendUserLocations` | Upload saved locations |
| `/SendFile` | Upload files |
| `/SendFeedback` | Submit feedback |

## Local Storage

User-drawn overlays are persisted to Realm. Photos and screenshots are saved to external storage under `/MobileGIS/`.

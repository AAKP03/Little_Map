# Little Map: Simple Distance Tracker

A lightweight Android app that captures two GPS location points — a start and an end — and calculates the straight-line distance walked between them.

## Features

- **Set Start Point** — captures the device's current GPS location and stores it.
- **Set End Point** — captures a second location and calculates the distance from the stored start point using `Location.distanceTo()`.
- **Coordinate display** — shows precise latitude/longitude for both points.
- **Human-readable address** — reverse-geocodes each point into a readable address (locality, province, country) as a friendlier secondary display alongside the raw coordinates.
- **Guard logic** — the "Set End Point" button stays disabled until a start point has been captured, preventing invalid distance calculations.
- **Runtime permission handling** — requests `ACCESS_FINE_LOCATION` at runtime with graceful fallback messaging if denied.

## Tech Stack

| Component | Details |
|---|---|
| Language | Kotlin |
| Min SDK | API 24 (Android 7.0) |
| Location API | [Fused Location Provider](https://developers.google.com/location-context/fused-location-provider) (`com.google.android.gms:play-services-location:21.3.0`) |
| Geocoding | Android `Geocoder` (reverse geocoding, requires internet) |
| Distance calculation | `android.location.Location.distanceTo()` |

## Permissions Used

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.INTERNET" />
```

- **Location permissions** — required to fetch GPS coordinates via the Fused Location Provider.
- **Internet permission** — required for reverse-geocoding coordinates into readable addresses.

## Getting Started

1. Clone the repository:
```bash
   git clone https://github.com/AAKP03/Little_Map.git
```
2. Open the project in Android Studio.
3. Let Gradle sync complete.
4. Run on a physical device (recommended) or emulator with a Google Play Store–enabled system image.

## How It Works

1. On launch, the app requests location permission if not already granted.
2. Tapping **Set Start Point** fetches the current location via `FusedLocationProviderClient.getCurrentLocation()`, stores it, and displays both coordinates and a reverse-geocoded address.
3. Once a start point exists, **Set End Point** becomes enabled.
4. Tapping **Set End Point** fetches a second location and calls `startLocation.distanceTo(endLocation)` to compute the distance in meters, displaying the result.

## Project Structure

```
app/src/main/java/com/example/littlemap/
└── MainActivity.kt        # Permissions, location fetching, distance calculation, UI binding

app/src/main/res/layout/
└── activity_main.xml      # UI: buttons, coordinate displays, distance readout

app/src/main/AndroidManifest.xml   # Permissions declaration
app/build.gradle.kts               # Play Services Location dependency
```


## Contributors

| Role | Responsibility |
|---|---|
| Project Setup & Permissions Lead | Manifest permissions, Play Services dependency, runtime permission flow |
| UI Layout | App layout — buttons, coordinate fields, distance display |
| Start Point Logic | Capture and store starting GPS location |
| End Point & Distance Logic | Capture ending GPS location, calculate distance via `distanceTo()` |
| Guard Logic & Testing | Button state guards, end-to-end testing with mock locations |

## Testing Notes

- Tested using mock locations set a few minutes apart in the Android Studio emulator's Extended Controls, as well as on a physical device with real GPS movement.
- Distance results were confirmed non-zero and consistent with expected real-world distances between test points.

## Possible Future Improvements

- Convert and display distance in kilometers when it exceeds 1000m.
- Add a "Reset" button to clear both points and start over.
- Cache addresses to reduce repeated Geocoder calls.

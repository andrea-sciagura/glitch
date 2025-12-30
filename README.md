# Glitch Compose 🧩

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-2.3.0-purple.svg)](https://kotlinlang.org/)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.9.3-blue.svg)](https://github.com/JetBrains/compose-multiplatform)
[![Platform](https://img.shields.io/badge/platform-Android%20%7C%20iOS%20%7C%20Desktop%20%7C%20Web-lightgrey.svg)](#platform-support)

A lightweight and highly configurable glitch animation library for **Compose Multiplatform**. Add high-quality digital glitch effects to any Composable with just a single Modifier.

---

## ✨ Features

- **Horizontal Displacement**: Slices elements into horizontal bands with random offsets.
- **Chromatic Aberration**: Classic RGB-shift (red/blue ghosting) for that authentic digital look.
- **Dual Implementations**: 
    - `animateGlitch`: High-performance, idiomatic, and fully inspectable via **Compose Animation Inspector**.
    - `glitchEffect`: Highly flexible, coroutine-based for non-deterministic "true random" patterns.
- **Configurable Timing**: Fine-grained control over delay, duration, and loop intervals.
- **Zero Dependencies**: Lightweight, targeting only the Compose runtime and foundation.

## 📦 Installation

### Gradle (Kotlin DSL)

Add the dependency to your `commonMain` source set:

```kotlin
commonMain.dependencies {
    implementation("xyz.andrea-sciagura.anim:glitch:1.0.0")
}
```

---

## 🚀 Quick Start

Applying a glitch is as simple as adding a modifier:

```kotlin
Text(
    text = "GLITCHED",
    modifier = Modifier.animateGlitch(
        delay = 1000,    // Wait 1s before starting
        duration = 500,   // Glitch for 500ms
        interval = 2000   // Pause for 2s between cycles
    )
)
```

---

## 🛠 API Reference

### `Modifier.animateGlitch` (Recommended)

Uses the standard `InfiniteTransition` API. This is the preferred choice for most users as it integrates perfectly with Android Studio's **Animation Inspector**.

| Parameter | Type | Default | Description |
| :--- | :--- | :--- | :--- |
| `enabled` | `Boolean` | `true` | Toggle the effect on/off. |
| `delay` | `Long` | `0` | Initial delay before the first cycle. |
| `duration` | `Long` | `500` | Duration of the active glitch phase. |
| `interval` | `Long` | `2000` | Idle time between glitch phases. |

### `Modifier.glitchEffect`

A low-level implementation using `LaunchedEffect`. Use this if you need highly non-linear or randomized timing patterns that are hard to achieve with standard transitions.

---

## 📱 Platform Support

| Android | iOS | Desktop (JVM) | Web (Wasm/JS) |
| :---: | :---: | :---: | :---: |
| ✅ | ✅ | ✅ | ✅ |

---

## ⚙️ Performance & Best Practices

- **Draw-Phase Optimization**: Both modifiers operate in the `draw` phase to avoid unnecessary recompositions.
- **GPU Usage**: The chromatic aberration effect uses `canvas.saveLayer`, which is a GPU-intensive operation. Avoid applying the effect to extremely large UI trees on low-end hardware.
- **Selective Activation**: Use the `enabled` parameter to disable animations when they are off-screen or during performance-critical transitions.

---

## 🤝 Contributing

Contributions are welcome! If you find a bug or have a feature request, please open an issue.

1. Fork the repository.
2. Create your feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

---

## 📄 License

```
Copyright 2024 Andrea Sciagura

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---

## 🏗 Project Structure & Development

- `:glitch-library`: The core Kotlin Multiplatform library.
- `:composeApp`: A sample application demonstrating the glitch effects on Android, iOS, Desktop, and Web.

### Running the Sample App

- **Android**: `./gradlew :composeApp:assembleDebug`
- **Desktop**: `./gradlew :composeApp:run`
- **Web (Wasm)**: `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`
- **iOS**: Open `iosApp/iosApp.xcworkspace` in Xcode.

---

## 💡 Acknowledgements

- Inspired by classic cyberpunk aesthetics.
- Built with [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform).
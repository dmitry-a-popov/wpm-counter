# Words Per Minute Counter (WPM Counter)

An Android application that measures a user's typing speed (Words Per Minute) in real time. The user is shown a sample text, types it inside the app, and receives:
- Live typing speed updates
- Error highlighting
- Final performance summary

This project serves as a practical example of applying a modern (2025) Android technology stack and modular architecture in a production-style scenario.

## Key Features
- Real-time WPM calculation based on keystroke analytics
- Mistake tracking and visual feedback while typing
- Modular separation of concerns (core services, domain features, app shell)
- Behavioral keystroke event logging (analytics subsystem)
- Simple login flow (user name persistence)
- MVI-style UI state handling in feature modules

## Architecture Overview
The codebase is organized into three layers:
- `app` – application entry point (Application + Activity), wiring DI and navigation
- `core` – shared domain/services (analytics, common utilities, logging, user, UI base)
- `feature` – independent verticals (`login`, `typing`) exposing API + implementation modules

Within features, presentation uses a lightweight MVI approach (`UiState`, `UiIntent`, one-time events, base ViewModel support).

## 2025 Technology Stack (All Present in Source)
- Kotlin (JVM target 17)
- Jetpack Compose UI (Material 3, previews, BOM)
- Navigation Compose
- Hilt for Dependency Injection (with KSP-based code generation)
- Room (behavioral analytics persistence; schema location configured via KSP arg)
- AndroidX DataStore (user name storage)
- Modular multi-module Gradle setup (API/impl split, version catalogs via `libs.versions.toml`)
- MVI-style state management helpers (custom `BaseMviViewModel`, one-time event flow)
- Android SDK: compileSdk = 36, targetSdk = 36, minSdk = 24
- Kotlin KSP (Hilt & Room processors)
- AndroidX Core KTX, Lifecycle Runtime, Activity Compose
- Logging abstraction (`Logger` + implementation)
- Word / text validation and screen orientation abstractions
- Dependency inversion via API modules (public contracts) and impl modules (internal logic)

## DI & Modularity
- Hilt entry point: `WpmCounterApplication`
- Per-module DI setup (e.g., `AnalyticsModule`, `CommonModule`, `LoggerModule`, `UserModule`, `TypingModule`)
- Separation allows targeted reuse and independent evolution of features

---

# Module Descriptions

---

## App Layer

# Module: app

## Purpose
Android application module providing the entry point (Application and launcher Activity), integrating Hilt dependency injection and Jetpack Compose UI, and wiring together core and feature modules.

## Dependencies on Other Modules
- :core:analytics:api
- :core:analytics:impl
- :core:common:api
- :core:common:impl
- :core:logger:api
- :core:logger:impl
- :core:user:api
- :core:user:impl
- :feature:login:api
- :feature:login:impl
- :feature:typing:api
- :feature:typing:impl

---

## Feature Layer

This layer contains the independent business features of the application.

# Module: feature:login:api
## Purpose
Public contracts for the Login feature. Provides navigation entry definition via `LoginRoute` (used to declare or build the login destination for app navigation).
## Dependencies on Other Modules
(None)

# Module: feature:login:impl
## Purpose
Implements the Login feature UI and presentation layer. Contains:
- MVI artifacts: `UiIntent`, `UiState`, `OneTimeEvent`
- Compose screen: `LoginScreen`
- ViewModel: `LoginViewModel`
- Navigation helpers: `LoginNavigation`
  Integrates with user domain (user name), shared UI utilities, and logging.
## Dependencies on Other Modules
- :feature:login:api
- :core:logger:api
- :core:ui-common
- :core:user:api

# Module: feature:typing:api
## Purpose
Public contracts for the Typing feature. Exposes navigation entry via `TypingRoute` for wiring the typing screen into the app navigation graph.
## Dependencies on Other Modules
(None)

# Module: feature:typing:impl
## Purpose
Implements Typing feature domain + UI:
- MVI/UI artifacts: `UiIntent`, `UiState`, `OneTimeEvent`, `TypingScreen`, `TextMarker`
- ViewModel: `TypingViewModel`
- Domain logic: `CurrentWordIndicesCalculator` (+ `CurrentWordIndicesCalculatorImpl`), `MistakeIndicesCalculator` (+ `MistakeIndicesCalculatorImpl`), sample text retrieval (`SampleTextRepository`, `SampleTextRepositoryImpl`)
- Navigation helpers: `TypingNavigation`
- DI setup: `TypingModule`
  Uses analytics (typing speed), common utilities, user info, shared UI, and logging.
## Dependencies on Other Modules
- :feature:typing:api
- :core:analytics:api
- :core:common:api
- :core:logger:api
- :core:ui-common
- :core:user:api

---

## Core Layer

This layer provides shared utilities and abstractions that can be used by any feature module.

# Module: core:analytics:api
## Purpose
Defines analytics domain contracts: keystroke tracking (`TrackKeyPressUseCase`), clearing recorded events (`ClearEventsUseCase`), typing speed retrieval (`GetTypingSpeedUseCase`), and the typing speed state model (`TypingSpeedState`).
## Dependencies on Other Modules
- :core:common:api

# Module: core:analytics:impl
## Purpose
Implements analytics domain logic: use case implementations (`TrackKeyPressUseCaseImpl`, `ClearEventsUseCaseImpl`, `GetTypingSpeedUseCaseImpl`), session state handling (`TypingSessionStateStore`, `TypingSessionUpdaterImpl`), speed calculation (`SpeedCalculatorImpl`), behavioral analytics persistence (`BehavioralAnalyticsRepositoryImpl`, `BehavioralAnalyticsDatabaseDataSource`, `KeystrokeEventEntity`, `OrientationConverter`), event mapping (`KeystrokeEventMapperImpl`), in‑memory session store (`InMemoryTypingSessionStateStore`), repository interface (`BehavioralAnalyticsRepository`), and DI wiring (`AnalyticsModule`).
## Dependencies on Other Modules
- :core:analytics:api
- :core:common:api
- :core:logger:api

# Module: core:common:api
## Purpose
Provides shared domain/service abstractions: word counting (`WordCounter`), screen orientation access (`ScreenOrientationProvider`, `ScreenOrientation`), time sourcing (`TimeProvider`), text validation (`TextValidator`, `WordComparisom`).
## Dependencies on Other Modules
(None)

# Module: core:common:impl
## Purpose
Concrete implementations of common services: `WordCounterImpl`, `TextValidatorImpl`, `SystemTimeProvider`, `ScreenOrientationProviderImpl`, plus DI setup (`CommonModule`).
## Dependencies on Other Modules
- :core:common:api
- :core:logger:api

# Module: core:logger:api
## Purpose
Defines logging contract via the `Logger` interface.
## Dependencies on Other Modules
(None)

# Module: core:logger:impl
## Purpose
Provides logging implementation (`LoggerImpl`) and DI module (`LoggerModule`).
## Dependencies on Other Modules
- :core:logger:api

# Module: core:ui-common
## Purpose
Common UI/architecture utilities: one‑shot event flow helper (`OneTimeEventFlow`) and base MVI ViewModel abstraction (`BaseMviViewModel`).
## Dependencies on Other Modules
(None)

# Module: core:user:api
## Purpose
User domain interfaces: persistence access (`UserRepository`) and user name saving use case (`SaveUserNameUseCase`).
## Dependencies on Other Modules
(None)

# Module: core:user:impl
## Purpose
User domain implementations: `UserRepositoryImpl`, `UserDataStoreDataSource` (DataStore backed), `SaveUserNameUseCaseImpl`, and DI wiring (`UserModule`).
## Dependencies on Other Modules
- :core:user:api

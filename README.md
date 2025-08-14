# eMarket

eMarket is a modern Android shopping application that allows users to browse products, add them to a cart, and manage their shopping experience with a clean and responsive interface. The project follows **Clean Architecture** principles and the **MVVM** pattern to ensure scalability, maintainability, and testability.

---

## Project Structure

The project follows a **layered architecture** with clear separation of concerns:

### 1. **Data Layer**
- **Local Storage**: `Room Database` with DAO interfaces.
- **Remote Data Source**: Retrofit API calls for fetching product data.
- **Repositories**: Implement data operations, combining local and remote sources.

### 2. **Domain Layer**
- **Entities**: Core business models like `Product` and `CartItem`.
- **Use Cases**: Application-specific business logic such as `GetProductsUseCase` and `InsertProductToLocalUseCase`.
- **Repository Interfaces**: Define contracts for data access.

### 3. **UI Layer (Presentation)**
- **Fragments**: `HomeFragment`, `CartFragment`, `ProductFragment` for user interaction.
- **ViewModels**: Manage UI state and call use cases.
- **Adapters**: `ProductAdapter` and `CartAdapter` for RecyclerViews.
- **State Management**: `StateFlow` and `LiveData` for reactive UI updates.

---

## Technologies Used

- **Language**: Kotlin
- **Architecture**: Clean Architecture + MVVM
- **UI**: XML layouts, RecyclerView, ViewBinding
- **Local Storage**: Room Database
- **Networking**: Retrofit
- **Image Loading**: Coil
- **Coroutines**: For asynchronous programming
- **Navigation**: Jetpack Navigation Component
- **Testing**: JUnit, MockK, Robolectric

---

## Testing

This project includes both unit and UI tests to ensure code quality, reliability, and maintainability.

### Testing Frameworks Used

- **JUnit 4** – Unit testing framework for Java/Kotlin, used for writing and running test cases.
- **MockK** – Library for creating mock objects and verifying interactions, making it easier to test isolated components.
- **Robolectric** – Framework for running Android UI tests without the need for an emulator, enabling faster and more consistent test execution.

---

## How It Works

1. **Home Screen** loads products from the remote API and displays them in a grid.
2. **Add to Cart** saves products to both in-memory cart (`CartManager`) and Room database for persistence.
3. **Cart Screen** displays items from Room; quantities and total price are updated live.
4. **Favorites** toggle changes the icon and updates the product state.
5. When the app is restarted, the cart is restored from Room automatically.

---


## Setup & Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/elifkoseler/emarket.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle dependencies.
4. Run on an emulator or Android device.



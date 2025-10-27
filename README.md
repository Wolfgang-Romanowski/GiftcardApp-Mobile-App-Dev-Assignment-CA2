# Gift Card Manager

An Android application for tracking and managing gift cards.

## About

Gift Card Manager helps users keep track of their gift cards in one convenient place. Users can store card details including store name, balance, card number, expiry date, and notes.

## Features

- Add new gift cards with full details
- View all cards in a scrollable list
- Search and filter cards by store name or card number
- Edit existing cards
- Delete cards (button or swipe gesture with undo)
- Date picker for expiry dates
- Persistent storage across app sessions
- Material Design interface

## Technical Details

- **Language:** Kotlin
- **Min SDK:** 26 (Android 8.0)
- **Target SDK:** 34
- **Architecture:** Model-View-Controller pattern
- **Data Storage:** SharedPreferences with JSON serialization (Gson)
- **UI Components:** RecyclerView, CardView, SearchView, DatePickerDialog
- **Data Passing:** Parcelable models
- **Testing:** JUnit unit tests

## Dependencies

- AndroidX libraries (Core, AppCompat, Material, RecyclerView)
- Timber (logging)
- Gson (JSON serialization)

## Development

This project was developed as part of Mobile App Development coursework, demonstrating Android best practices including ViewBinding, proper data persistence, Material Design guidelines, and Git workflow with feature branching.

## Version History

- v0.6.0 - Unit tests and UI enhancements
- v0.5.0 - Main branch merge
- v0.4.1 - Search functionality
- v0.4.0 - Swipe-to-delete
- v0.3.3 - Edit/delete features
- v0.3.2 - Data persistence
- v0.3.1 - Parcelable models
- v0.3.0 - Base application
- v0.2.0 - Mobile functionality
- v0.1.0 - Initial commit
## License

Created for educational purposes.

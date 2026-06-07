package com.example.guardia

import android.app.Application
import com.example.guardia.database.AppDatabase
import com.example.guardia.database.GuardiaRepository

/*
 GuardiaApplication
 Custom Application class that serves as the entry point for the app.
 It provides a centralized location for manual dependency injection,
 ensuring that only one instance of the database and repository is created
 and shared across the entire application lifecycle.
 */
class GuardiaApplication : Application() {

    /*
     Lazily initialized database instance.
     It is created only when first accessed, optimizing startup time.
     */
    val database by lazy { AppDatabase.getDatabase(this) }

    /*
     Lazily initialized repository instance.
     This provides the single source of truth for data access to the rest of the app.
     */
    val repository by lazy { GuardiaRepository(database) }
}

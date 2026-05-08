package com.example.recruitment_portal

import androidx.appcompat.app.AppCompatDelegate

object SessionManager {
    var isLoggedIn: Boolean = false

    init {
        // Force light mode globally for XML activities
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}

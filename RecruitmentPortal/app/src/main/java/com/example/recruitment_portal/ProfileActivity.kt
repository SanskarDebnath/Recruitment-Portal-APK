package com.example.recruitment_portal

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Handle back button using the modern OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                closePage(null)
            }
        })
    }

    /**
     * Closes the page with a slide-out-bottom animation.
     * @param view The view that was clicked (optional).
     */
    fun closePage(view: View?) {
        finish()
        overridePendingTransition(0, R.anim.slide_out_bottom)
    }
}
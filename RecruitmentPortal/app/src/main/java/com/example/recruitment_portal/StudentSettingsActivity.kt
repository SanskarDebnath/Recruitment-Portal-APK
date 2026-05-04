package com.example.recruitment_portal

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class StudentSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_settings)

        setupRow(R.id.rowEmailNotif, "Email Notifications")
        setupRow(R.id.rowJobAlerts, "Job Alerts")
        setupRow(R.id.rowChangePass, "Change Password")
        setupRow(R.id.rowTwoStep, "Two-Step Verification")

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                closePage(null)
            }
        })
    }

    private fun setupRow(id: Int, title: String) {
        val row = findViewById<View>(id)
        row.findViewById<TextView>(R.id.tvSettingTitle).text = title
    }

    fun closePage(view: View?) {
        vibrate(30)
        finish()
        overridePendingTransition(0, R.anim.slide_out_bottom)
    }
}
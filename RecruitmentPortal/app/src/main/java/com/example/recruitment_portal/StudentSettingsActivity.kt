package com.example.recruitment_portal

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.materialswitch.MaterialSwitch

class StudentSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_settings)

        // Account Preferences
        setupSwitchRow(R.id.rowEmailNotif, "Email Notifications", R.drawable.ic_notifications_24, true)
        setupSwitchRow(R.id.rowJobAlerts, "Job Alerts", R.drawable.ic_work_24, true)

        // Security
        setupClickRow(R.id.rowChangePass, "Change Password", R.drawable.ic_info_24) {
            showSimpleDialog("Change Password", "Verification link sent to your email.")
        }
        setupSwitchRow(R.id.rowTwoStep, "Two-Step Verification", R.drawable.ic_verified_24, false)

        // Privacy & Support
        setupClickRow(R.id.rowPrivacyPolicy, "Privacy Policy", R.drawable.ic_description_24) {
            showSimpleDialog("Privacy Policy", "Your data is secure and encrypted.")
        }
        setupClickRow(R.id.rowTermsOfService, "Terms of Service", R.drawable.ic_document_24) {
            showSimpleDialog("Terms", "Standard government recruitment terms apply.")
        }
        setupClickRow(R.id.rowHelpSupport, "Help & Support", R.drawable.ic_info_24) {
            showSimpleDialog("Support", "Contact us at support@portal.gov.in")
        }
        setupClickRow(R.id.rowAboutApp, "About Application", R.drawable.ic_settings_24) {
            showSimpleDialog("About", "Recruitment Portal v2.4.0\nBuilt for transparency.")
        }

        findViewById<View>(R.id.btnDeactivate).setOnClickListener {
            it.startClickAnimation()
            Toast.makeText(this, "Deactivation request submitted.", Toast.LENGTH_LONG).show()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                closePage(null)
            }
        })
    }

    private fun setupSwitchRow(id: Int, title: String, iconRes: Int, isChecked: Boolean) {
        val row = findViewById<View>(id)
        row.findViewById<TextView>(R.id.tvSettingTitle).text = title
        row.findViewById<ImageView>(R.id.ivSettingIcon).setImageResource(iconRes)
        row.findViewById<TextView>(R.id.tvArrow).visibility = View.GONE
        
        val switch = row.findViewById<MaterialSwitch>(R.id.switchSetting)
        switch.visibility = View.VISIBLE
        switch.isChecked = isChecked
        
        row.setOnClickListener {
            this.vibrate(20)
            switch.toggle()
        }

        switch.setOnCheckedChangeListener { _, checked ->
            val status = if (checked) "enabled" else "disabled"
            Toast.makeText(this, "$title $status", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupClickRow(id: Int, title: String, iconRes: Int, action: () -> Unit) {
        val row = findViewById<View>(id)
        row.findViewById<TextView>(R.id.tvSettingTitle).text = title
        row.findViewById<ImageView>(R.id.ivSettingIcon).setImageResource(iconRes)
        row.setOnClickListener {
            it.startClickAnimation()
            this.vibrate(20)
            action()
        }
    }

    private fun showSimpleDialog(title: String, message: String) {
        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    fun closePage(view: View?) {
        vibrate(30)
        finish()
        overridePendingTransition(0, R.anim.slide_out_bottom)
    }
}

package com.example.recruitment_portal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        findViewById<View>(R.id.btnLogout).setOnClickListener {
            it.startClickAnimation()
            SessionManager.isLoggedIn = false
            finish()
        }

        findViewById<View>(R.id.btnNotifications).setOnClickListener {
            it.startClickAnimation()
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_bottom, 0)
        }

        setupAppClicks()

        val bottomNav = findViewById<BottomNavigationView>(R.id.dashBottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            this.vibrate() // Extension function from ViewExtensions
            when (item.itemId) {
                R.id.dash_home -> true
                R.id.dash_activity -> {
                    val intent = Intent(this, ApplyOnlineActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_bottom, 0)
                    false
                }
                R.id.dash_profile -> {
                    val intent = Intent(this, StudentProfileActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_bottom, 0)
                    false
                }
                R.id.dash_settings -> {
                    val intent = Intent(this, StudentSettingsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_bottom, 0)
                    false
                }
                else -> false
            }
        }
    }

    private fun setupAppClicks() {
        val appIds = listOf(R.id.app1, R.id.app2, R.id.app3)
        appIds.forEach { id ->
            findViewById<View>(id).setOnClickListener {
                it.startClickAnimation()
                showMilestoneModal()
            }
        }
    }

    private fun showMilestoneModal() {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_application_tracking, null)
        dialog.setContentView(view)
        
        view.findViewById<View>(R.id.btnTrackClose).setOnClickListener {
            it.startClickAnimation()
            dialog.dismiss()
        }

        val container = view.findViewById<ViewGroup>(R.id.milestoneContainer)
        setupMilestones(container)

        dialog.show()
    }

    private fun setupMilestones(container: ViewGroup) {
        val titles = listOf("Application Submitted", "Document Verified", "Technical Interview", "HR Round")
        val dates = listOf("20 May 2026", "22 May 2026", "25 May 2026", "Pending")
        
        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i)
            if (i < titles.size) {
                child.findViewById<TextView>(R.id.tvMilestoneTitle).text = titles[i]
                child.findViewById<TextView>(R.id.tvMilestoneDate).text = dates[i]
                
                if (dates[i] == "Pending") {
                    child.findViewById<View>(R.id.vStepIndicator).backgroundTintList = 
                        android.content.res.ColorStateList.valueOf(getColor(R.color.text_secondary))
                }
            }
            if (i == container.childCount - 1) {
                child.findViewById<View>(R.id.vTimelineConnector).visibility = View.GONE
            }
        }
    }
}
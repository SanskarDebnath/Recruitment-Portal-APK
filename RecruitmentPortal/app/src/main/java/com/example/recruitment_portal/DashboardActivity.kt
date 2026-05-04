package com.example.recruitment_portal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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

        findViewById<View>(R.id.btnSync).setOnClickListener {
            it.startClickAnimation()
            NotificationHelper.showStackedNotification(findViewById(R.id.notificationStack), "Data Synced Successfully!")
        }

        setupAppClicks()
        setupToggles()
        setupScrollListener()

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

    private fun setupScrollListener() {
        val mainScrollView = findViewById<androidx.core.widget.NestedScrollView>(R.id.mainScrollView) ?: return
        val fabScroll = findViewById<View>(R.id.fabScroll) ?: return
        val tvScrollLabel = findViewById<TextView>(R.id.tvScrollLabel)
        val ivScrollIcon = findViewById<android.widget.ImageView>(R.id.ivScrollIcon)
        var isAtBottom = false

        fabScroll.setOnClickListener {
            it.startClickAnimation()
            if (isAtBottom) {
                mainScrollView.smoothScrollTo(0, 0)
            } else {
                mainScrollView.smoothScrollTo(0, mainScrollView.getChildAt(0).height)
            }
        }

        mainScrollView.setOnScrollChangeListener(androidx.core.widget.NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            val totalContentHeight = v.getChildAt(0).height
            val visibleHeight = v.height
            if (scrollY + visibleHeight >= totalContentHeight - 10) { 
                if (!isAtBottom) {
                    isAtBottom = true
                    tvScrollLabel.text = "Scroll up"
                    ivScrollIcon.setImageResource(R.drawable.ic_chevron_up)
                }
            } else {
                if (isAtBottom) {
                    isAtBottom = false
                    tvScrollLabel.text = "Scroll down"
                    ivScrollIcon.setImageResource(R.drawable.ic_chevron_down)
                }
            }
        })
    }

    private fun setupAppClicks() {
        findViewById<View>(R.id.app1)?.setOnClickListener { it.startClickAnimation(); showMilestoneModal() }
        findViewById<View>(R.id.app2)?.setOnClickListener { it.startClickAnimation(); showMilestoneModal() }
        findViewById<View>(R.id.admit1)?.setOnClickListener { it.startClickAnimation(); showMilestoneModal() }
        
        findViewById<View>(R.id.btnResumeDraft)?.setOnClickListener {
            it.startClickAnimation()
            Toast.makeText(this, "Resuming Draft...", Toast.LENGTH_SHORT).show()
        }

        val resultCard = findViewById<View>(R.id.result1)
        resultCard?.findViewById<View>(R.id.btnViewResult)?.setOnClickListener { 
            it.startClickAnimation()
            showResultDetailModal() 
        }
        resultCard?.setOnClickListener { 
            it.startClickAnimation()
            showResultDetailModal() 
        }
    }

    private fun showResultDetailModal() {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_result_details, null)
        dialog.setContentView(view)
        
        view.findViewById<View>(R.id.btnResultDetailCross).setOnClickListener {
            it.startClickAnimation()
            dialog.dismiss()
        }
        view.findViewById<View>(R.id.btnDownloadScorecard).setOnClickListener {
            it.startClickAnimation()
            Toast.makeText(this, "Scorecard downloading...", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setupToggles() {
        val chartToggle = findViewById<com.google.android.material.button.MaterialButtonToggleGroup>(R.id.chartToggle)
        val layoutPie = findViewById<View>(R.id.layoutPieChart)
        val layoutBar = findViewById<View>(R.id.layoutBarChart)

        chartToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                this.vibrate(20)
                layoutPie.visibility = if (checkedId == R.id.btnPieChart) View.VISIBLE else View.GONE
                layoutBar.visibility = if (checkedId == R.id.btnBarChart) View.VISIBLE else View.GONE
            }
        }

        val ongoingToggle = findViewById<com.google.android.material.button.MaterialButtonToggleGroup>(R.id.ongoingToggle)
        val gridOngoing = findViewById<android.widget.GridLayout>(R.id.gridOngoing)

        ongoingToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                this.vibrate(20)
                
                // Update icon tints for better feedback
                val listBtn = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnOngoingList)
                val gridBtn = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnOngoingGrid)
                
                val activeColor = android.content.res.ColorStateList.valueOf(getColor(R.color.orange_main))
                val inactiveColor = android.content.res.ColorStateList.valueOf(getColor(R.color.text_secondary))
                
                listBtn.iconTint = if (checkedId == R.id.btnOngoingList) activeColor else inactiveColor
                gridBtn.iconTint = if (checkedId == R.id.btnOngoingGrid) activeColor else inactiveColor

                gridOngoing.post {
                    try {
                        val isGrid = checkedId == R.id.btnOngoingGrid
                        
                        for (i in 0 until gridOngoing.childCount) {
                            val child = gridOngoing.getChildAt(i)
                            val lp = android.widget.GridLayout.LayoutParams()
                            lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                            
                            if (isGrid) {
                                lp.width = 0
                                lp.columnSpec = android.widget.GridLayout.spec(android.widget.GridLayout.UNDEFINED, 1, 1f)
                                // In grid, use horizontal margin for spacing between columns
                                val margin = 8.dpToPx(this@DashboardActivity)
                                lp.setMargins(margin, margin, margin, margin)
                            } else {
                                lp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT
                                lp.columnSpec = android.widget.GridLayout.spec(android.widget.GridLayout.UNDEFINED, 1, 1f)
                                lp.setMargins(0, 0, 0, 16.dpToPx(this@DashboardActivity))
                            }
                            child.layoutParams = lp
                        }
                        gridOngoing.columnCount = if (isGrid) 2 else 1
                        gridOngoing.requestLayout()
                    } catch (e: Exception) {
                        android.util.Log.e("Dashboard", "Error switching views", e)
                    }
                }
            }
        }

        findViewById<View>(R.id.btnStatsInfo).setOnClickListener {
            it.startClickAnimation()
            showStatsInfoModal()
        }
    }

    private fun showStatsInfoModal() {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_stats_info, null)
        dialog.setContentView(view)
        
        view.findViewById<View>(R.id.btnStatsClose).setOnClickListener {
            it.startClickAnimation()
            dialog.dismiss()
        }

        view.findViewById<View>(R.id.btnStatsCross).setOnClickListener {
            it.startClickAnimation()
            dialog.dismiss()
        }
        dialog.show()
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
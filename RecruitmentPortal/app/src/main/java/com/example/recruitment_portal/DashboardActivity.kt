package com.example.recruitment_portal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        findViewById<View>(R.id.btnLogout).setOnClickListener {
            it.startClickAnimation()
            showLogoutConfirmation()
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
        setupOngoingApplications()
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
        findViewById<View>(R.id.admit1)?.setOnClickListener { it.startClickAnimation(); showMilestoneModal() }
        
        findViewById<View>(R.id.btnResumeDraft)?.setOnClickListener {
            it.startClickAnimation()
            showCustomSnackbar(it, "Resuming Draft...")
        }

        findViewById<View>(R.id.btnProfileMeter)?.setOnClickListener {
            it.startClickAnimation()
            startActivity(Intent(this, StudentProfileActivity::class.java))
            overridePendingTransition(R.anim.slide_in_bottom, 0)
        }

        findViewById<View>(R.id.btnMockTests)?.setOnClickListener {
            it.startClickAnimation()
            showComingSoonDialog("Mock Tests", "Unlock 100+ Free Practice Tests for SSC & UPSC.")
        }

        findViewById<View>(R.id.btnJoinMeeting)?.setOnClickListener {
            it.startClickAnimation()
            showCustomSnackbar(it, "Opening Meeting Link...")
        }

        findViewById<View>(R.id.btnDigitalLocker)?.setOnClickListener {
            it.startClickAnimation()
            showComingSoonDialog("Digital Locker", "Your e-verified documents will appear here.")
        }

        findViewById<View>(R.id.btnHelpDesk)?.setOnClickListener {
            it.startClickAnimation()
            showComingSoonDialog("Help Desk", "Our team is available 24/7. Ticket system coming soon.")
        }

        findViewById<View>(R.id.btnRefer)?.setOnClickListener {
            it.startClickAnimation()
            showCustomSnackbar(it, "Referral link copied to clipboard!")
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

    private fun showComingSoonDialog(title: String, message: String) {
        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Explore later", null)
            .show()
    }

    private fun showCustomSnackbar(view: View, message: String) {
        val snackbar = com.google.android.material.snackbar.Snackbar.make(view, message, com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(getColor(R.color.orange_main))
        snackbar.setTextColor(getColor(R.color.white))
        snackbar.show()
    }

    private fun showLogoutConfirmation() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_stats_info, null) // reuse for simple dialog or builder
        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setTitle("Confirm Logout")
            .setMessage("Are you sure you want to log out of your account?")
            .setPositiveButton("Logout") { _, _ ->
                val root = findViewById<View>(android.R.id.content)
                root.animate().alpha(0f).setDuration(300).withEndAction {
                    SessionManager.isLoggedIn = false
                    finish()
                }.start()
            }
            .setNegativeButton("Cancel", null)
            .show()
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
            showCustomSnackbar(view, "Scorecard downloading...")
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setupToggles() {
        val chartToggle = findViewById<com.google.android.material.button.MaterialButtonToggleGroup>(R.id.chartToggle)
        val layoutPie = findViewById<View>(R.id.layoutPieChart)
        val layoutBar = findViewById<View>(R.id.layoutBarChart)

        val openStatsDetail = View.OnClickListener {
            it.startClickAnimation()
            val intent = Intent(this, StatsDetailActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_bottom, 0)
        }

        layoutPie.setOnClickListener(openStatsDetail)
        layoutBar.setOnClickListener(openStatsDetail)

        chartToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                this.vibrate(20)
                layoutPie.visibility = if (checkedId == R.id.btnPieChart) View.VISIBLE else View.GONE
                layoutBar.visibility = if (checkedId == R.id.btnBarChart) View.VISIBLE else View.GONE
            }
        }

        val ongoingToggle = findViewById<com.google.android.material.button.MaterialButtonToggleGroup>(R.id.ongoingToggle)
        val rvOngoingList = findViewById<RecyclerView>(R.id.rvOngoingList)
        val vpOngoingStack = findViewById<ViewPager2>(R.id.vpOngoingStack)

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

                val isGrid = checkedId == R.id.btnOngoingGrid
                
                rvOngoingList.visibility = if (isGrid) View.GONE else View.VISIBLE
                vpOngoingStack.visibility = if (isGrid) View.VISIBLE else View.GONE

                if (isGrid && vpOngoingStack.adapter?.itemCount ?: 0 > 1) {
                    vpOngoingStack.postDelayed({
                        try {
                            vpOngoingStack.beginFakeDrag()
                            val animator = android.animation.ValueAnimator.ofFloat(0f, -120f, 0f)
                            animator.duration = 450
                            var previousValue = 0f
                            animator.addUpdateListener { animation ->
                                val currentValue = animation.animatedValue as Float
                                val diff = currentValue - previousValue
                                vpOngoingStack.fakeDragBy(diff)
                                previousValue = currentValue
                            }
                            animator.addListener(object : android.animation.AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: android.animation.Animator) {
                                    vpOngoingStack.endFakeDrag()
                                }
                            })
                            animator.start()
                        } catch (e: Exception) {
                            // Ignore fake drag errors
                        }
                    }, 300)
                }
            }
        }

        findViewById<View>(R.id.btnStatsInfo).setOnClickListener {
            it.startClickAnimation()
            showStatsInfoModal()
        }
    }

    private fun setupOngoingApplications() {
        val dummyData = listOf(
            OngoingApp("Software Developer", getString(R.string.interview_scheduled), 75, "Next: Technical Interview", R.drawable.ic_work_24, "#2563EB", "#EFF6FF"),
            OngoingApp("Data Analyst", "Assessment Pending", 40, "Next: Online Test", R.drawable.ic_chart_pie_24, "#1D4ED8", "#DBEAFE"),
            OngoingApp("UI/UX Designer", "Portfolio Reviewed", 60, "Next: HR Round", R.drawable.ic_view_list_24, "#701A75", "#FDF4FF")
        )

        val rvOngoingList = findViewById<RecyclerView>(R.id.rvOngoingList)
        val vpOngoingStack = findViewById<ViewPager2>(R.id.vpOngoingStack)

        rvOngoingList.layoutManager = LinearLayoutManager(this)
        rvOngoingList.adapter = OngoingAdapter(dummyData, false)

        vpOngoingStack.adapter = OngoingAdapter(dummyData, true)
        vpOngoingStack.offscreenPageLimit = 3
        vpOngoingStack.setPageTransformer(StackPageTransformer())

        // Pagination controls
        val btnPrevPage = findViewById<View>(R.id.btnPrevPage)
        val btnNextPage = findViewById<View>(R.id.btnNextPage)
        val tvPageIndicator = findViewById<TextView>(R.id.tvPageIndicator)
        
        var currentPage = 1
        val maxPage = 3
        
        btnPrevPage?.setOnClickListener {
            it.startClickAnimation()
            if (currentPage > 1) {
                currentPage--
                tvPageIndicator?.text = "Page $currentPage of $maxPage"
                rvOngoingList.adapter = OngoingAdapter(dummyData.shuffled(), false)
                vpOngoingStack.adapter = OngoingAdapter(dummyData.shuffled(), true)
            }
        }
        
        btnNextPage?.setOnClickListener {
            it.startClickAnimation()
            if (currentPage < maxPage) {
                currentPage++
                tvPageIndicator?.text = "Page $currentPage of $maxPage"
                rvOngoingList.adapter = OngoingAdapter(dummyData.shuffled(), false)
                vpOngoingStack.adapter = OngoingAdapter(dummyData.shuffled(), true)
            }
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
package com.example.recruitment_portal

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import android.content.Intent
import android.widget.Button
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.widget.NestedScrollView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- Typing Animation for Title with Tricolor ---
        val tvMainTitle = findViewById<TextView>(R.id.tvMainTitle)
        val fullText = "Recruitment Portal"
        
        lifecycleScope.launch {
            tvMainTitle.text = ""
            for (i in 1..fullText.length) {
                val currentText = fullText.substring(0, i)
                val spannable = SpannableStringBuilder(currentText)
                
                if (i <= 11) {
                    spannable.setSpan(ForegroundColorSpan(getColor(R.color.flag_saffron)), 0, i, 0)
                } else {
                    spannable.setSpan(ForegroundColorSpan(getColor(R.color.flag_saffron)), 0, 11, 0)
                    if (i > 12) {
                        spannable.setSpan(ForegroundColorSpan(getColor(R.color.flag_green)), 12, i, 0)
                    }
                }
                
                tvMainTitle.text = spannable
                delay(120)
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.jobsRecyclerView)
        val jobs = mutableListOf(
            Job("Software Developer", "Develop and maintain web applications.", "21-30", "IT Department", "01 Aug 2026", "30 Aug 2026"),
            Job("System Administrator", "Manage servers and network infrastructure.", "22-35", "Infrastructure", "05 Aug 2026", "25 Aug 2026"),
            Job("Data Analyst", "Analyze recruitment data and reports.", "20-28", "Analytics", "10 Aug 2026", "28 Aug 2026"),
            Job("HR Specialist", "Manage recruitment processes and interviews.", "24-40", "Human Resources", "15 Aug 2026", "05 Sep 2026"),
            Job("Project Manager", "Oversee government portal projects.", "30-45", "Management", "20 Aug 2026", "15 Sep 2026"),
            Job("Security Officer", "Ensure security of government premises.", "21-35", "Security", "25 Aug 2026", "20 Sep 2026")
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = JobAdapter(jobs)

        // Setup Quick Service cards
        setupQuickService(R.id.cardLatestJobs, "Jobs", R.drawable.ic_work_24) {
            findViewById<NestedScrollView>(R.id.mainScrollView).smoothScrollTo(0, findViewById<View>(R.id.jobsRecyclerView).top)
        }
        setupQuickService(R.id.cardApplyOnline, "Apply", R.drawable.ic_home_24) {
            if (SessionManager.isLoggedIn) {
                val intent = Intent(this, ApplyOnlineActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_bottom, 0)
            } else {
                NotificationHelper.showStackedNotification(findViewById(R.id.notificationStack), "Login required to access portal")
                showLoginDialog()
            }
        }
        setupQuickService(R.id.cardAdmitCard, "Admit", R.drawable.ic_person_24) {
            val intent = Intent(this, AdmitCardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_bottom, 0)
        }
        setupQuickService(R.id.cardResults, "Results", R.drawable.ic_settings_24) {
            val intent = Intent(this, ResultsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_bottom, 0)
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnLogin)
            .setOnClickListener {
                it.startClickAnimation()
                showLoginDialog()
            }

        val bottomNav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            this.vibrate(30)
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_jobs -> true
                R.id.nav_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_bottom, 0)
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_bottom, 0)
                    true
                }
                else -> false
            }
        }

        val mainScrollView = findViewById<NestedScrollView>(R.id.mainScrollView)
        val fabScroll = findViewById<FloatingActionButton>(R.id.fabScroll)
        var isAtBottom = false

        fabScroll.setOnClickListener {
            if (isAtBottom) {
                mainScrollView.smoothScrollTo(0, 0)
            } else {
                mainScrollView.smoothScrollTo(0, mainScrollView.getChildAt(0).height)
            }
        }

        mainScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            val totalContentHeight = v.getChildAt(0).height
            val visibleHeight = v.height
            if (scrollY + visibleHeight >= totalContentHeight - 10) { 
                if (!isAtBottom) {
                    isAtBottom = true
                    fabScroll.setImageResource(R.drawable.ic_chevron_up)
                }
            } else {
                if (isAtBottom) {
                    isAtBottom = false
                    fabScroll.setImageResource(R.drawable.ic_chevron_down)
                }
            }
        })
    }

    private fun setupQuickService(id: Int, label: String, iconRes: Int, onClick: (View) -> Unit) {
        val view = findViewById<View>(id)
        view.findViewById<TextView>(R.id.tvLabel).text = label
        view.findViewById<android.widget.ImageView>(R.id.ivIcon).setImageResource(iconRes)
        view.setOnClickListener {
            it.startClickAnimation()
            onClick(it)
        }
    }

    private fun showLoginDialog() {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_signin, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()

        val etEmail = view.findViewById<android.widget.EditText>(R.id.etEmail)
        val etPassword = view.findViewById<android.widget.EditText>(R.id.etPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if (email == "student@gmail.com" && password == "1234") {
                SessionManager.isLoggedIn = true
                NotificationHelper.showStackedNotification(findViewById(R.id.notificationStack), "Login Successful!")
                dialog.dismiss()
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        }

        val signIn = view.findViewById<LinearLayout>(R.id.layoutSignIn)
        val signup = view.findViewById<LinearLayout>(R.id.layoutSignup)
        val forgot = view.findViewById<LinearLayout>(R.id.layoutForgot)

        view.findViewById<TextView>(R.id.tvGoSignup).setOnClickListener {
            android.transition.TransitionManager.beginDelayedTransition(view as android.view.ViewGroup)
            signIn.visibility = View.GONE
            signup.visibility = View.VISIBLE
        }
        view.findViewById<TextView>(R.id.tvBackLogin).setOnClickListener {
            android.transition.TransitionManager.beginDelayedTransition(view as android.view.ViewGroup)
            signup.visibility = View.GONE
            signIn.visibility = View.VISIBLE
        }
        view.findViewById<TextView>(R.id.tvForgot).setOnClickListener {
            android.transition.TransitionManager.beginDelayedTransition(view as android.view.ViewGroup)
            signIn.visibility = View.GONE
            forgot.visibility = View.VISIBLE
        }
        view.findViewById<TextView>(R.id.tvBackLogin2).setOnClickListener {
            android.transition.TransitionManager.beginDelayedTransition(view as android.view.ViewGroup)
            forgot.visibility = View.GONE
            signIn.visibility = View.VISIBLE
        }
        view.findViewById<TextView>(R.id.btnClose).setOnClickListener {
            it.startClickAnimation()
            dialog.dismiss()
        }
    }
}
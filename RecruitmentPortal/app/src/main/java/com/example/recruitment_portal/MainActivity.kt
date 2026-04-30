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

class MainActivity : AppCompatActivity() {

    private lateinit var bannerViewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bannerViewPager = findViewById(R.id.bannerViewPager)

        val banners = listOf(
            "Government Recruitment 2026",
            "Apply Online Easily",
            "Track Your Application Status"
        )
        val recyclerView = findViewById<RecyclerView>(R.id.jobsRecyclerView)

        val jobs = mutableListOf(
            Job(
                "Software Developer",
                "Develop and maintain web applications.",
                "21-30",
                "IT Department",
                "01 Aug 2026",
                "30 Aug 2026"
            ),
            Job(
                "System Administrator",
                "Manage servers and network infrastructure.",
                "22-35",
                "Infrastructure",
                "05 Aug 2026",
                "25 Aug 2026"
            ),
            Job(
                "Data Analyst",
                "Analyze recruitment data and reports.",
                "20-28",
                "Analytics",
                "10 Aug 2026",
                "28 Aug 2026"
            ),
            Job(
                "HR Specialist",
                "Manage recruitment processes and interviews.",
                "24-40",
                "Human Resources",
                "15 Aug 2026",
                "05 Sep 2026"
            ),
            Job(
                "Project Manager",
                "Oversee government portal projects.",
                "30-45",
                "Management",
                "20 Aug 2026",
                "15 Sep 2026"
            ),
            Job(
                "Security Officer",
                "Ensure security of government premises.",
                "21-35",
                "Security",
                "25 Aug 2026",
                "20 Sep 2026"
            )
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = JobAdapter(jobs)

        bannerViewPager.adapter = BannerAdapter(banners)

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnLogin)
            .setOnClickListener {

                val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
                val view = layoutInflater.inflate(R.layout.dialog_signin, null)

                dialog.setContentView(view)
                dialog.setCancelable(true)
                dialog.setCanceledOnTouchOutside(true)
                dialog.show()

                // Layouts
                val signIn = view.findViewById<LinearLayout>(R.id.layoutSignIn)
                val signup = view.findViewById<LinearLayout>(R.id.layoutSignup)
                val forgot = view.findViewById<LinearLayout>(R.id.layoutForgot)

                // Navigation
                view.findViewById<TextView>(R.id.tvGoSignup).setOnClickListener {
                    signIn.visibility = View.GONE
                    signup.visibility = View.VISIBLE
                }

                view.findViewById<TextView>(R.id.tvBackLogin).setOnClickListener {
                    signup.visibility = View.GONE
                    signIn.visibility = View.VISIBLE
                }

                view.findViewById<TextView>(R.id.tvForgot).setOnClickListener {
                    signIn.visibility = View.GONE
                    forgot.visibility = View.VISIBLE
                }

                view.findViewById<TextView>(R.id.tvBackLogin2).setOnClickListener {
                    forgot.visibility = View.GONE
                    signIn.visibility = View.VISIBLE
                }

                // ✅ CLOSE BUTTON (IMPORTANT)
                view.findViewById<TextView>(R.id.btnClose).setOnClickListener {
                    dialog.dismiss()
                }
            }

        val bottomNav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)

        bottomNav.setOnItemSelectedListener { item ->

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

        // --- Auto Scroll FAB Logic ---
        val mainScrollView = findViewById<NestedScrollView>(R.id.mainScrollView)
        val fabScroll = findViewById<FloatingActionButton>(R.id.fabScroll)
        var isAtBottom = false

        fabScroll.setOnClickListener {
            if (isAtBottom) {
                // Scroll to top
                mainScrollView.smoothScrollTo(0, 0)
            } else {
                // Scroll to bottom
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
}
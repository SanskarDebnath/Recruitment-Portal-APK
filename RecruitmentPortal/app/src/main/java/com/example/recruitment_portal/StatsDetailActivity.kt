package com.example.recruitment_portal

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator

class StatsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats_detail)

        setupAnimations()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                closePage(null)
            }
        })
    }

    private fun setupAnimations() {
        // Animate circular progress and text
        val circularProgress = findViewById<CircularProgressIndicator>(R.id.detailCircularProgress)
        val tvPercent = findViewById<TextView>(R.id.tvProgressPercent)
        
        animateProgress(0, 65) { value ->
            circularProgress.progress = value
            tvPercent.text = "$value%"
        }

        // Animate linear bars
        val progressApplied = findViewById<LinearProgressIndicator>(R.id.progressApplied)
        val progressShortlisted = findViewById<LinearProgressIndicator>(R.id.progressShortlisted)
        val progressRejected = findViewById<LinearProgressIndicator>(R.id.progressRejected)

        animateProgress(0, 85) { progressApplied.progress = it }
        animateProgress(0, 45) { progressShortlisted.progress = it }
        animateProgress(0, 20) { progressRejected.progress = it }
    }

    private fun animateProgress(from: Int, to: Int, onUpdate: (Int) -> Unit) {
        val animator = ValueAnimator.ofInt(from, to)
        animator.duration = 1200
        animator.startDelay = 300
        animator.addUpdateListener { onUpdate(it.animatedValue as Int) }
        animator.start()
    }

    fun closePage(view: View?) {
        vibrate(30)
        finish()
        overridePendingTransition(0, R.anim.slide_out_bottom)
    }
}

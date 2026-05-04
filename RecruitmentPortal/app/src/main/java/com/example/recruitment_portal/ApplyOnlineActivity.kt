package com.example.recruitment_portal

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent

class ApplyOnlineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_online)

        val btnApply1 = findViewById<View>(R.id.btnApply1)
        val btnApply2 = findViewById<View>(R.id.btnApply2)

        val applyListener = View.OnClickListener {
            it.startClickAnimation()
            if (SessionManager.isLoggedIn) {
                val intent = Intent(this, ApplyWizardActivity::class.java)
                startActivity(intent)
            } else {
                NotificationHelper.showStackedNotification(
                    findViewById(android.R.id.content), 
                    "Please login to apply for this job"
                )
            }
        }

        btnApply1.setOnClickListener(applyListener)
        btnApply2.setOnClickListener(applyListener)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                closePage(null)
            }
        })
    }

    fun closePage(view: View?) {
        vibrate(30)
        finish()
        overridePendingTransition(0, R.anim.slide_out_bottom)
    }
}
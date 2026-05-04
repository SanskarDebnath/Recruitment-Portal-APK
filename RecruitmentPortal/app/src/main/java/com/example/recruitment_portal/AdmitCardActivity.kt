package com.example.recruitment_portal

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class AdmitCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admit_card)

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
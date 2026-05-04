package com.example.recruitment_portal

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import android.widget.TextView

class ApplyWizardActivity : AppCompatActivity() {

    private var currentStep = 1
    private val totalSteps = 7

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_wizard)

        setupStepper()
        updateUI()

        findViewById<Button>(R.id.btnNext).setOnClickListener {
            if (currentStep < totalSteps) {
                currentStep++
                updateUI()
            } else {
                Toast.makeText(this, "Application Submitted Successfully!", Toast.LENGTH_LONG).show()
                finish()
            }
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            if (currentStep > 1) {
                currentStep--
                updateUI()
            }
        }
    }

    private fun setupStepper() {
        setStep(R.id.step1, "Personal", R.drawable.ic_person_24)
        setStep(R.id.step2, "Address", R.drawable.ic_home_24)
        setStep(R.id.step3, "Education", R.drawable.ic_info_24)
        setStep(R.id.step4, "Experience", R.drawable.ic_work_24)
        setStep(R.id.step5, "Documents", R.drawable.ic_notifications_24)
        setStep(R.id.step6, "City", R.drawable.ic_search)
        setStep(R.id.step7, "Preview", R.drawable.ic_settings_24)
    }

    private fun setStep(id: Int, label: String, iconRes: Int) {
        val view = findViewById<View>(id)
        view.findViewById<TextView>(R.id.tvStepLabel).text = label
        view.findViewById<android.widget.ImageView>(R.id.ivStepIcon).setImageResource(iconRes)
    }

    private fun updateUI() {
        val layoutStep1 = findViewById<LinearLayout>(R.id.layoutStep1)
        val placeholder = findViewById<TextView>(R.id.tvStepPlaceholder)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnNext = findViewById<Button>(R.id.btnNext)

        layoutStep1.visibility = if (currentStep == 1) View.VISIBLE else View.GONE
        placeholder.visibility = if (currentStep > 1) View.VISIBLE else View.GONE
        if (currentStep > 1) {
            placeholder.text = "Content for Step $currentStep"
        }

        btnBack.visibility = if (currentStep == 1) View.GONE else View.VISIBLE
        btnNext.text = if (currentStep == totalSteps) "Submit" else "Next"

        // Update stepper dots color
        val steps = arrayOf(R.id.step1, R.id.step2, R.id.step3, R.id.step4, R.id.step5, R.id.step6, R.id.step7)
        steps.forEachIndexed { index, id ->
            val view = findViewById<View>(id)
            val dot = view.findViewById<View>(R.id.vStepDot)
            if (index + 1 <= currentStep) {
                dot.backgroundTintList = android.content.res.ColorStateList.valueOf(
                    if (SessionManager.isLoggedIn) getColor(R.color.primary_green) else getColor(R.color.primary_orange)
                )
            } else {
                dot.backgroundTintList = android.content.res.ColorStateList.valueOf(getColor(R.color.text_secondary))
            }
        }
    }

    fun closeWizard(view: View?) {
        vibrate(30)
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.slide_out_bottom)
    }
}
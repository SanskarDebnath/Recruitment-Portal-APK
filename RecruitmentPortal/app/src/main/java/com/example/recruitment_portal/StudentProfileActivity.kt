package com.example.recruitment_portal

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class StudentProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_profile)

        setupFields("Personal Information")
        setupTabs()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                closePage(null)
            }
        })
    }

    private fun setupTabs() {
        val btnPersonal = findViewById<Button>(R.id.btnSecPersonal)
        val btnAddress = findViewById<Button>(R.id.btnSecAddress)
        val btnQualification = findViewById<Button>(R.id.btnSecQualification)
        val btnExperience = findViewById<Button>(R.id.btnSecExperience)

        val buttons = listOf(btnPersonal, btnAddress, btnQualification, btnExperience)

        btnPersonal.setOnClickListener {
            updateTabStyle(btnPersonal, buttons)
            setupFields("Personal Information")
        }

        btnAddress.setOnClickListener {
            updateTabStyle(btnAddress, buttons)
            setupAddressFields()
        }

        btnQualification.setOnClickListener {
            updateTabStyle(btnQualification, buttons)
            setupQualificationFields()
        }

        btnExperience.setOnClickListener {
            updateTabStyle(btnExperience, buttons)
            setupExperienceFields()
        }
    }

    private fun updateTabStyle(selected: Button, all: List<Button>) {
        selected.startClickAnimation()
        all.forEach { it.setTextColor(getColor(R.color.text_secondary)) }
//        selected.setTextColor(getThemeColor(com.google.android.material.R.attr.colorPrimary))
    }

    private fun setupFields(sectionTitle: String) {
        findViewById<TextView>(R.id.tvSectionTitle).text = sectionTitle
        setField(R.id.fieldFather, "FATHER NAME", "Samir Debnath")
        setField(R.id.fieldMother, "MOTHER NAME", "Shibani Debnath")
        setField(R.id.fieldGuardian, "GUARDIAN NAME", "Samir Debnath")
        setField(R.id.fieldMarital, "MARITAL STATUS", "UNMARRIED")
        setField(R.id.fieldGender, "GENDER", "Male")
        setField(R.id.fieldDOB, "DATE OF BIRTH", "16 Oct 2000")
    }

    private fun setupAddressFields() {
        findViewById<TextView>(R.id.tvSectionTitle).text = "Address Details"
        setField(R.id.fieldFather, "PRESENT ADDRESS", "Agartala, West Tripura")
        setField(R.id.fieldMother, "PIN CODE", "799001")
        setField(R.id.fieldGuardian, "STATE", "Tripura")
        setField(R.id.fieldMarital, "DISTRICT", "West Tripura")
        setField(R.id.fieldGender, "COUNTRY", "India")
        setField(R.id.fieldDOB, "LANDMARK", "Near City Center")
    }

    private fun setupQualificationFields() {
        findViewById<TextView>(R.id.tvSectionTitle).text = "Educational Background"
        setField(R.id.fieldFather, "HIGHEST DEGREE", "B.Tech (CSE)")
        setField(R.id.fieldMother, "UNIVERSITY", "Heritage Institute")
        setField(R.id.fieldGuardian, "YEAR OF PASSING", "2024")
        setField(R.id.fieldMarital, "PERCENTAGE", "89%")
        setField(R.id.fieldGender, "BOARD", "CBSE")
        setField(R.id.fieldDOB, "SPECIALIZATION", "Full Stack Dev")
    }

    private fun setupExperienceFields() {
        findViewById<TextView>(R.id.tvSectionTitle).text = "Work Experience"
        setField(R.id.fieldFather, "ORGANIZATION", "TCS")
        setField(R.id.fieldMother, "ROLE", "System Engineer")
        setField(R.id.fieldGuardian, "DURATION", "2 Years")
        setField(R.id.fieldMarital, "LOCATION", "Bangalore")
        setField(R.id.fieldGender, "NOTICE PERIOD", "30 Days")
        setField(R.id.fieldDOB, "PREV CTC", "12 LPA")
    }

    private fun setField(id: Int, label: String, value: String) {
        val field = findViewById<ViewGroup>(id)
        field.findViewById<TextView>(R.id.tvFieldLabel).text = label
        field.findViewById<TextView>(R.id.tvFieldValue).text = value
    }

    fun closePage(view: View?) {
        vibrate(30)
        finish()
        overridePendingTransition(0, R.anim.slide_out_bottom)
    }
}
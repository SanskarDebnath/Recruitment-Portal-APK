package com.example.recruitment_portal

data class Job(
    val title: String,
    val description: String,
    val age: String,
    val department: String,
    val startDate: String,
    val endDate: String,
    var isExpanded: Boolean = false
)
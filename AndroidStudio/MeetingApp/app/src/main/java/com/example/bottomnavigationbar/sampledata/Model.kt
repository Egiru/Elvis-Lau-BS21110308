package com.example.bottomnavigationbar.sampledata

import android.R

data class Detail(
    val description: String,
    val platform: String,
    val url: String,
    val status: String,
    val lastEvent: String,
    val created: String,
    val duration: String,
    val attendees: List<String>
)

data class BotDetail(
    val name: String,
    val id: String,
    val status: String,
    val attendees: List<String>
)

data class EmployeeDetail(
    val employerId: String,
    val email: String,
    val phoneNo: String,
    val gender: String,
    val address: String,
    val workingTime: String,
    val nric: String,
    val position: String
)

data class AryanChat(
    val text: String,
    val isUser: Boolean // true = user, false = Aryan
)


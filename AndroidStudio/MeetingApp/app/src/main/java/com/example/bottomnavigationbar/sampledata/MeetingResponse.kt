package com.example.bottomnavigationbar.sampledata

import com.example.bottomnavigationbar.R

data class MeetingResponse(
    val success: Boolean,
    val message: String,
    val data: List<Meeting>
)

data class MeetingDetail(
    val message: String,
    val data: Meeting
)

data class Meeting(
    val id: Int,
    val created_by: Int,
    val title: String?,
    val url: String?,
    val start_time: String?,
    val description: String?,
    val created_at: String?,
    val updated_at: String?
)

data class MeetingItem(
    val id: Int,
    val title: String,
    val iconResId: Int = R.drawable.google_meet
)
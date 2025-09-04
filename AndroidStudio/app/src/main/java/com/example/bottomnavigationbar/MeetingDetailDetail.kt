package com.example.bottomnavigationbar

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.bottomnavigationbar.network.RetrofitClient
import kotlinx.coroutines.launch

class MeetingDetailDetail : Fragment() {

    private var meetingId: Int = 0

    companion object {
        fun newInstance(meetingId: Int) = MeetingDetailDetail().apply {
            arguments = Bundle().apply {
                putInt("MEETING_ID", meetingId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get meetingId from arguments
        meetingId = arguments?.getInt("MEETING_ID", 0) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_meeting_detail_detail, container, false)
        val containerLayout = view.findViewById<LinearLayout>(R.id.detailsContainer)

        // Fetch meeting details by ID
        fetchMeetingDetail(containerLayout)

        return view
    }

    private fun fetchMeetingDetail(containerLayout: LinearLayout) {
        lifecycleScope.launch {
            try {val response = RetrofitClient.instance.getMeetingById(meetingId)

                if (response.isSuccessful) {
                    val meetingDetail = response.body()  // This is MeetingDetail
                    val meeting = meetingDetail?.data    // The actual Meeting object

                    if (meeting != null) {
                        val detailsMap = listOf(
                            "ID" to meeting.id,
                            "Title" to (meeting.title ?: "N/A"),
                            "URL" to (meeting.url ?: "N/A"),
                            "Start Time" to (meeting.start_time ?: "N/A"),
                            "Description" to (meeting.description ?: "N/A"),
                            "Created By" to meeting.created_by,
                            "Created At" to (meeting.created_at ?: "N/A"),
                            "Updated At" to (meeting.updated_at ?: "N/A")
                        )

                        containerLayout.removeAllViews()

                        for ((label, value) in detailsMap) {
                            val row = TextView(requireContext()).apply {
                                text = "$label: $value"
                                textSize = 16f
                                setTextColor(Color.BLACK)
                                setPadding(0, 8, 0, 8)
                            }
                            containerLayout.addView(row)
                        }
                    } else {
                        Log.e("API_ERROR", "Meeting data is null")
                    }
                } else {
                    Log.e("API_ERROR", "Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Exception: ${e.message}")
            }
        }
    }
}

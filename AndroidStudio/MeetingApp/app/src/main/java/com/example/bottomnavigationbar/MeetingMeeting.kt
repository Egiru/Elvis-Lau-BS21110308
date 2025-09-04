package com.example.bottomnavigationbar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.bottomnavigationbar.databinding.FragmentContainerMeetingBinding
import com.example.bottomnavigationbar.network.RetrofitClient
import kotlinx.coroutines.launch

data class MeetingItem(val id: Int, val title: String, val iconResId: Int)

class MeetingMeeting : Fragment() {

    private var _binding: FragmentContainerMeetingBinding? = null
    private val binding get() = _binding!!

    private val meetings = mutableListOf<MeetingItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContainerMeetingBinding.inflate(inflater, container, false)
        val root = binding.root

        // Call API
        MeetingListAPI()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun MeetingListAPI() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getMeetings(10, 1)
                if (response.isSuccessful) {
                    val apiMeetings = response.body()?.data
                    if (!apiMeetings.isNullOrEmpty()) {
                        // Clear old list
                        meetings.clear()

                        // Map API response to your MeetingItem
                        apiMeetings.forEach {
                            meetings.add(
                                MeetingItem(
                                    id = it.id,  // Now this exists
                                    title = it.title ?: "Untitled Meeting",
                                    iconResId = R.drawable.google_meet
                                )
                            )
                        }

                        // Update UI
                        displayMeetings()
                    }
                } else {
                    Log.e("API_ERROR", "Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Exception: ${e.message}")
            }
        }
    }


    private fun displayMeetings() {
        val meetingContainer = binding.containerMeetings
        val itemInflater = LayoutInflater.from(requireContext())

        meetingContainer.removeAllViews() // clear before re-adding

        meetings.forEach { meeting ->
            val itemView = itemInflater.inflate(R.layout.item_meeting, meetingContainer, false)
            val tvTitle = itemView.findViewById<TextView>(R.id.title)
            val ivIcon = itemView.findViewById<ImageView>(R.id.icon)
            val itemContainer = itemView.findViewById<View>(R.id.item_meeting)

            tvTitle.text = meeting.title
            ivIcon.setImageResource(meeting.iconResId)

            // Open details activity on click
            itemContainer.setOnClickListener {
                val intent = Intent(requireContext(), MeetingDetailActivity::class.java)
                intent.putExtra("MEETING_ID", meeting.id)
                intent.putExtra("MEETING_TITLE", meeting.title)
                startActivity(intent)
            }


            meetingContainer.addView(itemView)
        }
    }

}

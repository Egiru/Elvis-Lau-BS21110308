package com.example.bottomnavigationbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.bottomnavigationbar.sampledata.botDetails

class MeetingDetailBot : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_meeting_detail_bot, container, false)

        val bot = botDetails[0] // First sample item

        // Fill your text views
        view.findViewById<TextView>(R.id.tvBotName).text = bot.name
        view.findViewById<TextView>(R.id.tvBotId).text = bot.id
        view.findViewById<TextView>(R.id.tvBotStatus).text = bot.status
        view.findViewById<TextView>(R.id.tvBotAttendees).text =
            bot.attendees.joinToString("\n") { "- $it" }

        return view
    }

    companion object {
        fun newInstance(meetingId: Int) = MeetingDetailBot().apply {
            arguments = Bundle().apply {
                putInt("MEETING_ID", meetingId)
            }
        }
    }
}
package com.example.bottomnavigationbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bottomnavigationbar.databinding.FragmentMeetingDetailTranscriptionBinding

class MeetingDetailTranscription : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_meeting_detail_transcription, container, false)
    }

    companion object {
        fun newInstance(meetingId: Int) = MeetingDetailTranscription().apply {
            arguments = Bundle().apply {
                putInt("MEETING_ID", meetingId)
            }
        }
    }
}
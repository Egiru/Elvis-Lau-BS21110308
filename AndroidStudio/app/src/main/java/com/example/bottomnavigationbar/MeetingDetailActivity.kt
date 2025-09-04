package com.example.bottomnavigationbar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bottomnavigationbar.databinding.ActivityMeetingDetailBinding

class MeetingDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMeetingDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        // Inflate the layout with ViewBinding
        binding = ActivityMeetingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Close button
        binding.closeMeetingDetailButton.setOnClickListener {
            finish()
        }

        val meetingId = intent.getIntExtra("MEETING_ID", 0) // default 0
        val meetingTitle = intent.getStringExtra("MEETING_TITLE")
        val detailFragment = MeetingDetailDetail().apply {
            arguments = Bundle().apply {
                putInt("MEETING_ID", meetingId)
            }
        }
        replaceFragment(detailFragment)

        // Set Meeting Title
        binding.meetingTitle.text = meetingTitle ?: "Untitled Meeting"

        binding.bottomNavigationView2.setOnItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.detail -> MeetingDetailDetail.newInstance(meetingId)
                R.id.transcription -> MeetingDetailTranscription.newInstance(meetingId)
                R.id.bot -> MeetingDetailBot.newInstance(meetingId)
                R.id.testing -> MeetingDetailTest.newInstance(meetingId)
                R.id.session -> MeetingDetailSession.newInstance(meetingId)
                else -> null
            }

            fragment?.let { replaceFragment(it) }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.meeting_detail_frame, fragment)
            .commit()
    }
}



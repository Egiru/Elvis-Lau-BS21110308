package com.example.bottomnavigationbar

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MeetingPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MeetingMeeting()
            1 -> MeetingSchedule()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}

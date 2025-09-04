package com.example.bottomnavigationbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bottomnavigationbar.databinding.FragmentMainMeetingBinding
import com.google.android.material.tabs.TabLayoutMediator

class Meeting : Fragment() {

    private var _binding: FragmentMainMeetingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainMeetingBinding.inflate(inflater, container, false)

        val adapter = MeetingPagerAdapter(this)
        binding.homeViewPager.adapter = adapter

        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager) { tab, position ->
            tab.text = if (position == 0) "Meeting" else "Schedule"
        }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

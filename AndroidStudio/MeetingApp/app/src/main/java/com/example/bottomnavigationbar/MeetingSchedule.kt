package com.example.bottomnavigationbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class MeetingSchedule : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var tvSelectedDate: TextView

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_container_schedule, container, false)

        calendarView = view.findViewById(R.id.calendarView)
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate)

        // Set initial date to today
        tvSelectedDate.text = "Today: ${dateFormat.format(Date())}"

        // Update when user selects a date
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            tvSelectedDate.text = "Selected: ${dateFormat.format(cal.time)}"
        }

        return view
    }
}

package com.example.bottomnavigationbar

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

data class NotificationItem(
    val title: String,
    val description: String,
    val isNew: Boolean
)

class Notification : Fragment() {

    private val sampleData = listOf(
        NotificationItem("New Feature Available!", "Check out the latest update with cool new features.", true),
        NotificationItem("System Maintenance", "Scheduled maintenance is planned for tomorrow night.", false),
        NotificationItem("Welcome!", "Thanks for installing our app!", true),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main_notification, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val container = view.findViewById<LinearLayout>(R.id.containerNotifications)
        val inflater = LayoutInflater.from(requireContext())

        sampleData.forEach { item ->
            val itemView = inflater.inflate(R.layout.item_notification, container, false)

            val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
            val tvDesc  = itemView.findViewById<TextView>(R.id.tvDesc)

            tvTitle.text = item.title
            tvDesc.text  = item.description
            tvTitle.setTypeface(null, if (item.isNew) Typeface.BOLD else Typeface.NORMAL)

            container.addView(itemView)
        }
    }
}

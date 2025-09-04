package com.example.bottomnavigationbar

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavigationbar.databinding.ActivityProfileBinding
import com.example.bottomnavigationbar.sampledata.employeeDetails

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Close button
        binding.closeProfileButton.setOnClickListener {
            finish()
        }

        // Example: Get data passed from MainActivity
        val username = intent.getStringExtra("USERNAME")
        binding.profileName.text = username ?: "Guest"

        val employee = employeeDetails[0]

        val detailsMap = listOf(
            "Employer ID" to employee.employerId,
            "Email" to employee.email,
            "Phone No" to employee.phoneNo,
            "Gender" to employee.gender,
            "Address" to employee.address,
            "Working Time" to employee.workingTime,
            "NRIC" to employee.nric,
            "Position" to employee.position,
        )

        // Find your LinearLayout in Activity's layout
        val containerLayout = findViewById<LinearLayout>(R.id.containerProfile)

        for ((label, value) in detailsMap) {
            val rowView = layoutInflater.inflate(R.layout.item_profile, containerLayout, false)
            rowView.findViewById<TextView>(R.id.detailLabel).text = label
            rowView.findViewById<TextView>(R.id.detailValue).text = value
            containerLayout.addView(rowView)
        }

    }
}

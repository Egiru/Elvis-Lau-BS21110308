package com.example.bottomnavigationbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavigationbar.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Close button
        binding.closeSettingButton.setOnClickListener {
            finish()
        }

        // Example: Get data passed from MainActivity
        val username = intent.getStringExtra("SETTING")
        binding.tvSettingName.text = username ?: "Default Setting"
    }
}

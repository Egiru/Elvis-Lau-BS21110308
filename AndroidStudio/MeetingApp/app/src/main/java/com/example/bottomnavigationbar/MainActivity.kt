package com.example.bottomnavigationbar  // <-- match your package!

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.bottomnavigationbar.databinding.ActivityMainBinding
import androidx.core.view.GravityCompat

import android.util.Log;
import android.view.View
import android.graphics.Rect

import androidx.lifecycle.lifecycleScope
import com.example.bottomnavigationbar.network.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    @SuppressLint("UnsafeIntentLaunch")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        testApi()  // call API here

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val headerView = binding.navView.getHeaderView(0) // 0 = first header
        val headerContainer = headerView.findViewById<LinearLayout>(R.id.nav_header_container)
        val rootView = findViewById<View>(android.R.id.content)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Open drawer when menu button clicked
        findViewById<ImageView>(R.id.btn_profile).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        replaceFragment(Meeting())

        headerContainer.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("USERNAME", "Isak")
            startActivity(intent)
            drawerLayout.closeDrawers()
        }


        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra("USERNAME", "Username")
                    startActivity(intent)
                }
                R.id.nav_settings -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    intent.putExtra("SETTING", "Setting")
                    startActivity(intent)
                }
                R.id.nav_logout -> {
                    finishAffinity()
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)

            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                bottomNav.visibility = View.GONE
            } else {
                bottomNav.visibility = View.VISIBLE
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.meeting -> replaceFragment(Meeting())
                R.id.studio -> replaceFragment(Studio())
                R.id.aryan -> replaceFragment(Aryan())
                R.id.dashboard -> replaceFragment(Dashboard())
                R.id.notification -> replaceFragment(Notification())
            }
            true
        }
    }


    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun testApi() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getMeetings(10, 1)
                if (response.isSuccessful) {
                    val meetings = response.body()?.data
                    meetings?.forEach {
                        Log.d(
                            "API_SUCCESS",
                            "ID: ${it.id}, CreatedBy: ${it.created_by}, Title: ${it.title}, " +
                                    "URL: ${it.url}, StartTime: ${it.start_time}, " +
                                    "Description: ${it.description}, " +
                                    "CreatedAt: ${it.created_at}, UpdatedAt: ${it.updated_at}"
                        )
                    }
                } else {
                    Log.e("API_ERROR", "Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Exception: ${e.message}")
            }
        }
    }

    fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    fun showBottomNav() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}

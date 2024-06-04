package com.hav.group3.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import com.hav.group3.R

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.home)

        val historyButton = findViewById<View>(R.id.history_button)
        historyButton.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        val detectButton = findViewById<View>(R.id.detection_button)
        detectButton.setOnClickListener {
            startActivity(Intent(this, DetectActivity::class.java))
        }

        val feedBack = findViewById<View>(R.id.feed_back)
        feedBack.setOnClickListener {
            startActivity(Intent(this, FeedBackActivity::class.java))
        }

        val logoutButton = findViewById<View>(R.id.logout_button)
        logoutButton.setOnClickListener {

            val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun signListClicked(view: View?) {
        val intent = Intent(this, SignListActivity::class.java)
        startActivity(intent)
    }
}
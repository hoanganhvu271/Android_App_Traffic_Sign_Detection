package com.hav.group3.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import com.hav.group3.R

class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.splash_screen)
        Handler().postDelayed({
            val token = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE).getString("token", "")
//            Log.d("Vu", token.toString());
            if (token != "") {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 3000)
    }
}
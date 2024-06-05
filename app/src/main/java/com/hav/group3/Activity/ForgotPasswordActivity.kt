package com.hav.group3.Activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.hav.group3.Api.G3Api
import com.hav.group3.Model.DataResponse
import com.hav.group3.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ForgotPasswordActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.activity_forgot_password)

        val email = findViewById<EditText>(R.id.et_email)
        val button = findViewById<Button>(R.id.sendOtp)
        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener(View.OnClickListener {
            finish()
        });


        button.setOnClickListener {
            sendOtp(email.text.toString());

            val intent = Intent(this, OtpActivity::class.java)
            intent.putExtra("email", email.text.toString())
            startActivity(intent)
        }

    }

    private fun sendOtp(email: String) {
        val BASE_URL = "https://backend-1-rnqj.onrender.com"
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(G3Api::class.java)

        api.sendOtp(email)?.enqueue(object : Callback<DataResponse?> {
            override fun onResponse(call: Call<DataResponse?>, response: Response<DataResponse?>) {
                Log.d("Vu", response.toString())
                if (response.isSuccessful) {
                    Toast.makeText(this@ForgotPasswordActivity, "OTP sent to your email", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ForgotPasswordActivity, "Failed to send OTP", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DataResponse?>, t: Throwable) {
                Toast.makeText(this@ForgotPasswordActivity, "Failed to send OTP", Toast.LENGTH_SHORT).show()
            }
        });


    }
}
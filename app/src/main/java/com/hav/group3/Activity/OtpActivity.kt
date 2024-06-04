package com.hav.group3.Activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
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

class OtpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.activity_otp)

        val email = intent.getStringExtra("email")

        val otp = findViewById<EditText>(R.id.et_otp)

        val button = findViewById<Button>(R.id.btn_otp)

        button.setOnClickListener {
            verifyOtp(email, otp.text.toString())
        }
    }

    private fun verifyOtp(email: String?, otp: String?) {
        // Verify OTP
        val BASE_URL = "https://backend-1-rnqj.onrender.com"
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(G3Api::class.java)


        api.verifyOtp(email, otp)?.enqueue(object : Callback<DataResponse?> {
            override fun onResponse(call: Call<DataResponse?>, response: Response<DataResponse?>) {
                Log.d("Vu", response.body()?.status.toString())
                if (response.isSuccessful && response.body()?.status == 200){
//                    Toast.makeText(this@OtpActivity, "OTP verified", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@OtpActivity, ResetPasswordActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@OtpActivity,"OTP không hợp lệ", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DataResponse?>, t: Throwable) {
//                Log.d("Vu", t.toString())
                Toast.makeText(this@OtpActivity, "Failed to verify OTP", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
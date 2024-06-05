package com.hav.group3.Activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.hav.group3.Api.G3Api
import com.hav.group3.Model.DataResponse
import com.hav.group3.R
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.activity_reset_password)
        val tvPassword = findViewById<EditText>(R.id.et_password)
        val tvConfirmPassword = findViewById<EditText>(R.id.et_confirm_password)
        val email = intent.getStringExtra("email")

        val back = findViewById<View>(R.id.back)
        back.setOnClickListener {
            finish()
        }

        val button = findViewById<View>(R.id.btn_reset_password)
        button.setOnClickListener {
            if (tvPassword != null && tvConfirmPassword != null) {
                if (tvPassword.text.toString() == tvConfirmPassword.text.toString()) {
                        // Call API to reset password
                      resetPassword(email, tvPassword.text.toString(), tvConfirmPassword.text.toString())
                }
                else{
                    Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    private fun resetPassword(email: String?, toString: String, toString1: String) {
        // Call API to reset password
        val BASE_URL = "https://backend-1-rnqj.onrender.com"
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(G3Api::class.java)

        api.changePassword(email, toString, toString1)?.enqueue(object : retrofit2.Callback<DataResponse?> {
            override fun onResponse(call: Call<DataResponse?>, response: Response<DataResponse?>) {
                if (response.isSuccessful && response.body()?.status == 200){
                    Toast.makeText(this@ResetPasswordActivity, "Password reset success", Toast.LENGTH_SHORT).show()

                    intent = Intent(this@ResetPasswordActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(this@ResetPasswordActivity,"Password reset failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DataResponse?>, t: Throwable) {
                Toast.makeText(this@ResetPasswordActivity,"Password reset failed", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
package com.hav.group3.Activity

import android.content.Intent
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.hav.group3.Api.ApiClient
import com.hav.group3.Model.DataResponse
import com.hav.group3.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Bundle


class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_register);

        val btnRegister = findViewById<View>(R.id.register)
        btnRegister.setOnClickListener {
            val username = findViewById<EditText>(R.id.username).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            val name = findViewById<EditText>(R.id.fullname).text.toString()
            val email = findViewById<EditText>(R.id.email).text.toString()
            val phone = findViewById<EditText>(R.id.phone).text.toString()
            if(username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()){
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val api = ApiClient.createClient( "")
            val call = api.createNewAccount(username, password, name, email, phone)
            ?.enqueue(object : Callback<DataResponse?> {
                override fun onResponse(call: Call<DataResponse?>, response: Response<DataResponse?>) {
                    if (response.isSuccessful) {
                        val dataResponse = response.body()
                        if (dataResponse?.code == 200) {
                            Toast.makeText(this@RegisterActivity, "Register success", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        } else {
                            Toast.makeText(this@RegisterActivity, dataResponse?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<DataResponse?>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Register failed", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
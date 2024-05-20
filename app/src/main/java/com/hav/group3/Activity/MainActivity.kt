package com.hav.group3.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
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


class MainActivity : ComponentActivity() {

    private val BASE_URL = "https://backend-1-rnqj.onrender.com"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.login)
    }

    fun loginButtonClicked(view: View?) {
        val userInput = findViewById<EditText>(R.id.editText_username)
        val passwordInput = findViewById<EditText>(R.id.editText_password)

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(G3Api::class.java)

        api.checkLogin(userInput.text.toString(), passwordInput.text.toString())
            ?.enqueue( object : Callback<DataResponse?> {
                override fun onResponse(call: Call<DataResponse?>, response: Response<DataResponse?>) {
                    if (response.isSuccessful && response.body()?.status == 200){
                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this@MainActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DataResponse?>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Server Error", Toast.LENGTH_SHORT).show()
                }
            })



    }


}

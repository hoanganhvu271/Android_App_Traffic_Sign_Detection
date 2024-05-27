package com.hav.group3.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.hav.group3.Api.ApiClient
import com.hav.group3.Model.DataResponse
import com.hav.group3.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class FeedBackActivity : ComponentActivity() {

    var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        val backButton = findViewById<View>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        val submitButton = findViewById<View>(R.id.submit_button)
        submitButton.setOnClickListener {
            val contentEditText = findViewById<EditText>(R.id.editText)
            val textPart = contentEditText.text.toString()

            if(imageUri != null){
                val inputStream = contentResolver.openInputStream(imageUri!!)
                val requestFile = RequestBody.create(
                    MediaType.parse(contentResolver.getType(imageUri!!)!!),
                    inputStream?.readBytes() ?: ByteArray(0)
                )

                val imagePart = MultipartBody.Part.createFormData("image", "a", requestFile)

                // Call API
                val token = getSharedPreferences("sharedPrefs", MODE_PRIVATE).getString("token", "")
                val api = token?.let { it1 -> ApiClient.createClient(it1) }
                Log.d("Vu", api.toString())
                if (api != null) {
                    api.sendFeedback(imagePart, textPart).enqueue(object : Callback<DataResponse> {
                        override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                            if (response.isSuccessful && response.body()?.code  == 200){
                                Toast.makeText(this@FeedBackActivity, "Gửi thanh công", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this@FeedBackActivity, "Gửi thanh công", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                            Log.e("Vu", t.message, t)
                            Toast.makeText(this@FeedBackActivity, "Loi gui", Toast.LENGTH_SHORT).show()
                        }
                    })
                }

            }

        }

        val uploadButton = findViewById<View>(R.id.upload_button)
        uploadButton.setOnClickListener {
            //upload image
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data

            val imageView = findViewById<ImageView>(R.id.image_view)
            imageView.setImageURI(imageUri)

        }
    }
}
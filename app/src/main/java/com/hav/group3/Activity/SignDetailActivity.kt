package com.hav.group3.Activity

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.hav.group3.R

class SignDetailActivity : ComponentActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.detail)

        val backButton = findViewById<ImageView>(R.id.idSignDetailBack)
        backButton.setOnClickListener {
            finish()
        }

        val intent = intent
        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")
        val imgId = intent.getIntExtra("img_id", -1)
        val audioId = intent.getIntExtra("audio_id", -1)

        val imageView: ImageView = findViewById(R.id.idSignDetailImage)
        val nameTextView: TextView = findViewById(R.id.idSignDetailName)
        val descriptionTextView: TextView = findViewById(R.id.idSignDetailDescription)

        if (imgId != -1) {
            imageView.setImageResource(imgId)
        }
        nameTextView.text = name
        descriptionTextView.text = description

        val button = findViewById<Button>(R.id.playButton)

        mediaPlayer = MediaPlayer.create(this, audioId)
        button.setOnClickListener {
            mediaPlayer?.seekTo(0)
            mediaPlayer?.start()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
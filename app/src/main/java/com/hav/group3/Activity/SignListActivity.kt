package com.hav.group3.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hav.group3.Adapter.SignAdapter
import com.hav.group3.Helper.DatabaseHelper
import com.hav.group3.Model.TrafficSign
import com.hav.group3.R
import kotlin.math.log


class SignListActivity : ComponentActivity() {
    var db: DatabaseHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.signinfo)

        val backButton = findViewById<ImageView>(R.id.idSignBack)
        backButton.setOnClickListener {
            finish()
        }

        db = DatabaseHelper(this);
        val signRecyclerView = findViewById<RecyclerView>(R.id.idSignRV)
        var signDataArrayList = ArrayList<TrafficSign>()
        signDataArrayList = db?.getAllTrafficSign() ?: ArrayList<TrafficSign>()



        val adapter = SignAdapter(signDataArrayList, this, object : SignAdapter.ItemClickListener{
            override fun onItemClick(position: Int) {
                val sign = signDataArrayList[position]
                val intent = Intent(this@SignListActivity, SignDetailActivity::class.java)
                val bundle = Bundle().apply {
                    putString("id", sign.getId())
                    putString("name", sign.getName())
                    putString("description", sign.getDescription())
                    putInt("img_id", sign.getImgId())
                    putInt("audio_id", sign.getAudioId())
                }
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        val layoutManager = GridLayoutManager(this, 4)
        signRecyclerView.layoutManager = layoutManager
        signRecyclerView.adapter = adapter
    }
}
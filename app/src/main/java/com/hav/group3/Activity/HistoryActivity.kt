package com.hav.group3.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hav.group3.Adapter.HistoryAdapter
import com.hav.group3.Api.G3Api
import com.hav.group3.Helper.DatabaseHelper
import com.hav.group3.Model.DataResponse
import com.hav.group3.Model.DetectionHistory
import com.hav.group3.Model.HistoryResponse
import com.hav.group3.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HistoryActivity : ComponentActivity() {
    var db: DatabaseHelper? = null
    private val BASE_URL = "https://backend-1-rnqj.onrender.com"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.historylist)

        val backButton = findViewById<ImageView>(R.id.idHistoryBack)
        backButton.setOnClickListener {
            finish()
        }

        db = DatabaseHelper(this);
        val historyRecyclerView = findViewById<RecyclerView>(R.id.idHistoryRV)
        var historyDataArrayList = ArrayList<DetectionHistory>()

        //Call Api
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(G3Api::class.java)

        var adapter: HistoryAdapter ?= null

        api.getDetectionHistory("B21DCCN343")
            ?.enqueue( object : Callback<HistoryResponse?> {
                override fun onResponse(call: Call<HistoryResponse?>, response: Response<HistoryResponse?>) {
                    Log.d("Vu", response.body()?.data.toString())
                    if (response.isSuccessful && response.body()?.status == 200){
                        historyDataArrayList = response.body()?.data ?: ArrayList<DetectionHistory>()
                        Log.d("Vu", historyDataArrayList.toString())

                        val adapter = HistoryAdapter(historyDataArrayList, this@HistoryActivity, object : HistoryAdapter.ItemClickListener {
                            override fun onItemClick(position: Int) {}
                        }, db!!)

                        historyRecyclerView.layoutManager = LinearLayoutManager(this@HistoryActivity, LinearLayoutManager.VERTICAL, false)
                        historyRecyclerView.adapter = adapter
                    }
                    else {
                        Toast.makeText(this@HistoryActivity, "Nothing to show", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<HistoryResponse?>, t: Throwable) {
                    Toast.makeText(this@HistoryActivity, "Server Error", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
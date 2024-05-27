package com.hav.group3.Activity

import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import com.hav.group3.R
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.IOException
import java.util.Locale

class MapActivity : ComponentActivity() {

    var address: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.activity_map);

        val intent = getIntent();
        val id = intent.getStringExtra("id");
        val time = intent.getStringExtra("time");
        val name = intent.getStringExtra("name");

        val tvTime = findViewById<View>(R.id.tv_time)
        tvTime?.let {
            (it as android.widget.TextView).text = time
        }

        val tvName = findViewById<View>(R.id.tv_name)
        tvName?.let {
            (it as android.widget.TextView).text = name
        }

        val longitude = intent.getStringExtra("longitude")?.toDoubleOrNull()
        val latitude = intent.getStringExtra("latitude")?.toDoubleOrNull()

//        Log.d("Vu", "Longitude: $longitude, Latitude: $latitude")
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val provider = LocationManager.NETWORK_PROVIDER

        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            if(longitude != null && latitude != null){
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                address = addresses?.get(0)?.getAddressLine(0)
                val tvAddress = findViewById<View>(R.id.tv_address)
                tvAddress?.let {
                    (it as android.widget.TextView).text = address
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }


        //Visualize map
        val mapView: MapView = findViewById<MapView>(R.id.map_view)
        Configuration.getInstance().load(
            applicationContext, PreferenceManager.getDefaultSharedPreferences(
                applicationContext
            )
        )
        mapView.setMultiTouchControls(true)
        if(longitude != null && latitude != null){
            val startPoint = GeoPoint(latitude, longitude)
            mapView.controller.setZoom(15)
            mapView.controller.setCenter(startPoint)

            val startMarker = Marker(mapView)
            startMarker.setPosition(startPoint)
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mapView.overlays.add(startMarker)
        }

        val ivBack = findViewById<View>(R.id.iv_back)
        ivBack.setOnClickListener {
            finish()
        }

    }
}
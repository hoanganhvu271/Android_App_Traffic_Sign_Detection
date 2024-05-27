package com.hav.group3.Model

import com.google.gson.annotations.SerializedName

data class DetectionHistory(
    @SerializedName("DetectionId")
    var detectionId: Int,
    @SerializedName("SignId")
    var signId: String,
    @SerializedName("Time")
    var time: String,
    @SerializedName("UserId")
    var userId: String,

    @SerializedName("Longitude")
    var longitude: String,

    @SerializedName("Latitude")
    var latitude: String
)
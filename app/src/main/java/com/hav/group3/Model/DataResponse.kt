package com.hav.group3.Model

data class DataResponse (
    val code: Int,
    val status: Int,
    val message : String
)
data class HistoryResponse(
    val code: Int,
    val status: Int,
    val message : String,
    val data : ArrayList<DetectionHistory>
)
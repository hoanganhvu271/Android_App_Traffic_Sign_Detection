package com.hav.group3.Model

class DetectionHistory(private var id: Int, private var signId: String, private var time: String) {

    companion object {
        val TABLE_NAME = "detectionhistory"
        val COLUMN_SIGN_ID = "SignID"
        val COLUMN_SIGN_NAME = "SignName"
        val COLUMN_SIGN_DESCRIPTION = "SignDescription"
        val COLUMN_SIGN_IMG_ID = "ImageURL"
        val COLUMN_SIGN_AUDIO_ID = "AudioURL"
        val CREATE_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COLUMN_SIGN_ID + " TEXT PRIMARY KEY,"
                + COLUMN_SIGN_NAME + " TEXT,"
                + COLUMN_SIGN_DESCRIPTION + " TEXT,"
                + COLUMN_SIGN_IMG_ID + " INTEGER,"
                + COLUMN_SIGN_AUDIO_ID + " INTEGER"
                + ")")
    }
}
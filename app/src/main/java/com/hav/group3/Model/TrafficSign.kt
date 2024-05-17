package com.hav.group3.Model

class TrafficSign(private var id: String, private var name: String, private var description: String, private var img_id : Int, private var audio_id : Int) {

    companion object {
        val TABLE_NAME = "trafficsign"
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


    fun getId(): String {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getDescription(): String {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun getImgId(): Int {
        return img_id
    }

    fun setImgId(img_id: Int) {
        this.img_id = img_id
    }

    fun getAudioId(): Int {
        return audio_id
    }
}
package com.hav.group3.Helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hav.group3.Model.TrafficSign
import com.hav.group3.R
import java.io.IOException

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "group3_db"
        private const val DATABASE_VERSION = 8
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TrafficSign.CREATE_TABLE)
        insertDefaultData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TrafficSign.TABLE_NAME)
        onCreate(db)
    }



    fun getTrafficSign(signId: String): TrafficSign? {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM " + TrafficSign.TABLE_NAME + " WHERE " + TrafficSign.COLUMN_SIGN_ID + " = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(signId))

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(TrafficSign.COLUMN_SIGN_ID)
                val nameIndex = cursor.getColumnIndex(TrafficSign.COLUMN_SIGN_NAME)
                val descriptionIndex = cursor.getColumnIndex(TrafficSign.COLUMN_SIGN_DESCRIPTION)
                val imgIdIndex = cursor.getColumnIndex(TrafficSign.COLUMN_SIGN_IMG_ID)
                val audioIdIndex = cursor.getColumnIndex(TrafficSign.COLUMN_SIGN_AUDIO_ID)

                if (idIndex != -1 && nameIndex != -1 && descriptionIndex != -1 && imgIdIndex != -1 && audioIdIndex != -1) {
                    val sign = TrafficSign(
                        cursor.getString(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(descriptionIndex),
                        cursor.getInt(imgIdIndex),
                        cursor.getInt(audioIdIndex)
                    )
                    cursor.close()
                    return sign
                }
            }
        }
        cursor?.close()
        return null
    }

    data class TrafficSignJson(
        val id: String,
        val name: String,
        val description: String,
        val imgId: String,
        val audioId: String
    )

    private fun insertDefaultData(db: SQLiteDatabase) {
        val signsJson = context.assets?.open("signs.json")?.bufferedReader()?.use { it.readText() }
        if (signsJson != null) {
            val gson = Gson()
            val signsType = object : TypeToken<List<TrafficSignJson>>() {}.type
            val signsJson: List<TrafficSignJson> = gson.fromJson(signsJson, signsType)

            for (signJson in signsJson) {
                val imgId = context.resources.getIdentifier(signJson.imgId, "drawable", context.packageName)
                val audioId = context.resources.getIdentifier(signJson.audioId, "raw", context.packageName)
                val sign = TrafficSign(signJson.id, signJson.name, signJson.description, imgId, audioId)
                addTrafficSign(db, sign)
            }
        } else {
            // Handle the case where signsJson is null
        }
    }




    private fun addTrafficSign(db: SQLiteDatabase, sign: TrafficSign) {
        val values = ContentValues()
        values.put(TrafficSign.COLUMN_SIGN_ID, sign.getId())
        values.put(TrafficSign.COLUMN_SIGN_NAME, sign.getName())
        values.put(TrafficSign.COLUMN_SIGN_DESCRIPTION, sign.getDescription())
        values.put(TrafficSign.COLUMN_SIGN_IMG_ID, sign.getImgId())
        values.put(TrafficSign.COLUMN_SIGN_AUDIO_ID, sign.getAudioId())
        db.insert(TrafficSign.TABLE_NAME, null, values)
    }

    fun getAllTrafficSign(): ArrayList<TrafficSign> {
        val signList = ArrayList<TrafficSign>()
        val selectQuery = "SELECT * FROM " + TrafficSign.TABLE_NAME

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val idIndex = cursor.getColumnIndex(TrafficSign.COLUMN_SIGN_ID)
                    val nameIndex = cursor.getColumnIndex(TrafficSign.COLUMN_SIGN_NAME)
                    val descriptionIndex =
                        cursor.getColumnIndex(TrafficSign.COLUMN_SIGN_DESCRIPTION)
                    val imgIdIndex = cursor.getColumnIndex(TrafficSign.COLUMN_SIGN_IMG_ID)
                    val audioIdIndex = cursor.getColumnIndex(TrafficSign.COLUMN_SIGN_AUDIO_ID)

                    if (idIndex != -1 && nameIndex != -1 && descriptionIndex != -1 && imgIdIndex != -1 && audioIdIndex != -1) {
                        val sign = TrafficSign(
                            cursor.getString(idIndex),
                            cursor.getString(nameIndex),
                            cursor.getString(descriptionIndex),
                            cursor.getInt(imgIdIndex),
                            cursor.getInt(audioIdIndex)
                        )
                        signList.add(sign)
                    }
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return signList
    }
}
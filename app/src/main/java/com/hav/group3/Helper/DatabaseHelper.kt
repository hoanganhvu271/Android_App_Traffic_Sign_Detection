package com.hav.group3.Helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hav.group3.Model.TrafficSign
import com.hav.group3.R

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "group3_db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TrafficSign.CREATE_TABLE)
        insertDefaultData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TrafficSign.TABLE_NAME)
        onCreate(db)
    }

    private fun insertDefaultData(db: SQLiteDatabase) {
        val sign1 = TrafficSign(
            "P.124a",
            "Cấm quay đầu",
            "Cấm tất cả các phương tiện bao gồm: xe tải, ô tô, xe máy... quay đầu, với biểu tượng hình chữ U và mũi tên chỉ hướng muốn cấm rẽ.",
            R.drawable.p124a,
            R.raw.p124a
        )
        val sign2 = TrafficSign("", "No Parking", "No Parking", 0, 0)
        val sign3 = TrafficSign("", "No U-Turn", "No U-Turn", 0, 0)
        addTrafficSign(db, sign1)
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
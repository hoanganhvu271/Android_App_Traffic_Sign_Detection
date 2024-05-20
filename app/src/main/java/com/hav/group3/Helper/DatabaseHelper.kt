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

    private fun insertDefaultData(db: SQLiteDatabase) {
        val sign1 = TrafficSign(
            "P.124a",
            "Cấm quay đầu",
            "Cấm tất cả các phương tiện bao gồm: xe tải, ô tô, xe máy... quay đầu, với biểu tượng hình chữ U và mũi tên chỉ hướng muốn cấm rẽ.",
            R.drawable.p124a,
            R.raw.p124a
        )
        val sign2 = TrafficSign("W.208", "Giao nhau với đường ưu tiên", "Biển số W.208 \"Giao nhau với đường ưu tiên\" được đặt Trên đường không ưu tiên, để báo trước sắp đến nơi giao nhau với đường ưu tiên. Trong nội thành, nội thị có thể không đặt biển này.\n" +
                "\n" +
                "Các xe đi trên đường có đặt biển số W.208 phải nhường đường cho xe đi trên đường ưu tiên khi qua nơi giao nhau (trừ các loại xe được quyền ưu tiên theo quy định).", R.drawable.w208, R.raw.w208)
        val sign3 = TrafficSign("W.209", "Giao nhau có tín hiệu đèn", "Biển số W.209 \"Giao nhau có tín hiệu đèn\". Để báo trước nơi giao nhau có điều khiển giao thông bằng tín hiệu đèn trong trường hợp người tham gia giao thông khó quan sát thấy đèn để kịp thời xử lý. ", R.drawable.w209, R.raw.w209)
        val sign4 = TrafficSign("W.210", "Giao nhau có rào chắn", "Biển số W. 210 ” Giao nhau với đường sắt có rào chắn”: Để báo trước sắp đến chỗ giao nhau giữa đường bộ và đường sắt có rào chắn kín hay rào chắn nửa kín và có nhân viên ngành đường sắt điều khiển giao thông, đặt biển số W.", R.drawable.w210, R.raw.w210)
        val sign5 = TrafficSign("W.219", "Cảnh báo dốc xuống nguy hiểm", "Biển số W.219 \"Dốc xuống nguy hiểm\"\n" +
                " Để báo trước sắp tới đoạn đường xuống dốc nguy hiểm phải đặt biển số W.219 \"Dốc xuống nguy hiểm\".\n" +
                " Con số ghi trong biển chỉ độ dốc thực tế tính bằng % làm tròn không có số thập phân Chiều dài của đoạn dốc được chỉ dẫn bằng biển số S.501 \"Phạm vi tác dụng của biển\" đặt bên dưới biển chính.\n" +
                " Những vị trí xuống dốc nguy hiểm là:\n" +
                "- Độ dốc từ 6% trở lên và chiều dài dốc trên 600 m;\n" +
                "- Độ dốc từ 10% trở lên và chiều dài dốc trên 140 m;", R.drawable.w219, R.raw.w219)
        val sign6 = TrafficSign("W.221b", "Đường không bằng phẳng", "Biển báo giao thông báo hiệu đoạn \"đường có sóng mấp mô nhân tạo (humps)\" để hạn chế tốc độ xe chạy (biển được cắm kèm theo biển số 227 \"Hạn chế tốc độ tối đa\"), bắt buộc lái xe phải chạy với tốc độ chậm trước khi qua những điểm cần kiểm soát, kiểm tra", R.drawable.w221b, R.raw.w221b)

        val sign7 = TrafficSign("P.102", "Cấm đi ngược chiều", "Biển số P.102 có tác dụng báo cho người tham gia giao thông biết \"Tất cả các phương tiện giao thông không được đi ngược chiều kể từ sau biển báo.\" Biển báo thường được cắm ở phần lối rẽ giữa dải phân cách của đoạn đường 2 chiều. Biển số P.102 báo đường cấm các loại xe đi vào theo chiều đặt biển, trừ các xe được ưu tiên theo quy định.", R.drawable.p102, R.raw.p102)


        addTrafficSign(db, sign1)
        addTrafficSign(db, sign2)
        addTrafficSign(db, sign3)
        addTrafficSign(db, sign4)
        addTrafficSign(db, sign5)
        addTrafficSign(db, sign6)
        addTrafficSign(db, sign7)
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
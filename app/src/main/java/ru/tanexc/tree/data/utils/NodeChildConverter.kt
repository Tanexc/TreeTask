package ru.tanexc.tree.data.utils

import android.util.Log
import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class NodeChildConverter {

    @TypeConverter
    fun fromList(data: List<Long>): String {
        Log.i("Test", "Converter pdr $data ${Json.encodeToString(data)}")
        return Json.encodeToString(data)
    }

    @TypeConverter
    fun toList(data: String): List<Long> {
        Log.i("Test", "Converter pdr $data  oo ${Json.decodeFromString<List<Long>>(data)}")
        return Json.decodeFromString(data)
    }

}
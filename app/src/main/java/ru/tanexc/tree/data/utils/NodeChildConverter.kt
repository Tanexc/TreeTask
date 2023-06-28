package ru.tanexc.tree.data.utils

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class NodeChildConverter {

    @TypeConverter
    fun fromList(data: List<Long>): String {
        return Json.encodeToString(data)
    }

    @TypeConverter
    fun toList(data: String): List<Long> {
        return Json.decodeFromString(data)
    }

}
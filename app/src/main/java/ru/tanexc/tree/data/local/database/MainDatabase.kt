package ru.tanexc.tree.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.tanexc.tree.data.local.dao.NodeDao
import ru.tanexc.tree.data.local.entity.NodeEntity
import ru.tanexc.tree.data.utils.NodeChildsConverter


@Database(
    entities=[NodeEntity::class],
    exportSchema=false,
    version=1
)
@TypeConverters(
    NodeChildsConverter::class
)
abstract class MainDatabase : RoomDatabase() {
    abstract val nodeDao: NodeDao
}
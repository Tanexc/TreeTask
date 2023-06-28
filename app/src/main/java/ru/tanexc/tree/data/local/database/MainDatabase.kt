package ru.tanexc.tree.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.tanexc.tree.data.local.dao.NodeDao
import ru.tanexc.tree.data.local.dao.SettingsDao
import ru.tanexc.tree.data.local.entity.NodeEntity
import ru.tanexc.tree.data.local.entity.SettingsEntity
import ru.tanexc.tree.data.utils.NodeChildConverter


@Database(
    entities=[NodeEntity::class, SettingsEntity::class],
    exportSchema=false,
    version=1
)
@TypeConverters(
    NodeChildConverter::class
)
abstract class MainDatabase : RoomDatabase() {
    abstract val nodeDao: NodeDao
    abstract val settingsDao: SettingsDao
}
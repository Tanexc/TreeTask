package ru.tanexc.tree.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.tanexc.tree.data.local.entity.SettingsEntity

@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSettings(settings: SettingsEntity)

    @Query("SELECT * FROM settings WHERE id = 0")
    suspend fun getSettings(): SettingsEntity

}
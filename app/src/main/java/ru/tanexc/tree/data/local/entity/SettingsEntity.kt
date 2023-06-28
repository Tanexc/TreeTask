package ru.tanexc.tree.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.tanexc.tree.domain.interfaces.DatabaseEntity
import ru.tanexc.tree.domain.model.Settings

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey
    val id: Long = 0,
    val themeId: Long,
    val useDarkTheme: Boolean,
    val lastNodeId: Long
) : DatabaseEntity {
    override fun asDomain(): Settings = Settings(
        themeId = themeId,
        useDarkTheme = useDarkTheme,
        lastNodeId = lastNodeId
    )
}
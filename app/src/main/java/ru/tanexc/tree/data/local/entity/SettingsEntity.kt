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
    val lastNodeId: Long,
    val useDarkTheme: Boolean
) : DatabaseEntity {
    override fun asDomain(): Settings = Settings(
        themeId = themeId,
        lastNodeId = lastNodeId,
        useDarkTheme = useDarkTheme
    )
}
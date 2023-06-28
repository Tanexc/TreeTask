package ru.tanexc.tree.domain.model

import ru.tanexc.tree.data.local.entity.SettingsEntity
import ru.tanexc.tree.domain.interfaces.Model

data class Settings(
    val themeId: Long,
    val useDarkTheme: Boolean,
    val lastNodeId: Long
): Model {
    override fun asDatabaseEntity(): SettingsEntity = SettingsEntity(
        themeId = themeId,
        useDarkTheme = useDarkTheme,
        lastNodeId = lastNodeId
    )
}
package ru.tanexc.tree.domain.model

import androidx.compose.material3.ColorScheme
import ru.tanexc.tree.core.utils.Theme.Companion.getScheme
import ru.tanexc.tree.data.local.entity.SettingsEntity
import ru.tanexc.tree.domain.interfaces.Model
import ru.tanexc.tree.presentation.theme.getTheme

data class Settings(
    val themeId: Long = 0,
    val useDarkTheme: Boolean = true,
    val lastNodeId: Long = 0
): Model {
    override fun asDatabaseEntity(): SettingsEntity = SettingsEntity(
        themeId = themeId,
        useDarkTheme = useDarkTheme,
        lastNodeId = lastNodeId
    )

    fun getColorScheme(): ColorScheme = getTheme(
        getScheme(themeId.toInt()),
        useDarkTheme
    )
}
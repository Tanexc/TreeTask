package ru.tanexc.tree.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.tanexc.tree.core.utils.State
import ru.tanexc.tree.domain.model.Settings

interface SettingsRepository {

    fun getSettings(): Flow<State<Settings?>>

    fun setSettings(settings: Settings): Flow<State<Settings?>>
}
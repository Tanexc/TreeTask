package ru.tanexc.tree.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanexc.tree.core.utils.State
import ru.tanexc.tree.data.local.dao.SettingsDao
import ru.tanexc.tree.domain.model.Settings
import ru.tanexc.tree.domain.repository.SettingsRepository
import java.lang.Exception
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDao: SettingsDao
): SettingsRepository {
    override fun getSettings(): Flow<State<Settings?>> = flow {
        try {
            emit(State.Success(settingsDao.getSettings().asDomain()))
        } catch (e: Exception) {
            emit(State.Error(message = e.message))
        }
    }

    override fun setSettings(settings: Settings): Flow<State<Settings?>> = flow {
        try {
            settingsDao.setSettings(settings.asDatabaseEntity())
            emit(State.Success(settings))
        } catch (e: Exception) {
            emit(State.Error(message = e.message))
        }
    }
}
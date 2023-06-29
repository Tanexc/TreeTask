package ru.tanexc.tree.presentation.settings.view_model

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import ru.tanexc.tree.core.utils.Theme
import ru.tanexc.tree.domain.model.Settings
import ru.tanexc.tree.domain.use_case.settings_use_cases.SetSettingsUseCase
import ru.tanexc.tree.presentation.theme.getTheme
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setSettingsUseCase: SetSettingsUseCase
): ViewModel() {

    private val _settings: MutableState<Settings> = mutableStateOf(Settings())
    val settings by _settings

    private val _colorScheme: MutableState<ColorScheme?> = mutableStateOf(null)
    val colorScheme by _colorScheme

    private val _useDarkTheme: MutableState<Boolean?> = mutableStateOf(null)
    val useDarkTheme by _useDarkTheme

    private val _themeId: MutableState<Long?> = mutableStateOf(null)
    val themeId by _themeId

    fun updateSettings(settings: Settings) {
        _settings.value = settings
        _themeId.value = settings.themeId
        _useDarkTheme.value = settings.useDarkTheme
        setSettingsUseCase(settings).launchIn(viewModelScope)
    }

    fun changeTheme(theme: Theme) {
        _themeId.value = theme.id.toLong()
        _colorScheme.value = getTheme(theme, useDarkTheme!!)
        updateSettings(settings.copy(themeId=themeId!!))
    }

    fun changeUseDarkTheme() {
        _useDarkTheme.value = !_useDarkTheme.value!!
        _colorScheme.value = getTheme(Theme.getScheme(themeId!!.toInt()), useDarkTheme!!)
        updateSettings(settings.copy(useDarkTheme=useDarkTheme!!))
    }
}
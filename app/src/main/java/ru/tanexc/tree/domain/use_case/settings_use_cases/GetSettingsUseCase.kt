package ru.tanexc.tree.domain.use_case.settings_use_cases

import ru.tanexc.tree.domain.repository.SettingsRepository
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke() = settingsRepository.getSettings()
}
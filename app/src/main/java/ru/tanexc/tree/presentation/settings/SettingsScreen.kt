package ru.tanexc.tree.presentation.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import ru.tanexc.tree.R
import ru.tanexc.tree.core.utils.Theme
import ru.tanexc.tree.domain.model.SelectButtonItem
import ru.tanexc.tree.domain.model.Settings
import ru.tanexc.tree.presentation.components.cards.InfoCard
import ru.tanexc.tree.presentation.components.group.SelectButtonGroup
import ru.tanexc.tree.presentation.settings.view_model.SettingsViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onSettingsChange: (Settings) -> Unit,
    settings: Settings
) {

    val viewModel: SettingsViewModel = hiltViewModel()
    LaunchedEffect(true) {
        viewModel.updateSettings(settings)
    }


    Box(modifier = modifier) {

        LazyColumn(
            modifier = Modifier
                .padding(
                    horizontal = 8.dp,
                    vertical = 0.dp
                )
        ) {


            item {
                val items = listOf(
                    SelectButtonItem(
                        title = stringResource(R.string.app_default),
                        onSelected = {
                            viewModel.changeTheme(Theme.Default())
                            onSettingsChange(viewModel.settings)
                        }
                    ),
                    SelectButtonItem(
                        title = stringResource(R.string.blue),
                        onSelected = {
                            viewModel.changeTheme(Theme.Blue())
                            onSettingsChange(viewModel.settings)
                        }
                    ),
                    SelectButtonItem(
                        title = stringResource(R.string.purple),
                        onSelected = {
                            viewModel.changeTheme(Theme.Purple())
                            onSettingsChange(viewModel.settings)
                        }
                    )
                )
                InfoCard(
                    modifier = Modifier
                        .height(108.dp)
                        .padding(
                            horizontal = 0.dp,
                            vertical = 4.dp
                        ),
                    borderColor = (viewModel.colorScheme ?: settings.getColorScheme()).outline,
                    borderRadius = 16.dp,
                    borderWidth = 1.dp,
                    backgroundColor = (viewModel.colorScheme
                        ?: settings.getColorScheme()).secondaryContainer.copy(
                        0.3f
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            stringResource(R.string.style),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat))
                        )

                        SelectButtonGroup(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            selectedItemIndex = (viewModel.themeId ?: settings.themeId).toInt(),
                            borderColor = (viewModel.colorScheme
                                ?: settings.getColorScheme()).outline,
                            selectedColor = (viewModel.colorScheme
                                ?: settings.getColorScheme()).secondaryContainer,
                            items = items,
                            fontSize = 16.dp
                        )
                    }
                }
            }
            item {
                InfoCard(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(
                            horizontal = 0.dp,
                            vertical = 4.dp
                        ),
                    borderColor = (viewModel.colorScheme ?: settings.getColorScheme()).outline,
                    borderRadius = 16.dp,
                    borderWidth = 1.dp,
                    backgroundColor = (viewModel.colorScheme
                        ?: settings.getColorScheme()).secondaryContainer.copy(
                        0.3f
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .wrapContentHeight()
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .wrapContentHeight()
                                .align(CenterVertically)
                        ) {
                            Text(
                                stringResource(R.string.dark_theme),
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .align(Start)
                                    .padding(8.dp),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat))
                            )
                        }
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Switch(
                                checked = viewModel.useDarkTheme ?: settings.useDarkTheme,
                                onCheckedChange = {
                                    viewModel.changeUseDarkTheme()
                                    onSettingsChange(viewModel.settings)
                                },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.End),
                                thumbContent = {
                                    when (viewModel.useDarkTheme) {
                                        true -> Icon(
                                            Icons.Outlined.Check,
                                            null,
                                            modifier = Modifier.padding(4.dp)
                                        )

                                        else -> {}
                                    }

                                }
                            )
                        }
                    }
                }
            }
        }
    }

}

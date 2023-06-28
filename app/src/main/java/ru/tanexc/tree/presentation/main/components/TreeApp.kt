package ru.tanexc.tree.presentation.main.components

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.popAll
import dev.olshevski.navigation.reimagined.rememberNavController
import ru.tanexc.tree.R
import ru.tanexc.tree.core.utils.Screen
import ru.tanexc.tree.core.utils.Theme
import ru.tanexc.tree.domain.model.Node
import ru.tanexc.tree.domain.model.Settings
import ru.tanexc.tree.presentation.child.ChildScreen
import ru.tanexc.tree.presentation.main.view_model.MainViewModel
import ru.tanexc.tree.presentation.theme.TreeTheme
import ru.tanexc.tree.presentation.theme.getTheme
import ru.tanexc.tree.presentation.node.NodeScreen
import ru.tanexc.tree.presentation.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeApp() {

    val viewModel: MainViewModel = hiltViewModel()
    TreeTheme(
        colorScheme = viewModel.colorScheme?: (viewModel.settings?: Settings()).getColorScheme()
    ) {
        val screens = listOf(
            Screen.Child,
            Screen.Node,
            Screen.Settings
        )

        val startDestination: Screen = Screen.Node
        val navController = rememberNavController(startDestination = startDestination)

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            stringResource(viewModel.currentScreen.label),
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(Font(R.font.montserrat))
                        )
                    }
                )
            },

            bottomBar = {
                BottomAppBar {
                    screens.forEach {
                        NavigationBarItem(
                            selected = it.label == viewModel.currentScreen.label,
                            onClick = {
                                navController.popAll()
                                navController.navigate(it)
                                viewModel.updateCurrentScreen(it)
                            },
                            label = {
                                Text(
                                    stringResource(it.label),
                                    fontFamily = FontFamily(Font(R.font.montserrat))
                                )
                            },
                            icon = {
                                Icon(
                                    when (viewModel.currentScreen.label == it.label) {
                                        true -> it.iconFilled
                                        false -> it.iconOutlined
                                    },
                                    contentDescription = null
                                )
                            },
                            alwaysShowLabel = false
                        )
                    }
                }
            }

        ) { innerPadding ->

            NavBackHandler(navController)
            NavHost(navController) { screen ->
                when (screen) {
                    is Screen.Node -> {
                        NodeScreen(
                            modifier = Modifier.padding(innerPadding),
                            colorScheme = viewModel.colorScheme?: (viewModel.settings?: Settings()).getColorScheme(),
                            node = viewModel.currentNode ?: Node(),
                            parent = viewModel.currentNodeParent
                                ?: Node(label = stringResource(R.string.no_parent)),
                            child = viewModel.currentNodeChild,
                            onNavigateToParent = {
                                viewModel.updateShownNode(it)
                            }
                        )
                    }

                    is Screen.Settings -> {
                        SettingsScreen(
                            modifier = Modifier.padding(innerPadding),
                            onSettingsChange = {
                                Log.i("cum", "${it}")
                                viewModel.setColorScheme(it.getColorScheme())
                                viewModel.changeSettings(it)
                            },
                            colorScheme = viewModel.colorScheme?: (viewModel.settings?: Settings()).getColorScheme(),
                            settings = viewModel.settings?: Settings()
                        )
                    }
                    is Screen.Child -> {
                        ChildScreen(
                            modifier = Modifier.padding(innerPadding),
                            child = viewModel.currentNodeChild,
                            colorScheme = viewModel.colorScheme?: (viewModel.settings?: Settings()).getColorScheme(),
                            shownNode = viewModel.currentNode?: Node(),
                            onNavigateToChild = {
                                viewModel.updateShownNode(it)
                                viewModel.updateCurrentScreen(Screen.Node)
                                navController.popAll()
                                navController.navigate(Screen.Node)
                                viewModel.updateCurrentScreen(Screen.Node)
                            },
                            onChildCreated = {
                                viewModel.createNode(it.description)
                            },
                            onDeleteChild = {
                                viewModel.removeChild(it.id)
                                viewModel.deleteChildNodeBranch(it)
                            }
                        )
                    }
                }
            }
        }
    }
}
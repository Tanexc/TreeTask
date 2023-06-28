package ru.tanexc.tree.presentation.main.components

import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import ru.tanexc.tree.presentation.main.view_model.MainViewModel
import ru.tanexc.tree.presentation.theme.TreeTheme
import ru.tanexc.tree.presentation.theme.getTheme
import ru.tanexc.tree.presentation.tree.NodeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeApp() {

    val viewModel: MainViewModel = hiltViewModel()

    val colorScheme = getTheme(Theme.Default(), isSystemInDarkTheme())

    TreeTheme(
        colorScheme = colorScheme
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
                when(screen) {
                    is Screen.Node -> {
                        NodeScreen(
                            modifier = Modifier.padding(innerPadding),
                            colorScheme = colorScheme,
                            node = viewModel.shownNode?: Node(),
                            parent = viewModel.shownNodeParent ?: Node(label = stringResource(R.string.no_parent)),
                            child = viewModel.shownNodeChild,
                            onNavigateToParent = {
                                viewModel.updateShownNode(it)
                            }
                        )
                    }
                    is Screen.Settings -> {}
                    is Screen.Child -> {}
                }
            }
        }
    }
}
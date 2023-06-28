package ru.tanexc.tree.presentation.main.components

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.popAll
import dev.olshevski.navigation.reimagined.rememberNavController
import ru.tanexc.tree.R
import ru.tanexc.tree.core.utils.Screen
import ru.tanexc.tree.domain.model.Node
import ru.tanexc.tree.domain.model.Settings
import ru.tanexc.tree.presentation.child.ChildScreen
import ru.tanexc.tree.presentation.components.dialogs.ExitDialog
import ru.tanexc.tree.presentation.components.dialogs.TipsDialog
import ru.tanexc.tree.presentation.main.view_model.MainViewModel
import ru.tanexc.tree.presentation.theme.TreeTheme
import ru.tanexc.tree.presentation.node.NodeScreen
import ru.tanexc.tree.presentation.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeApp() {

    val activity: Activity = LocalContext.current as Activity

    val viewModel: MainViewModel = hiltViewModel()
    viewModel.initializeNode(viewModel.settings?.lastNodeId ?: 1L)

    val tipsDialogVisibilityState: MutableState<Boolean> = remember { mutableStateOf(false) }
    val exitDialogVisibilityState: MutableState<Boolean> = remember { mutableStateOf(false) }

    TreeTheme(
        colorScheme = viewModel.colorScheme ?: (viewModel.settings ?: Settings()).getColorScheme()
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
                    },
                    navigationIcon = {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null
                                ) {
                                    tipsDialogVisibilityState.value =
                                        !tipsDialogVisibilityState.value
                                }
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
                            colorScheme = viewModel.colorScheme ?: (viewModel.settings
                                ?: Settings()).getColorScheme(),
                            node = viewModel.currentNode ?: Node(),
                            parent = viewModel.currentNodeParent ?: Node(),
                            onNavigateToParent = {
                                viewModel.updateCurrentNode(it)
                                navController.popAll()
                                navController.navigate(Screen.Node)
                            }
                        )
                    }

                    is Screen.Settings -> {
                        SettingsScreen(
                            modifier = Modifier.padding(innerPadding),
                            onSettingsChange = {
                                viewModel.setColorScheme(it.getColorScheme())
                                viewModel.changeSettings(it)
                            },
                            colorScheme = viewModel.colorScheme ?: (viewModel.settings
                                ?: Settings()).getColorScheme(),
                            settings = viewModel.settings ?: Settings()
                        )
                    }

                    is Screen.Child -> {
                        ChildScreen(
                            modifier = Modifier.padding(innerPadding),
                            child = viewModel.currentNodeChild,
                            colorScheme = viewModel.colorScheme ?: (viewModel.settings
                                ?: Settings()).getColorScheme(),
                            parentNode = viewModel.currentNode ?: Node(),
                            onNavigateToChild = {
                                viewModel.updateCurrentNode(it)
                                viewModel.updateCurrentScreen(Screen.Node)
                                navController.popAll()
                                navController.navigate(Screen.Node)
                            },
                            onChildCreated = { parent, child ->
                                viewModel.updateCurrentNode(parent)
                                viewModel.updateChild(child)
                                navController.popAll()
                                navController.navigate(Screen.Child)
                            },
                            onDeleteChild = {
                                viewModel.updateCurrentNode(it)
                                navController.popAll()
                                navController.navigate(Screen.Child)
                            }
                        )
                    }
                }
            }
        }

        BackHandler(enabled = true) {
            exitDialogVisibilityState.value = !exitDialogVisibilityState.value
        }

        AnimatedVisibility(
            visible = tipsDialogVisibilityState.value,
            enter = EnterTransition.None,
            exit = ExitTransition.None
        ) {
            TipsDialog(
                onConfirm = {
                    tipsDialogVisibilityState.value = !tipsDialogVisibilityState.value
                }) {
                Text(
                    " " + stringResource(viewModel.currentScreen.tips),
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    fontSize = 16.sp
                )
            }
        }

        AnimatedVisibility(
            visible = exitDialogVisibilityState.value,
            enter = EnterTransition.None,
            exit = ExitTransition.None
        ) {
            ExitDialog(onDismiss = { exitDialogVisibilityState.value = !exitDialogVisibilityState.value}) {
                activity.finish()
            }
        }

    }
}
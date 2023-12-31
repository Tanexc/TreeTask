package ru.tanexc.tree.presentation.child

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import ru.tanexc.tree.R
import ru.tanexc.tree.domain.model.Node
import ru.tanexc.tree.presentation.child.view_model.ChildScreenViewModel
import ru.tanexc.tree.presentation.components.cards.SwipeCard
import ru.tanexc.tree.presentation.components.dialogs.DeleteNodeDialog
import ru.tanexc.tree.presentation.components.dialogs.NodeCreatingDialog


@Composable
fun ChildScreen(
    modifier: Modifier = Modifier,
    parentNode: Node,
    child: List<Node>,
    onNavigateToChild: (Node) -> Unit,
    onChildCreated: (Node, List<Node>) -> Unit,
    colorScheme: ColorScheme,
    onDeleteChild: (Node) -> Unit,
) {

    val viewModel: ChildScreenViewModel = hiltViewModel()

    LaunchedEffect(true) {
        viewModel.updateNodeChild(child)
        viewModel.updateParent(parentNode)
    }


    val nodeCreatingDialogVisible: MutableState<Boolean> = remember { mutableStateOf(false) }
    val deleteNodeDialogState: MutableState<Node?> = remember { mutableStateOf(null) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .padding(
                    horizontal = 8.dp,
                    vertical = 0.dp
                )
        ) {

            (viewModel.child?: child).forEach {

                item {
                    SwipeCard(
                        modifier = Modifier
                            .padding(
                                0.dp,
                                4.dp
                            )
                            .height(84.dp),
                        borderRadius = 16.dp,
                        borderWidth = 1.dp,
                        borderColor = colorScheme.outline,
                        backgroundColor = colorScheme.secondaryContainer.copy(0.2f),
                        onClick = { onNavigateToChild(it) },
                        swipeContent = {
                            Box(modifier = Modifier.size(104.dp, 84.dp)) {
                                Text(
                                    stringResource(R.string.delete),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .align(Center)
                                        .clickable {
                                            deleteNodeDialogState.value = it
                                        },
                                    color = colorScheme.error
                                )
                            }
                        }
                    ) {

                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    text = it.label,
                                    modifier = Modifier.align(CenterVertically),
                                    textAlign = TextAlign.Center
                                )
                            }

                        }

                    }
                }

            }

        }

        FloatingActionButton(
            onClick = {
                nodeCreatingDialogVisible.value = true
            },
            containerColor = colorScheme.secondaryContainer,
            contentColor = contentColorFor(backgroundColor = colorScheme.secondaryContainer),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .height(56.dp)
        ) {
            Icon(
                Icons.Outlined.Add,
                contentDescription = null,
                modifier = Modifier.align(Center)
            )
        }
    }

    AnimatedVisibility(
        visible = nodeCreatingDialogVisible.value,
        enter = EnterTransition.None,
        exit = ExitTransition.None
    ) {
        NodeCreatingDialog(
            parent = Node(parent = (viewModel.parent?: parentNode).id),
            onConfirm = {
                viewModel.createNode(
                    it.description,
                    onChildCreated
                )
                nodeCreatingDialogVisible.value = false
            },
            onDismiss = { nodeCreatingDialogVisible.value = false }
        )
    }

    AnimatedVisibility(
        visible = (deleteNodeDialogState.value is Node),
        enter = EnterTransition.None,
        exit = ExitTransition.None
    ) {
        DeleteNodeDialog(
            onConfirm = {
                viewModel.deleteChildNodeBranch(deleteNodeDialogState.value!!)
                onDeleteChild(viewModel.parent!!)
                deleteNodeDialogState.value = null
            },
            onDismiss = { deleteNodeDialogState.value = null }
        )
    }
}
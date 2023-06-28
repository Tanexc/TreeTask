package ru.tanexc.tree.presentation.node


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import ru.tanexc.tree.R
import ru.tanexc.tree.domain.model.Node
import ru.tanexc.tree.presentation.components.cards.InfoCard
import ru.tanexc.tree.presentation.components.cards.SwipeCard
import ru.tanexc.tree.presentation.components.dialogs.DeleteNodeDialog
import ru.tanexc.tree.presentation.node.view_model.NodeScreenViewModel

@Composable
fun NodeScreen(
    modifier: Modifier = Modifier,
    node: Node,
    parent: Node,
    onNavigateToParent: (parentNode: Node) -> Unit,
    colorScheme: ColorScheme
) {

    val deleteNodeDialogState: MutableState<Node?> = remember { mutableStateOf(null) }
    val viewModel: NodeScreenViewModel = hiltViewModel()

    LaunchedEffect(true) {
        viewModel.updateCurrentNode(node.id)
    }

    LazyColumn(
        modifier = modifier.padding(8.dp, 0.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.info),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraLight
            )

            SwipeCard(
                modifier = Modifier
                    .padding(
                        0.dp,
                        4.dp
                    )
                    .height(108.dp),
                borderRadius = 16.dp,
                borderWidth = 1.dp,
                borderColor = colorScheme.outline,
                backgroundColor = colorScheme.secondaryContainer.copy(0.2f),
                swipeContent = {
                    Box(modifier = Modifier.size(104.dp, 108.dp)) {
                        Text(
                            stringResource(R.string.delete),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .clickable {
                                    if ((viewModel.currentNode ?: node).id != 1L) {
                                        deleteNodeDialogState.value = viewModel.currentNode ?: node
                                    }
                                },
                            color = if ((viewModel.currentNode ?: node).id != 1L) {
                                colorScheme.error
                            } else {
                                colorScheme.outline
                            }
                        )
                    }

                }
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = stringResource(R.string.title),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                    )
                    Row(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = (viewModel.currentNode ?: node).label,
                            modifier = Modifier.align(CenterVertically),
                            textAlign = TextAlign.Center
                        )
                    }

                }

            }

            InfoCard(
                modifier = Modifier
                    .padding(0.dp, 4.dp)
                    .wrapContentHeight()
                    .defaultMinSize(minHeight = 108.dp),
                borderRadius = 16.dp,
                borderWidth = 1.dp,
                borderColor = colorScheme.outline,
                backgroundColor = colorScheme.secondaryContainer.copy(0.2f)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = stringResource(R.string.description),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                    )
                    Row(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = if ((viewModel.currentNode
                                    ?: node).label == (viewModel.parentNode ?: parent).label
                            ) {
                                stringResource(R.string.root_description)
                            } else {
                                (viewModel.currentNode ?: node).description
                            },
                            modifier = Modifier
                                .align(CenterVertically)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                }

            }

            InfoCard(
                modifier = Modifier
                    .padding(
                        0.dp,
                        4.dp
                    )
                    .wrapContentHeight(),
                borderRadius = 16.dp,
                borderWidth = 1.dp,
                borderColor = colorScheme.outline,
                backgroundColor = colorScheme.secondaryContainer.copy(0.2f)
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .wrapContentHeight()
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .align(CenterVertically)
                    ) {
                        Text(
                            stringResource(R.string.amount_of_child_nodes) + " ${(viewModel.currentNode ?: node).child.size}",
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .align(CenterHorizontally)
                                .padding(8.dp)
                        )
                    }

                }
            }
        }
        item {

            Text(
                text = stringResource(R.string.parent),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraLight,
                modifier = Modifier.padding(top = 8.dp)
            )

            if (parent.label == node.label) {

                InfoCard(
                    modifier = Modifier
                        .padding(
                            0.dp,
                            4.dp
                        )
                        .wrapContentHeight(),
                    borderRadius = 16.dp,
                    borderWidth = 1.dp,
                    borderColor = colorScheme.outline,
                    backgroundColor = colorScheme.secondaryContainer.copy(0.2f)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .wrapContentHeight()
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .align(CenterVertically)
                        ) {
                            Text(
                                stringResource(R.string.no_parent),
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .align(CenterHorizontally)
                                    .padding(8.dp)
                            )
                        }

                    }
                }

            } else {

                InfoCard(
                    modifier = Modifier
                        .padding(
                            0.dp,
                            4.dp
                        )
                        .height(108.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            onNavigateToParent(parent)
                        },
                    borderRadius = 16.dp,
                    borderWidth = 1.dp,
                    borderColor = colorScheme.outline,
                    backgroundColor = colorScheme.secondaryContainer.copy(0.2f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = stringResource(R.string.title),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                        )
                        Row(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = (viewModel.parentNode ?: parent).label,
                                modifier = Modifier.align(CenterVertically),
                                textAlign = TextAlign.Center
                            )
                        }

                    }

                }
            }
        }
    }
    AnimatedVisibility(
        visible = (deleteNodeDialogState.value is Node),
        enter = EnterTransition.None,
        exit = ExitTransition.None
    ) {
        DeleteNodeDialog(
            onConfirm = {
                val newParent = (viewModel.parentNode ?: parent).copy(
                    child = (viewModel.parentNode
                        ?: parent).child.filter { it != (viewModel.currentNode ?: node).id })
                viewModel.deleteChildNodeBranch(viewModel.currentNode ?: node)
                viewModel.changeNode(newParent)
                onNavigateToParent(newParent)
                deleteNodeDialogState.value = null
            },
            onDismiss = { deleteNodeDialogState.value = null }
        )
    }
}
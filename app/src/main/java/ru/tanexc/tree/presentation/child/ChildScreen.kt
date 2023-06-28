package ru.tanexc.tree.presentation.child

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.tanexc.tree.domain.model.Node
import ru.tanexc.tree.presentation.components.cards.InfoCard
import ru.tanexc.tree.presentation.components.dialogs.NodeCreatingDialog
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChildScreen(
    modifier: Modifier = Modifier,
    shownNode: Node,
    child: List<Node>,
    onNavigateToChild: (Node) -> Unit,
    onChildCreated: (Node) -> Unit,
    colorScheme: ColorScheme
) {

    val nodeCreatingDialogVisible: MutableState<Boolean> = remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .padding(
                    horizontal = 4.dp,
                    vertical = 0.dp
                )
        ) {
            item {
                var unread by remember { mutableStateOf(false) }
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToEnd) unread = !unread
                        it != DismissValue.DismissedToEnd
                    }
                )

                SwipeToDismiss(
                    state = dismissState,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth(),
                    directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                    background = {
                        val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                        val color = Color.Green
                        val alignment = when (direction) {
                            DismissDirection.StartToEnd -> Alignment.CenterStart
                            DismissDirection.EndToStart -> Alignment.CenterEnd
                        }
                        val icon = when (direction) {
                            DismissDirection.StartToEnd -> Icons.Default.Done
                            DismissDirection.EndToStart -> Icons.Default.Delete
                        }
                        val scale = 0.75f

                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = 20.dp),
                            contentAlignment = alignment
                        ) {
                            Text(text = "Delete")
                        }
                    },
                    dismissContent = {
                        InfoCard(
                            modifier = Modifier
                                .padding(
                                    0.dp,
                                    4.dp
                                )
                                .height(84.dp)
                                .fillMaxWidth(),
                            borderRadius = 16.dp,
                            borderWidth = 1.dp,
                            borderColor = colorScheme.outline,
                            backgroundColor = colorScheme.secondaryContainer.copy(0.2f)
                        ) {

                            Text("swipe")

                        }
                    }
                )
                
            }

            item {

                var offsetX by remember { mutableStateOf(0f) }

                InfoCard(
                    modifier = Modifier
                        .offset { IntOffset(offsetX.roundToInt(), 0) }
                        .padding(
                            0.dp,
                            4.dp
                        )
                        .height(84.dp)
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()

                                val (x, _) = dragAmount
                                when {
                                    x > 0 -> { /* right */ }
                                    x < 0 -> { /* left */ }
                                }

                            }
                        },
                    borderRadius = 16.dp,
                    borderWidth = 1.dp,
                    borderColor = colorScheme.outline,
                    backgroundColor = colorScheme.secondaryContainer.copy(0.2f)
                ) {

                    Text("swipe")

                }

            }

            child.forEach {

                item {
                    InfoCard(
                        modifier = Modifier
                            .padding(
                                0.dp,
                                4.dp
                            )
                            .height(84.dp)
                            .clickable {
                                onNavigateToChild(it)
                            },
                        borderRadius = 16.dp,
                        borderWidth = 1.dp,
                        borderColor = colorScheme.outline,
                        backgroundColor = colorScheme.secondaryContainer.copy(0.2f)
                    ) {

                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    text = it.label,
                                    modifier = Modifier.align(Alignment.CenterVertically),
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
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .height(56.dp)
        ) {
            Icon(
                Icons.Outlined.Add,
                contentDescription = null,
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
        }
    }

    AnimatedVisibility(
        visible = nodeCreatingDialogVisible.value,
        enter = slideInVertically { it }) {
        NodeCreatingDialog(
            node = Node(parent = shownNode.id),
            onConfirm = {
                onChildCreated(it)
                nodeCreatingDialogVisible.value = false
            },
            onDismiss = { nodeCreatingDialogVisible.value = false }
        )
    }


}
package ru.tanexc.tree.presentation.tree


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tanexc.tree.R
import ru.tanexc.tree.domain.model.Node
import ru.tanexc.tree.presentation.components.InfoCard

@Composable
fun NodeScreen(
    modifier: Modifier = Modifier,
    node: Node,
    parent: Node,
    child: List<Node>,
    onNavigateToParent: (parentNode: Node) -> Unit,
    colorScheme: ColorScheme
) {
    LazyColumn(
        modifier = modifier.padding(8.dp, 0.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.info),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraLight
            )

            InfoCard(
                modifier = Modifier
                    .padding(
                        0.dp,
                        4.dp
                    )
                    .height(108.dp),
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
                            text = node.label,
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
                            text = if (node.label == parent.label) {
                                stringResource(R.string.root_description)
                            } else {
                                node.description
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
                            stringResource(R.string.amount_of_child_nodes) + " ${node.child.size}",
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
                modifier = Modifier.padding(top=8.dp)
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
                        .height(108.dp),
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
                                text = parent.label,
                                modifier = Modifier.align(CenterVertically),
                                textAlign = TextAlign.Center
                            )
                        }

                    }

                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    FilledTonalButton(
                        onClick = { onNavigateToParent(parent) },
                        modifier = Modifier
                            .wrapContentSize()
                            .align(CenterHorizontally)
                            .padding(0.dp, 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.navigate_to_parent),
                            modifier = Modifier
                                .align(CenterVertically)
                                .wrapContentSize()
                                .padding(4.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
        }
    }
}
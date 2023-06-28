package ru.tanexc.tree.presentation.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.tanexc.tree.R
import ru.tanexc.tree.domain.model.Node


@Composable
fun NodeCreatingDialog(
    parent: Node,
    onConfirm: (Node) -> Unit,
    onDismiss: () -> Unit
) {

    val _node: MutableState<Node> = remember { mutableStateOf(parent) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(stringResource(R.string.creating), textAlign = TextAlign.Center)
        },
        icon = { Icon(Icons.Outlined.EditNote, null) },
        confirmButton = {
            Text(
                stringResource(R.string.done),
                modifier = Modifier
                    .clickable { onConfirm(_node.value) })
        },
        dismissButton = {
            Text(
                stringResource(R.string.dismiss),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { onDismiss() }
            )
        },
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier
            .defaultMinSize(minWidth = 280.dp)
            .wrapContentHeight(),
        text = {
            Box {
                OutlinedTextField(
                    modifier = Modifier
                        .height(160.dp),
                    value = _node.value.description,
                    onValueChange = {
                        _node.value = _node.value.copy(description = it)
                    },
                    label = {
                        Text(stringResource(R.string.description))
                    }

                )
            }
        }
    )
}
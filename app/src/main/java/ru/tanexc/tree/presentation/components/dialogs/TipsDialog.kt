package ru.tanexc.tree.presentation.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tanexc.tree.R

@Composable
fun TipsDialog(
    onConfirm: () -> Unit,
    text: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onConfirm() },
        title = {
            Text(
                stringResource(R.string.tips),
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(R.font.montserrat))
            )
        },

        text = text,
        icon = { Icon(Icons.Outlined.Info, null) },
        confirmButton = {
            Text(
                stringResource(R.string.ok),
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        onConfirm()
                    },
                fontFamily = FontFamily(Font(R.font.montserrat))
            )
        },
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier
            .defaultMinSize(minWidth = 280.dp)
    )
}
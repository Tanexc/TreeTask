package ru.tanexc.tree.presentation.components.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SwipeCard(
    modifier: Modifier = Modifier,
    borderWidth: Dp = 0.dp,
    borderRadius: Dp = 0.dp,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary.copy(0.3f),
    onClick: () -> Unit = {},
    swipeContent: @Composable () -> Unit,
    content: @Composable () -> Unit

) {

    var offsetX by remember { mutableStateOf(0.dp) }
    var swiped by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentHeight()
    ) {

        AnimatedVisibility(
            visible = swiped,
            exit = ExitTransition.None,
            enter = EnterTransition.None
        ) {
            swipeContent()
        }

        Box(modifier = Modifier
            .offset(offsetX, 0.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    val x = dragAmount
                    when {
                        x > 0 -> {
                            if (x > 60) {
                                offsetX = 104.dp
                                swiped = true
                            }
                        }

                        x < 0 -> {
                            if (x < -60) {
                                swiped = false
                                offsetX = 0.dp
                            }
                        }
                    }
                }

            }) {


            Box(
                modifier = modifier
                    .border(
                        width = borderWidth,
                        shape = RoundedCornerShape(borderRadius),
                        brush = SolidColor(borderColor)
                    )
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(borderRadius)
                    )
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        if (!swiped) {
                            onClick()
                        }
                    }

            ) {
                content()
            }
        }


    }


}
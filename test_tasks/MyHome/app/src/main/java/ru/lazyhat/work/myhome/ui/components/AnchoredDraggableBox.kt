package ru.lazyhat.work.myhome.ui.components

import androidx.compose.animation.core.EaseOutCirc
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

enum class DragValue { Start, End }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnchoredDraggableBox(
    modifier: Modifier = Modifier,
    swipeContent: @Composable RowScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val density = LocalDensity.current
    var swipeContentSize by remember { mutableStateOf(IntSize.Zero) }
    val anchors = with(density) {
        DraggableAnchors {
            DragValue.Start at 0.dp.toPx()
            DragValue.End at -swipeContentSize.width.toFloat()
        }
    }
    val draggableState = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Start,
            positionalThreshold = { totalDistance: Float ->
                totalDistance * 0.5f
            },
            velocityThreshold = {
                with(density) {
                    100.dp.toPx()
                }
            },
            animationSpec = tween(300, 0, EaseOutCirc)
        )
    }
    SideEffect {
        draggableState.updateAnchors(anchors)
    }
    Box(modifier) {
        Row(modifier = Modifier
            .align(Alignment.CenterEnd)
            .onSizeChanged {
                swipeContentSize = it
            }
            .offset {
                IntOffset(
                    x = swipeContentSize.width + draggableState
                        .requireOffset()
                        .roundToInt(),
                    y = 0
                )
            }
            .padding(start = 30.dp)
        ) {
            swipeContent()
        }
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = draggableState
                            .requireOffset()
                            .roundToInt(),
                        y = 0
                    )
                }
                .anchoredDraggable(draggableState, Orientation.Horizontal)
        ) {
            content()
        }
    }
}

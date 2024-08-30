package com.chub.officemanager.ui.view

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.chub.officemanager.ui.ItemOperation
import com.chub.officemanager.util.OfficeItem

@Composable
fun OfficeItemLayout(
    item: OfficeItem,
    operations: List<ItemOperation>,
    onClick: (OfficeItem) -> Unit,
) {
    val isMenuVisible = rememberSaveable { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    var itemHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    val shape = RoundedCornerShape(12.dp)
    Card(
        modifier = Modifier
            .clip(shape)
            .fillMaxWidth()
            .heightIn(min = 96.dp, max = 160.dp)
            .indication(interactionSource, rememberRipple())
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        isMenuVisible.value = true
                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                    },
                    onPress = {
                        val press = PressInteraction.Press(it)
                        interactionSource.emit(press)
                        tryAwaitRelease()
                        interactionSource.emit(PressInteraction.Release(press))
                    },
                    onTap = {
                        onClick(item)
                    }
                )
            }
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            },
        shape = shape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(modifier = Modifier.padding(bottom = 8.dp), fontWeight = Bold, text = "${item.type} : ${item.name}")
            Text(text = item.description)
        }
        if (operations.isNotEmpty()) {
            PopupMenu(
                isMenuVisible,
                pressOffset,
                itemHeight,
                item,
                operations,
            )
        }

    }
}


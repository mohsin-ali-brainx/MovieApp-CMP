package com.brainx.utils_extensions.compose_ui_utils.safe_click

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import kotlin.time.Clock


internal interface MultipleEventsCutter {
    fun processEvent(event: () -> Unit)

    companion object
}

internal fun MultipleEventsCutter.Companion.get(): MultipleEventsCutter =
    MultipleEventsCutterImpl()

private class MultipleEventsCutterImpl : MultipleEventsCutter {
    private val now: Long
        get() = Clock.System.now().toEpochMilliseconds()

    private var lastEventTimeMs: Long = 0

    override fun processEvent(event: () -> Unit) {
        if (now - lastEventTimeMs >= 500L) {
            event.invoke()
        }
        lastEventTimeMs = now
    }
}

@Composable
fun Modifier.clickableSingleWithoutRipple(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    },
    factory = {
        val multipleEventsCutter = remember { MultipleEventsCutter.get() }
        this.then(
            Modifier.clickable(
                enabled = enabled,
                onClickLabel = onClickLabel,
                onClick = { multipleEventsCutter.processEvent { onClick() } },
                role = role,
                indication = null,
                interactionSource = NoRippleInteractionSource()
            )
        )
    }
)
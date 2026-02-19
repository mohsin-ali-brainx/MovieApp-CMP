package com.brainx.utils_extensions.compose_ui_utils.safe_click

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class NoRippleInteractionSource : MutableInteractionSource {

    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true

}

@Composable
fun Modifier.clickableWithoutRipple(onClick:()->Unit)= composed(
    factory = {
        this.then(
            Modifier.clickable(
                interactionSource = NoRippleInteractionSource(),
                indication = null,
                onClick = { onClick() }
            )
        )
    }
)
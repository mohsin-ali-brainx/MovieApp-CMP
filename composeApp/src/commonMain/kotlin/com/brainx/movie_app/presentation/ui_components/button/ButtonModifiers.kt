package com.brainx.movie_app.presentation.ui_components.button

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.brainx.movie_app.presentation.theme.AppDimens

@Composable
internal fun Modifier.defaultFullWidthButtonModifier() = composed(factory = {
    this.then(
        Modifier
        .wrapContentSize()
        .height(AppDimens.Button.defaultButtonHeight))
})
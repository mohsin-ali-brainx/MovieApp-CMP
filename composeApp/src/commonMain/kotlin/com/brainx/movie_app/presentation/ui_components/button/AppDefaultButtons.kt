package com.brainx.movie_app.presentation.ui_components.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.brainx.movie_app.presentation.theme.LocalAppTheme
import com.brainx.movie_app.presentation.theme.AppDimens
import com.brainx.movie_app.presentation.ui_components.text.CustomText
import com.brainx.movie_app.presentation.ui_components.text.CustomTextToDisplay

@Composable
private fun AppDefaultButton(
    modifier: Modifier = Modifier.defaultFullWidthButtonModifier(),
    textModifier:Modifier = Modifier.wrapContentSize(),
    buttonText: CustomTextToDisplay,
    buttonColor: Color,
    textColor: Color,
    borderColor: Color?=null,
    borderWidth: Dp = AppDimens.Button.zero,
    isClickable:Boolean=true,
    showLoader:Boolean=false,
    buttonRadius:Dp=AppDimens.Radius.radius12,
    buttonShape: Shape = RoundedCornerShape(buttonRadius),
    fontSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize,
    textAlign: TextAlign = TextAlign.Center,
    fontWeight: FontWeight = FontWeight.W500,
    fontStyle: FontStyle = FontStyle.Normal,
    leadingIcon: (@Composable() () -> Unit)? = null,
    trailingIcon: (@Composable() () -> Unit)? = null,
    onClickAction: ()->Unit
){
    val localDensity = LocalDensity.current
    val loaderSize = remember { with(localDensity) {
        fontSize.toDp()
    }}
    Button(
        onClick = { if (isClickable) onClickAction() else null},
        elevation = ButtonDefaults.buttonElevation(defaultElevation = AppDimens.Button.zero),
        enabled = isClickable,
        modifier = modifier,
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(containerColor =buttonColor),
        border = BorderStroke(borderWidth, borderColor?: Color.Transparent)
    ) {
        if (leadingIcon!=null)
            leadingIcon()

        CustomText(
            modifier=textModifier,
            text =buttonText ,
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign,
            fontStyle = fontStyle,
        )
        AnimatedVisibility(showLoader){
            CircularProgressIndicator(
                Modifier.padding(AppDimens.Padding.textPadding).size(loaderSize),
                color = textColor
            )
        }
        if (trailingIcon != null)
            trailingIcon()
    }
}


@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    buttonText:CustomTextToDisplay.StringResourceText,
    buttonColor: Color = LocalAppTheme.current.primaryButtonColor,
    isEnable:Boolean=true,
    borderColor: Color?=null,
    leadingIcon: (@Composable() () -> Unit)? = null,
    trailingIcon: (@Composable() () -> Unit)? = null,
    onClickAction: () -> Unit
){
    val isButtonEnable by remember(isEnable) { derivedStateOf { isEnable } }
    val clickAction by remember(onClickAction) { derivedStateOf { onClickAction }  }
    val buttonColor by remember(isButtonEnable) { derivedStateOf { if (isButtonEnable) buttonColor else buttonColor.copy(alpha = 0.90f)  }  }

    AppDefaultButton(
        modifier = modifier.then(Modifier.defaultFullWidthButtonModifier()),
        buttonText = buttonText,
        textColor = LocalAppTheme.current.primaryWhiteTextColor,
        buttonColor = buttonColor,
        fontSize = AppDimens.Fonts.font16,
        fontWeight = FontWeight.W600,
        borderColor = borderColor,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isClickable = isButtonEnable,
        onClickAction = clickAction
    )
}
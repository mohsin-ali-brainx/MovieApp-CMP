package com.brainx.ticket_tribe.presentation.ui_components.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.brainx.ticket_tribe.presentation.theme.AppDimens
import com.brainx.ticket_tribe.presentation.theme.LocalAppTheme
import com.brainx.ticket_tribe.presentation.theme.defaultEditTextShape
import com.brainx.utils_extensions.constants.ExtConstants
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tickettribekmp.composeapp.generated.resources.Res
import tickettribekmp.composeapp.generated.resources.ic_search
import tickettribekmp.composeapp.generated.resources.search_movies

@Composable
fun CustomBasicTextField(
    modifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    placeHolderText:String?=null,
    cursorBrush: Color = Color.White,
    textStyle: TextStyle = editTextStyle(),
    hintTextColor: Color?=null,
    hintTextStyle: TextStyle = editTextStyle(
        textColor = hintTextColor ?: MaterialTheme.colorScheme.onSecondaryContainer
    ),
    maxLength:Int?=null,
    singleLine:Boolean=true,
    minLines:Int?=null,
    maxLines:Int?=null,
    enabled:Boolean=true,
    hintTextAlignment: Alignment = Alignment.Center,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.None,
    keyboardActions: KeyboardActions = KeyboardActions(),
    onCursorPositionChange: ((Int) -> Unit)?=null,
    onValueChange: (String) -> Unit,
){

    // Holds the latest internal TextFieldValue state. We need to keep it to have the correct value
    // of the composition.
    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = text, selection = when {
                    text.isEmpty() -> TextRange.Zero
                    else -> TextRange(text.length, text.length)
                }
            )
        )
    }

    // Holds the latest TextFieldValue that BasicTextField was recomposed with. We couldn't simply
    // pass `TextFieldValue(text = value)` to the CoreTextField because we need to preserve the
    // composition.
    val textFieldValue = textFieldValueState.copy(text = text)

    SideEffect {
        if (textFieldValue.selection != textFieldValueState.selection ||
            textFieldValue.composition != textFieldValueState.composition
        ) {
            textFieldValueState = textFieldValue
        }
    }
    // Last String value that either text field was recomposed with or updated in the onValueChange
    // callback. We keep track of it to prevent calling onValueChange(String) for same String when
    // CoreTextField's onValueChange is called multiple times without recomposition in between.
    var lastTextValue by remember(text) { mutableStateOf(text) }

    BasicTextField(
        modifier= modifier,
        value = textFieldValue,
        cursorBrush = SolidColor(cursorBrush),
        enabled = enabled,
        onValueChange = { newTextFieldValueState ->
            if (maxLength!=null && newTextFieldValueState.text.length>maxLength) return@BasicTextField
            textFieldValueState = newTextFieldValueState

            val stringChangedSinceLastInvocation = lastTextValue != newTextFieldValueState.text
            lastTextValue = newTextFieldValueState.text

            if (stringChangedSinceLastInvocation) {
                onValueChange(newTextFieldValueState.text)
            }
            if (onCursorPositionChange!=null)
                onCursorPositionChange(newTextFieldValueState.selection.start)

        },
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions(
            capitalization = if (keyboardType == KeyboardType.Password) KeyboardCapitalization.None else KeyboardCapitalization.Sentences,
            autoCorrectEnabled = false,
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        minLines = minLines?: ExtConstants.IntegerConstants.ONE,
        maxLines = if (singleLine) ExtConstants.IntegerConstants.ONE else maxLines?:Int.MAX_VALUE,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = hintTextAlignment
            ) {
                if (text.isEmpty()) {
                    placeHolderText?.let {
                        Text(
                            modifier = Modifier.padding(AppDimens.Padding.zero),
                            text = it,
                            textAlign = TextAlign.Start,
                            style = hintTextStyle,
                        )
                    }
                }
            }
            innerTextField()
        })
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    placeHolderText:String?=null,
    cursorBrush: Color = Color.White,
    textStyle: TextStyle = editTextStyle(),
    hintTextColor: Color?=null,
    hintTextStyle: TextStyle = editTextStyle(
        textColor = hintTextColor ?: MaterialTheme.colorScheme.onSecondaryContainer
    ),
    maxLength:Int?=null,
    singleLine:Boolean=true,
    minLines:Int?=null,
    maxLines:Int?=null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.None,
    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChange: (String) -> Unit,
    paddingLeadingIconEnd: Dp = AppDimens.Padding.smallPadding,
    paddingTrailingIconStart: Dp = AppDimens.Padding.smallPadding,
    leadingIcon: (@Composable() () -> Unit)? = null,
    trailingIcon: (@Composable() () -> Unit)? = null,
    onCursorPositionChange: ((Int) -> Unit)?=null,
) {
    Row(modifier = modifier
        .padding(vertical = AppDimens.Padding.xxSmallPadding, horizontal = AppDimens.Padding.xxSmallPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start) {
        if (leadingIcon!=null)
            leadingIcon()
        Box(
            modifier = Modifier
                .padding(start = paddingLeadingIconEnd, end = paddingTrailingIconStart)
                .fillMaxWidth()
        ) {
            CustomBasicTextField(
                text = text,
                cursorBrush = cursorBrush,
                textStyle = textStyle,
                placeHolderText = placeHolderText,
                hintTextColor = hintTextColor,
                maxLength = maxLength,
                keyboardType = keyboardType,
                singleLine = singleLine,
                minLines = minLines,
                maxLines = maxLines,
                hintTextStyle = hintTextStyle,
                imeAction = imeAction,
                hintTextAlignment = Alignment.TopStart,
                keyboardActions = keyboardActions,
                onValueChange = { onValueChange(it) },
                onCursorPositionChange = onCursorPositionChange
            )
        }
        if (trailingIcon != null)
            trailingIcon()
    }

}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    text: String,
    imeAction: ImeAction = ImeAction.Search,
    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChange: (String) -> Unit
){
    val appThemeColor =  LocalAppTheme.current
    val editTextModifier = modifier
        .height(AppDimens.EditText.searchBarHeight)
        .background(appThemeColor.mainEditTextColor, shape = defaultEditTextShape())
        .clip(defaultEditTextShape())
    CustomTextField(
        text = text,
        placeHolderText = stringResource(Res.string.search_movies),
        modifier = editTextModifier,
        hintTextColor = appThemeColor.primaryWhiteTextColor.copy(alpha = 0.3F),
        onValueChange = onValueChange,
        imeAction = imeAction,
        keyboardActions = keyboardActions,
        leadingIcon = {
            Image(
                modifier = Modifier.padding(start = AppDimens.Padding.smallPadding),
                painter = painterResource( Res.drawable.ic_search), contentDescription = ExtConstants.StringConstants.EMPTY )
        }
    )
}

@Preview
@Composable
private fun PreviewSearch(){
    SearchBar(modifier = Modifier, text = "") { }
}
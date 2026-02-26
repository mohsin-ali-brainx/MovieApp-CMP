package com.brainx.movie_app.presentation.ui_components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.brainx.movie_app.presentation.theme.appPrimaryFontFamily
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed interface CustomTextToDisplay{
    data class StringResourceText(val text: StringResource):CustomTextToDisplay
    data class StringText(val text:String):CustomTextToDisplay
    data class AnnotatedStringText(val text: AnnotatedString):CustomTextToDisplay
}

@Composable
fun CustomText(
    modifier: Modifier,
    text: CustomTextToDisplay,
    fontSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize,
    textAlign: TextAlign = TextAlign.Center,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    fontWeight: FontWeight = FontWeight.W400,
    minLines:Int = 1,
    maxLines:Int = Int.MAX_VALUE,
    fontStyle: FontStyle = FontStyle.Normal,
    textStyle: TextStyle?=null,
    textOverflow: TextOverflow = TextOverflow.Ellipsis,
    textDecoration: TextDecoration? = null
){
    when(text){
        is CustomTextToDisplay.StringResourceText->{
            Text(
                modifier =modifier,
                text =  stringResource(text.text),
                style = textStyle ?: TextStyle(
                    fontWeight = fontWeight,
                    fontFamily = appPrimaryFontFamily(),
                    fontSize = fontSize,
                    fontStyle = fontStyle,
                    color = color
                ),
                textAlign = textAlign,
                minLines = minLines,
                maxLines = maxLines,
                overflow = textOverflow,
                textDecoration = textDecoration,
            )
        }
        is CustomTextToDisplay.AnnotatedStringText->{
            Text(
                modifier =modifier,
                text =  text.text,
                style = textStyle ?: TextStyle(
                    fontWeight = fontWeight,
                    fontFamily = appPrimaryFontFamily(),
                    fontSize = fontSize,
                    fontStyle = fontStyle,
                    color = color
                ),
                textAlign = textAlign,
                minLines = minLines,
                maxLines = maxLines,
                overflow = textOverflow,
                textDecoration = textDecoration
            )
        }
        is CustomTextToDisplay.StringText->{
            Text(
                modifier =modifier,
                text =  text.text,
                style = textStyle ?: TextStyle(
                    fontWeight = fontWeight,
                    fontFamily = appPrimaryFontFamily(),
                    fontSize = fontSize,
                    fontStyle = fontStyle,
                    color = color
                ),
                textAlign = textAlign,
                minLines = minLines,
                maxLines = maxLines,
                overflow = textOverflow,
                textDecoration = textDecoration
            )
        }
    }

}

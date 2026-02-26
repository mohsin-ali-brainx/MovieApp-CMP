package com.brainx.movie_app.presentation.ui_components.list_items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.brainx.domain.network.dto_mappers.movie.MediaDTO
import com.brainx.movie_app.presentation.theme.LocalAppTheme
import com.brainx.movie_app.presentation.theme.AppDimens
import com.brainx.movie_app.presentation.ui_components.text.CustomText
import com.brainx.movie_app.presentation.ui_components.text.CustomTextToDisplay
import com.brainx.utils_extensions.constants.ExtConstants

@Composable
fun MovieCard(modifier: Modifier, data: MediaDTO) {

    val appThemeColor =  LocalAppTheme.current

    val displayText = remember(data.name, data.title) {
        data.name ?: data.title ?: ExtConstants.StringConstants.EMPTY
    }

    Column(
        modifier = modifier
            .width(140.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MoviePoster(url = data.posterPath)
        CustomText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppDimens.Padding.defaultPadding),
            text = CustomTextToDisplay.StringText(
                text = displayText
            ),
            color = appThemeColor.secondaryTextColor,
            fontSize = AppDimens.Fonts.font18,
            maxLines = 1,
            minLines = 1,
            textOverflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.W400
        )
    }
}


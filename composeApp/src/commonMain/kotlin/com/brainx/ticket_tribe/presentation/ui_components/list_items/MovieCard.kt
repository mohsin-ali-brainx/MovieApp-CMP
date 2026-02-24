package com.brainx.ticket_tribe.presentation.ui_components.list_items

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.brainx.domain.network.dto_mappers.movie.MediaDTO
import com.brainx.ticket_tribe.presentation.theme.LocalAppTheme
import com.brainx.ticket_tribe.presentation.theme.AppDimens
import com.brainx.ticket_tribe.presentation.ui_components.text.CustomText
import com.brainx.ticket_tribe.presentation.ui_components.text.CustomTextToDisplay
import com.brainx.utils_extensions.compose_ui_utils.animation.PulseAnimation
import com.brainx.utils_extensions.constants.ExtConstants
import com.brainx.utils_extensions.constants.ExtConstants.AnimationsConstants.IMAGE_ANIMATION_DURATION
import com.brainx.utils_extensions.constants.ExtConstants.IntegerConstants.ONE
import com.brainx.utils_extensions.constants.ExtConstants.StringConstants.NO_IMAGE_URL

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


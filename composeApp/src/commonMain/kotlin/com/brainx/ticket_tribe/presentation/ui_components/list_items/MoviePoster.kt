package com.brainx.ticket_tribe.presentation.ui_components.list_items

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.brainx.ticket_tribe.presentation.theme.AppDimens
import com.brainx.utils_extensions.compose_ui_utils.animation.PulseAnimation
import com.brainx.utils_extensions.constants.ExtConstants
import com.brainx.utils_extensions.constants.ExtConstants.AnimationsConstants.IMAGE_ANIMATION_DURATION
import com.brainx.utils_extensions.constants.ExtConstants.IntegerConstants.ONE
import com.brainx.utils_extensions.constants.ExtConstants.StringConstants.NO_IMAGE_URL

@Composable
fun MoviePoster(modifier: Modifier= Modifier, url: String?) {
    Box(
        modifier = modifier
            .height(220.dp)
            .clip(RoundedCornerShape(AppDimens.Radius.radius16)),
        contentAlignment = Alignment.Center
    ) {
        var imageLoadResult by remember {
            mutableStateOf<Result<Painter>?>(null)
        }
        val imageUrl = if (url != null) "https://image.tmdb.org/t/p/w500${url}" else NO_IMAGE_URL
        val painter = rememberAsyncImagePainter(
            model = imageUrl,
            onSuccess = {
                imageLoadResult =
                    if (it.painter.intrinsicSize.width > ONE && it.painter.intrinsicSize.height > ONE) {
                        Result.success(it.painter)
                    } else {
                        Result.failure(Exception("Invalid image size"))
                    }
            },
            onError = {
                it.result.throwable.printStackTrace()
                imageLoadResult = Result.failure(it.result.throwable)
            }
        )

        val painterState by painter.state.collectAsStateWithLifecycle()
        val transition by animateFloatAsState(
            targetValue = if (painterState is AsyncImagePainter.State.Success) {
                ExtConstants.FloatConstants.ONE
            } else {
                ExtConstants.FloatConstants.ZERO
            },
            animationSpec = tween(durationMillis = IMAGE_ANIMATION_DURATION)
        )

        when (val result = imageLoadResult) {
            null -> PulseAnimation(
                modifier = Modifier.size(60.dp)
            )

            else -> {
                Image(
                    painter = painter,
                    contentDescription = "",
                    contentScale = if (result.isSuccess) {
                        ContentScale.Crop
                    } else {
                        ContentScale.Fit
                    },
                    modifier = Modifier
                        .aspectRatio(
                            ratio = 0.65f,
                            matchHeightConstraintsFirst = true
                        )
                        .graphicsLayer {
                            rotationX = (1f - transition) * 30f
                            val scale = 0.8f + (0.2f * transition)
                            scaleX = scale
                            scaleY = scale
                        }
                )
            }
        }
    }
}
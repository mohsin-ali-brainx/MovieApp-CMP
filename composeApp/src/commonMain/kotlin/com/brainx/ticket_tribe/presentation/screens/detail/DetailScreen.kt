package com.brainx.ticket_tribe.presentation.screens.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.brainx.domain.network.dto_mappers.movie.MediaDTO
import com.brainx.ticket_tribe.presentation.navigation.AppRoutes
import com.brainx.ticket_tribe.presentation.theme.AppDimens
import com.brainx.ticket_tribe.presentation.theme.LocalAppTheme
import com.brainx.ticket_tribe.presentation.ui_components.button.PrimaryButton
import com.brainx.ticket_tribe.presentation.ui_components.list_items.MoviePoster
import com.brainx.ticket_tribe.presentation.ui_components.text.CustomText
import com.brainx.ticket_tribe.presentation.ui_components.text.CustomTextToDisplay
import com.brainx.utils_extensions.compose_ui_utils.animation.PulseAnimation
import com.brainx.utils_extensions.constants.ExtConstants
import com.brainx.utils_extensions.constants.ExtConstants.AnimationsConstants.IMAGE_ANIMATION_DURATION
import tickettribekmp.composeapp.generated.resources.Res
import tickettribekmp.composeapp.generated.resources.play_video
import com.brainx.domain.utils.media_type_utils.isVideoContent
import com.brainx.utils_extensions.compose_ui_utils.safe_click.clickableSingleWithoutRipple
import com.brainx.utils_extensions.constants.ExtConstants.StringConstants.NO_IMAGE_URL
import org.jetbrains.compose.resources.painterResource
import tickettribekmp.composeapp.generated.resources.ic_arrow_back
import tickettribekmp.composeapp.generated.resources.ic_play

@Composable
fun DetailScreen(
    mediaDataModel: MediaDTO,
    onNavigate: (AppRoutes) -> Unit,
    onBack:()->Unit
){
    val appThemeColor =  LocalAppTheme.current


    Scaffold(
        modifier = Modifier.background(appThemeColor.mainBackgroundColor)
            .fillMaxSize()
            .imePadding()
    ) { paddingValues ->
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .background(appThemeColor.mainBackgroundColor)
                .padding(paddingValues)
        ) {
            val (backBtn,banner,gradient,poster,button,title,description) = createRefs()

            BannerImage(
                url = mediaDataModel.backdropPath,
                posterUrl = mediaDataModel.posterPath,
                modifier = Modifier.constrainAs(banner) {
                    linkTo(start = parent.start,end=parent.end)
                    linkTo(top = parent.top,bottom=parent.bottom)
                },
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.6f),
                                Color.Black.copy(0.8f)
                            )
                        )
                    )
                    .constrainAs(gradient) {
                        linkTo(start = parent.start, end = parent.end)
                        linkTo(top = parent.top, bottom = parent.bottom)
                    }
            )

            MoviePoster(
                modifier = Modifier.constrainAs(poster) {
                    linkTo(start = parent.start,end=parent.end)
                    top.linkTo(parent.top, margin = AppDimens.Padding.xLargePadding)
                },
                url = mediaDataModel.posterPath,
            )

            CustomText(
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(title) {
                        linkTo(
                            start = parent.start,
                            end = parent.end,
                            startMargin = AppDimens.Padding.mediumPadding,
                            endMargin = AppDimens.Padding.mediumPadding
                        )
                        top.linkTo(poster.bottom, margin = AppDimens.Padding.mediumPadding)
                    },
                text = CustomTextToDisplay.StringText(mediaDataModel.title ?: mediaDataModel.name ?: ""),
                color = appThemeColor.primaryWhiteTextColor,
                fontSize = AppDimens.Fonts.font24,
                fontWeight = FontWeight.Bold
            )

            CustomText(
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(description) {
                        linkTo(
                            start = parent.start,
                            end = parent.end,
                            bias = ExtConstants.FloatConstants.ZERO,
                            startMargin = AppDimens.Padding.mediumPadding,
                            endMargin = AppDimens.Padding.mediumPadding
                        )
                        top.linkTo(title.bottom, margin = AppDimens.Padding.smallPadding12)
                    },
                text = CustomTextToDisplay.StringText(mediaDataModel.overview ?: ""),
                color = appThemeColor.primaryWhiteTextColor,
                fontSize = AppDimens.Fonts.font16,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Start
            )

            if (mediaDataModel.mediaType?.isVideoContent()==true){
                PrimaryButton(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .constrainAs(button) {
                            linkTo(start = parent.start, end = parent.end)
                            bottom.linkTo(parent.bottom, margin = AppDimens.Padding.smallPadding)
                        },
                    leadingIcon = {
                        Icon(painter = painterResource(Res.drawable.ic_play), contentDescription = ExtConstants.StringConstants.EMPTY)
                    },
                    buttonColor = appThemeColor.secondaryColor,
                    buttonText = CustomTextToDisplay.StringResourceText(Res.string.play_video))
                {
                    mediaDataModel.apply {
                        onNavigate(
                            AppRoutes.VideoPlayer(
                                id = id ?: ExtConstants.IntegerConstants.ZERO,
                                mediaType = mediaType ?: ExtConstants.StringConstants.EMPTY
                            )
                        )
                    }
                }
            }

            IconButton(
                modifier = Modifier.constrainAs(backBtn) {
                    top.linkTo(parent.top, margin = AppDimens.Padding.mediumPadding)
                    start.linkTo(parent.start, margin = AppDimens.Padding.mediumPadding)
                },
                onClick = {
                    onBack()
                },
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    tint = appThemeColor.primaryWhiteIconColor,
                    contentDescription = ExtConstants.StringConstants.EMPTY)
            }
        }
    }
}

@Composable
private fun BannerImage(modifier: Modifier,url: String?,posterUrl: String?){
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        var imageLoadResult by remember {
            mutableStateOf<Result<Painter>?>(null)
        }
        val imageUrl = if (url!=null) "https://image.tmdb.org/t/p/original${url}" else if(posterUrl!=null) "https://image.tmdb.org/t/p/w500${posterUrl}"  else NO_IMAGE_URL
        val painter = rememberAsyncImagePainter(
            model = imageUrl,
            contentScale = ContentScale.FillHeight,
            onSuccess = {
                imageLoadResult =
                    if (it.painter.intrinsicSize.width > ExtConstants.IntegerConstants.ONE && it.painter.intrinsicSize.height > ExtConstants.IntegerConstants.ONE) {
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
                    contentDescription = ExtConstants.StringConstants.EMPTY,
                    contentScale = if (result.isSuccess) {
                        ContentScale.Crop
                    } else {
                        ContentScale.Fit
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = transition
                        }
                        .blur(radius = AppDimens.Radius.radius12)
                )
            }
        }
    }
}
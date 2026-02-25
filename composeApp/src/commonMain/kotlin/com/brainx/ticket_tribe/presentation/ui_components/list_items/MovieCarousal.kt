package com.brainx.ticket_tribe.presentation.ui_components.list_items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.brainx.domain.network.dto_mappers.movie.MediaDTO
import com.brainx.domain.network.dto_mappers.movie.MovieTypeDTO
import com.brainx.ticket_tribe.presentation.theme.AppDimens
import com.brainx.ticket_tribe.presentation.ui_components.text.CustomText
import com.brainx.ticket_tribe.presentation.ui_components.text.CustomTextToDisplay
import com.brainx.utils_extensions.compose_ui_utils.safe_click.clickableWithoutRipple
import com.brainx.utils_extensions.constants.ExtConstants
import com.brainx.utils_extensions.constants.ExtConstants.IntegerConstants.ONE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlin.compareTo


@Composable
fun MovieCarousal(
    modifier: Modifier = Modifier,
    data: MovieTypeDTO,
    onClick: (MediaDTO) -> Unit,
    onLoadMore: () -> Unit
) {
    // Remember list state with stable key
    val listState = rememberLazyListState()

    // Remember computed text
    val mediaTypeText = remember(data.mediaType) {
        derivedStateOf { data.mediaType.uppercase() }
    }

    // Remember items list
    val mediaItems = remember(data.mediaItems) {
        data.mediaItems
    }

    // Remember callbacks
    val onItemClick = remember(onClick) { onClick }
    val onLoadMoreCallback = remember(onLoadMore) { onLoadMore }

    LaunchedEffect(listState, mediaItems.size) {
        snapshotFlow { listState.isScrollInProgress }
            .filter { it }
            .flatMapLatest {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            }
            .collect { visibleItems ->
                val lastVisible = visibleItems.lastOrNull()?.index ?: return@collect
                if (lastVisible >= mediaItems.size - ONE) {
                    onLoadMoreCallback()
                }
            }
    }

    Column(modifier = modifier.wrapContentSize()) {
        MediaTypeHeader(mediaType = mediaTypeText.value)
        MediaItemsList(
            items = mediaItems,
            listState = listState,
            onItemClick = onItemClick
        )
    }
}

@Composable
private fun MediaTypeHeader(mediaType: String) {
    CustomText(
        text = CustomTextToDisplay.StringText(mediaType),
        fontSize = AppDimens.Fonts.font24,
        fontWeight = FontWeight.W500,
        modifier = Modifier.wrapContentSize()
    )
}

@Composable
private fun MediaItemsList(
    items: List<MediaDTO>,
    listState: LazyListState,
    onItemClick: (MediaDTO) -> Unit
) {
    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppDimens.Padding.smallPadding),
        horizontalArrangement = Arrangement.spacedBy(AppDimens.Padding.smallPadding)
    ) {
        items(
            items = items,
            key = { item -> item.id ?: 0 } // Simple, stable key
        ) { item ->
            MovieCardItem(
                item = item,
                onClick = { onItemClick(item) }
            )
        }
    }
}

@Composable
private fun MovieCardItem(
    item: MediaDTO,
    onClick: () -> Unit
) {
    MovieCard(
        modifier = Modifier
            .wrapContentSize()
            .clickableWithoutRipple(onClick = onClick),
        data = item
    )
}

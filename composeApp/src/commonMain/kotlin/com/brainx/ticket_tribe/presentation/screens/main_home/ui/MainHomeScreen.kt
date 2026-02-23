package com.brainx.ticket_tribe.presentation.screens.main_home.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.brainx.ticket_tribe.presentation.navigation.AppRoutes
import com.brainx.ticket_tribe.presentation.screens.main_home.ui_events.MainHomeScreenUiEvents
import com.brainx.ticket_tribe.presentation.screens.main_home.ui_state.MainHomeScreenUiState
import com.brainx.ticket_tribe.presentation.screens.main_home.ui_intents.MainHomeScreenUiIntents
import com.brainx.ticket_tribe.presentation.theme.AppColors
import com.brainx.ticket_tribe.presentation.theme.AppDimens
import com.brainx.ticket_tribe.presentation.ui_components.button.PrimaryButton
import com.brainx.ticket_tribe.presentation.ui_components.list_items.MovieCarousal
import com.brainx.ticket_tribe.presentation.ui_components.text.CustomTextToDisplay
import com.brainx.ticket_tribe.presentation.ui_components.textfield.SearchBar
import com.brainx.utils_extensions.compose_ui_utils.ConsumeUIEffects
import com.brainx.utils_extensions.compose_ui_utils.safe_click.clickableSingleWithoutRipple
import com.brainx.utils_extensions.constants.ExtConstants
import com.brainx.utils_extensions.navigation.toJson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import tickettribekmp.composeapp.generated.resources.Res
import tickettribekmp.composeapp.generated.resources.search

@Composable
fun MainHomeScreen(
    dataState: StateFlow<MainHomeScreenUiState>,
    uiEvents: Flow<MainHomeScreenUiEvents>,
    onIntent: (MainHomeScreenUiIntents) -> Unit,
    onNavigate:(AppRoutes)->Unit
) {
    val state by dataState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // ConsumeUIEffects already handles flow collection efficiently
    ConsumeUIEffects(uiEvents) { event, scope ->
        when (event) {
            is MainHomeScreenUiEvents.Navigate.MoveToDetail -> {
                onNavigate(AppRoutes.Detail(event.media.toJson()))
            }
            else -> Unit
        }
    }

    MainContent(state, onIntent = {
        if (it !is MainHomeScreenUiIntents.TextFieldsIntent.OnSearchTextUpdate) {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
        onIntent(it)
    })

}

@Composable
private fun MainContent(
    dataState: MainHomeScreenUiState,
    onIntent: (MainHomeScreenUiIntents) -> Unit
){
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var isKeyboardVisible by remember { mutableStateOf(false) }

    val keyboardHeight = WindowInsets.ime.getBottom(density = LocalDensity.current)

    LaunchedEffect(key1 = keyboardHeight) {
        isKeyboardVisible = keyboardHeight > 0
    }

    // Use derivedStateOf for computed values to prevent unnecessary recompositions
    val searchResults = remember(dataState.searchResponse) {
        derivedStateOf {
            dataState.searchResponse?.result ?: emptyList()
        }
    }

    // Remember search text to isolate recompositions
    val searchText = remember(dataState.searchText) { dataState.searchText }

    // Remember loading state with page check to prevent unnecessary recompositions
    val shouldShowLoading = remember(dataState.isLoading, dataState.searchResponse?.metaData?.page) {
        derivedStateOf {
            dataState.isLoading &&
            (dataState.searchResponse?.metaData?.page ?: ExtConstants.IntegerConstants.ONE) <= ExtConstants.IntegerConstants.ONE
        }
    }

    Scaffold(
        modifier = Modifier.background(AppColors.mainBackgroundColor)
            .fillMaxSize()
//            .statusBarsPadding()
            .imePadding()
            .clickableSingleWithoutRipple {
                focusManager.clearFocus(force = true)
                keyboardController?.hide()
            }
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.Center
        ) {
            ConstraintLayout(
                Modifier
                    .fillMaxSize()
                    .background(AppColors.mainBackgroundColor)
                    .padding(horizontal = AppDimens.Padding.defaultPadding)
                    .padding(paddingValues)
            ) {
                val (searchBar, searchButton, listView,loader) = createRefs()

                SearchBar(
                    modifier = Modifier.constrainAs(searchBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(searchButton.start, margin = AppDimens.Padding.smallPadding)
                        width = Dimension.fillToConstraints
                    },
                    text = searchText,
                    keyboardActions = KeyboardActions(onSearch = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onIntent(MainHomeScreenUiIntents.ButtonIntents.OnSearchButtonIntent)
                    })
                ) {
                    onIntent(MainHomeScreenUiIntents.TextFieldsIntent.OnSearchTextUpdate(search = it))
                }

                PrimaryButton(
                    modifier = Modifier.constrainAs(searchButton) {
                        end.linkTo(parent.end)
                    },
                    buttonText = CustomTextToDisplay.StringResourceText(text = Res.string.search),
                    isEnable = searchText.isNotEmpty()
                )
                {
                    onIntent(MainHomeScreenUiIntents.ButtonIntents.OnSearchButtonIntent)
                }



                LazyColumn(
                    modifier = Modifier
//                        .imePadding()
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                        .constrainAs(listView) {
                            linkTo(
                                top = searchBar.bottom,
                                bottom = parent.bottom,
                                topMargin = AppDimens.Padding.mediumPadding
                            )
                            linkTo(start = parent.start, end = parent.end)
                            height = Dimension.fillToConstraints
                            width = Dimension.matchParent
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(AppDimens.Padding.smallPadding)
                ) {
                    itemsIndexed(
                        items = searchResults.value,
                        key = { index, item -> item.mediaType } // Stable key for efficient recomposition
                    ) { index, item ->
                        MovieCarousal(
                            modifier = Modifier.fillMaxWidth(),
                            data = item,
                            onLoadMore = {
                                onIntent(MainHomeScreenUiIntents.ListItemIntent.OnTriggerPagination)
                            },
                            onClick = {
                                onIntent(MainHomeScreenUiIntents.ListItemIntent.OnMovieItemClick(media = it))
                            }
                        )
                    }
                }

                if (shouldShowLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .constrainAs(loader) {
                                linkTo(start = parent.start, end = parent.end)
                                linkTo(top = parent.top, bottom = parent.bottom)
                            }
                            .size(AppDimens.Icons.loaderSize),
                        color = AppColors.secondaryColor
                    )
                }
            }
        }

    }
}


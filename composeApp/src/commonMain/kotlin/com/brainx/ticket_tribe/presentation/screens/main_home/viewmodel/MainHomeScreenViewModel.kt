package com.brainx.ticket_tribe.presentation.screens.main_home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brainx.domain.network.dto_mappers.movie.MovieTypeDTO
import com.brainx.domain.network.dto_mappers.movie.SearchMultiMovieDto
import com.brainx.domain.use_cases.SearchMultiUseCase
import com.brainx.domain.utils.resource_state.Resource
import com.brainx.ticket_tribe.presentation.screens.main_home.ui_events.MainHomeScreenUiEvents
import com.brainx.ticket_tribe.presentation.screens.main_home.ui_intents.MainHomeScreenUiIntents
import com.brainx.ticket_tribe.presentation.screens.main_home.ui_state.MainHomeScreenUiState
import com.brainx.utils_extensions.constants.ExtConstants.IntegerConstants.ONE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainHomeScreenViewModel(
    private val ioDispatcher : CoroutineDispatcher,
    private val searchMultiUseCase: SearchMultiUseCase
): ViewModel() {
    private var searchJob: Job? = null

    private val _state = MutableStateFlow(MainHomeScreenUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 500) ,
        initialValue = MainHomeScreenUiState()
    )

    private val _eventFlow = Channel<MainHomeScreenUiEvents>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private fun emitUIEvents(event: MainHomeScreenUiEvents){
        viewModelScope.launch {
            _eventFlow.send(event)
        }
    }

    private fun searchMulti(page: Int = ONE, resetResults: Boolean = false) {
        if (resetResults) {
            searchJob?.cancel()
            _state.update {
                it.copy(
                    searchResponse = null
                )
            }
        }
        searchJob = searchMultiUseCase.invoke(_state.value.searchText, page = page)
            .onStart {

            }
            .onCompletion {

            }
            .onEach {result->
                when(result){
                    is Resource.Success->{
                        val responseResultData = result.data
                        updateListState(
                            responseResultData = responseResultData,
                            appendToExisting = !resetResults
                        )
                    }
                    is Resource.Error->{
//                        Events.updateBaseEvent(BaseUiEvents.ShowToast(message = result.message))
                    }
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = result.isLoading) }
                    }
                }
            }
            .flowOn(ioDispatcher)
            .launchIn(viewModelScope)
    }

    private fun updateListState(
        responseResultData: SearchMultiMovieDto?,
        appendToExisting: Boolean
    ) {
        _state.update { currentState ->
            val previousList = if (appendToExisting) {
                currentState.searchResponse?.result ?: emptyList()
            } else {
                emptyList()
            }
            val newList = responseResultData?.result ?: emptyList()


            val combinedMap = mutableMapOf<String, MovieTypeDTO>()


            previousList.forEach { oldItem ->
                combinedMap[oldItem.mediaType] = oldItem
            }


            newList.forEach { newItem ->
                val existing = combinedMap[newItem.mediaType]
                if (existing != null) {

                    val existingIds = existing.mediaItems.map { it.id }.toSet()
                    

                    val uniqueNewItems = newItem.mediaItems.filter { it.id !in existingIds }
                    

                    combinedMap[newItem.mediaType] = existing.copy(
                        mediaItems = existing.mediaItems + uniqueNewItems
                    )
                } else {
                    combinedMap[newItem.mediaType] = newItem
                }
            }

            currentState.copy(
                searchResponse = SearchMultiMovieDto(
                    result = combinedMap.values.toList(),
                    metaData = responseResultData?.metaData
                ),
                isLoading = false
            )
        }
    }


    fun onIntent(intent: MainHomeScreenUiIntents){
        when(intent){
            is MainHomeScreenUiIntents.TextFieldsIntent.OnSearchTextUpdate->{
                _state.update { it.copy(searchText = intent.search) }
            }
            is MainHomeScreenUiIntents.ButtonIntents.OnSearchButtonIntent->{
                searchMulti(resetResults = true)
            }
            is MainHomeScreenUiIntents.ListItemIntent.OnMovieItemClick->{
                emitUIEvents(MainHomeScreenUiEvents.Navigate.MoveToDetail(intent.media))
            }
            is MainHomeScreenUiIntents.ListItemIntent.OnTriggerPagination->{
                _state.value.searchResponse?.metaData?.apply {
                    if (page==totalPages) return
                    searchMulti(page = page?.plus(ONE) ?: ONE)
                }

            }
        }
    }

}
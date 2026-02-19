package com.brainx.utils_extensions.compose_ui_utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun <T> ConsumeUIEffects(
    uiEvents: Flow<T>,
    shouldBeLifecycleAware: Boolean = false,
    consumer: (T) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val scope = rememberCoroutineScope()

    DisposableEffect(uiEvents) {
        val job = if (shouldBeLifecycleAware) {
            uiEvents
                .onEach { consumer(it) }
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .launchIn(lifecycle.coroutineScope)
        } else {
            uiEvents
                .onEach { consumer(it) }
                .launchIn(scope)
        }

        onDispose { job.cancel() }
    }
}

@Composable
fun <T> ConsumeUIEffects(
    uiEvents: Flow<T>,
    shouldBeLifecycleAware:Boolean=false,
    consumer: (T,CoroutineScope) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(uiEvents) {
        if (shouldBeLifecycleAware){
            lifecycle.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                uiEvents.collectLatest {
                    consumer(it,this)
                }
            }
        }else{
            uiEvents.collectLatest { consumer(it,this) }
        }
    }
}





@Composable
fun <T> ConsumeNonSuspendedUIEffects(
    uiEvents: Flow<T>,
    shouldBeLifecycleAware: Boolean = false,
    consumer: (T) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val scope = rememberCoroutineScope()

    DisposableEffect(uiEvents) {
        val job = if (shouldBeLifecycleAware) {
            uiEvents
                .onEach { consumer(it) }
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .launchIn(lifecycle.coroutineScope)
        } else {
            uiEvents
                .onEach { consumer(it) }
                .launchIn(scope)
        }

        onDispose { job.cancel() }
    }
}


@Composable
fun <T> ConsumeSuspendUIEffects(
    uiEvents: Flow<T>,
    shouldBeLifecycleAware: Boolean = false,
    consumer: suspend (T, CoroutineScope) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(uiEvents) {
        if (shouldBeLifecycleAware) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                uiEvents.collectLatest { consumer(it, this) }
            }
        } else {
            uiEvents.collectLatest { consumer(it, this) }
        }
    }
}

package com.gmurari.pokemon.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * La funzione si occupa di eseguire la collect dai channel flow, assicurandosi che questa venga
 * interrotta se l'applicazione non è in stato STARTED senza tuttavia perdere degli eventi,
 * cosa che potrebbe capitare se l'app viene ruotata e l'evento viene emesso tra l'onDestroy e
 * l'onCreate
 *
 * @param flow Flow di tipo channel
 * @param onEvent lambda che viene eseguita quando viene eseguito il collect di un evento
 */
@Composable
fun <T> ObserveAsEvents(flow: Flow<T>, onEvent: (T) -> Unit) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    LaunchedEffect(flow, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}
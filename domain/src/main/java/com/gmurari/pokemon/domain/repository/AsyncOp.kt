package com.gmurari.pokemon.domain.repository

sealed interface AsyncOp<out T> {
    data class Success<T>(val data: T): AsyncOp<T>
    data class Error<T>(val error: Throwable, val data: T? = null): AsyncOp<T>
    data class Loading<T>(val isLoading: Boolean = true, val progress: Percentage): AsyncOp<T>
}
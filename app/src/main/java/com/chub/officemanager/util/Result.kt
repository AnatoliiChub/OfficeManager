package com.chub.officemanager.util

sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val errorMessage: ErrorMessage) : Result<Nothing>()
}

enum class ErrorMessage {
    NONE,
    ITEM_IS_ALREADY_RELATED,
    ITEM_HAS_BEEN_ALREADY_ADDED,
    UNKNOWN_ERROR
}
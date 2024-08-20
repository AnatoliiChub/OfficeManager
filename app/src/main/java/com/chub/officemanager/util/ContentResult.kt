package com.chub.officemanager.util

sealed class ContentResult<out T> {
    data object Loading : ContentResult<Nothing>()
    data class Success<out T>(val data: T) : ContentResult<T>()
}
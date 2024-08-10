package com.chub.officemanager.domain

sealed class Result<out T> {
    data object Loading : Result<Nothing>()

    data class Error(val errorMessage: String) : Result<Nothing>()

    data class Success<out T>(val data: T) : Result<T>()
}
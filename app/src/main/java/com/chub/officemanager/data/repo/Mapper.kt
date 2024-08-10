package com.chub.officemanager.data.repo


interface Mapper<T, R> {
    fun map(input: T): R
}
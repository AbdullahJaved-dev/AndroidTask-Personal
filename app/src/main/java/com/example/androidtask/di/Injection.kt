package com.emedinaa.kotlinmvvm.di


import com.example.androidtask.model.UsersDataSource
import com.example.androidtask.model.UsersRepository

object Injection {
    private val usersRepository = UsersRepository()
    fun providerRepository():UsersDataSource= usersRepository
}
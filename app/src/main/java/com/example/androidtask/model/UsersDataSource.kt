package com.example.androidtask.model

import com.example.androidtask.data.OperationResult

interface UsersDataSource {
    suspend fun retrieveUsers(): OperationResult<User>
}
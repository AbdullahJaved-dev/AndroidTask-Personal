package com.example.androidtask.model

import com.example.androidtask.data.ApiClient
import com.example.androidtask.data.OperationResult

class UsersRepository : UsersDataSource {

    override suspend fun retrieveUsers(): OperationResult<User> {
        val response = ApiClient.build()?.users()

        try {
            response?.let {
                return if (it.isSuccessful && it.body() != null) {
                    val data = it.body()
                    OperationResult.Success(data)
                } else {
                    OperationResult.Error(Exception("Unable to load"))
                }
            } ?: run {
                return OperationResult.Error(Exception("An error occoured"))
            }
        } catch (e: Exception) {
            return OperationResult.Error(e)
        }
    }
}
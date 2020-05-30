package com.example.androidtask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtask.data.OperationResult
import com.example.androidtask.model.User
import com.example.androidtask.model.UsersDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val repository: UsersDataSource): ViewModel() {

    private val _users = MutableLiveData<List<User>>().apply { value = emptyList() }
    val users: LiveData<List<User>> = _users

    private val _isViewLoading= MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _onMessageError= MutableLiveData<Any>()
    val onMessageError: LiveData<Any> = _onMessageError

    private val _isEmptyList= MutableLiveData<Boolean>()
    val isEmptyList: LiveData<Boolean> = _isEmptyList


    fun cancel(){
        viewModelScope.cancel()
    }

    fun loadUsers(){
        _isViewLoading.postValue(true)
        viewModelScope.launch {
            var  result: OperationResult<User> = withContext(Dispatchers.IO){
                repository.retrieveUsers()
            }
            _isViewLoading.postValue(false)
            when(result){
                is OperationResult.Success ->{
                    if(result.data.isNullOrEmpty()){
                        _isEmptyList.postValue(true)
                    }else{
                        _users.value = result.data
                    }
                }
                is OperationResult.Error ->{
                    _onMessageError.postValue(result.exception)

                }
            }
        }
    }
}
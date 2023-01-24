package com.ktor.chat.presentation.users

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ktor.chat.data.remote.ListOfUsers
import com.ktor.chat.data.remote.dto.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class getUsersViewModel @Inject constructor(
    private val listOfUsers: ListOfUsers
): ViewModel(){

    private val _getUsers = MutableSharedFlow<List<UserInfo>>()
    val getUsers= _getUsers.asSharedFlow()
    fun getUsers(from: String?){
        viewModelScope.launch {
            println("Get Users Called !!!")
            _getUsers.emit(listOfUsers.getAllUsers(from))
        }
    }
}
package com.ktor.chat.presentation.username

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserNameViewModel @Inject constructor(): ViewModel(){

    private val _userName = mutableStateOf("")
    val userName: State<String> = _userName

    private val _onJoinChat = MutableSharedFlow<String>()
    val joinChat= _onJoinChat.asSharedFlow()

    fun onUserNameChange(userName: String){
        _userName.value = userName
    }

    fun onJoinClick() {
        viewModelScope.launch {
            println("userrrrrr = ${userName.value}")
            if (userName.value.isNotBlank()) {
                println("userrrrrr = ${userName.value}")
                _onJoinChat.emit(userName.value)
            }
        }
    }




}
package com.ktor.chat.presentation.p2p.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktor.chat.data.remote.P2PMesService
import com.ktor.chat.data.remote.P2PSession
import com.ktor.chat.presentation.chat.P2PChatMessage
import com.ktor.chat.presentation.chat.P2PLocationMessage
import com.ktor.chat.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class P2PViewModel @Inject constructor(
    private val pMesService: P2PMesService,
    private val p2PSession: P2PSession,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _messageText = mutableStateOf("")
    val messageText: State<String> = _messageText

    private val _chatState = mutableStateOf(P2PChatMessage())
    val chatState: State<P2PChatMessage> = _chatState

    private val _locationState = mutableStateOf(P2PLocationMessage())
    val locationState: State<P2PLocationMessage> = _locationState

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    fun connectToChat(from: String?, to: String?) {
//        getAllMessages(from, to)

        viewModelScope.launch {
            val result = p2PSession.initSession(from, to)
            println("P2P result is ${result}")
            when (result) {
                is Resource.Success -> {
//                    if (!_messageText.value.contains("latitude=#")) {
                        p2PSession.getAllMessage()
                            .onEach { message ->
                                val newList = chatState.value.message.toMutableList().apply {
                                    add(0, message)
                                }
                                _chatState.value = chatState.value.copy(
                                    message = newList
                                )
                            }.launchIn(viewModelScope)
//                    } else {
                        println("P2P location is ${p2PSession.getLocation().toList()}")
//                        p2PSession.getLocation().onEach { locationMessage ->
//                            val newList = chatState.value.message.toMutableList().apply {
//                                add(0, locationMessage)
//                            }
//                            _chatState.value = chatState.value.copy(
//                                message = newList
//                            )
//                        }
//                    }
                }
                is Resource.Failure -> {
                    _toastEvent.emit(result.message ?: "Unknown error")
                }
            }
        }


    }

    fun sendLocation(){
        viewModelScope.launch {
            p2PSession.sendLocation(messageText.value)
        }
    }


    fun sendMessage() {
        viewModelScope.launch {
            p2PSession.sendMessage(messageText.value)
        }
    }

    fun getAllMessages(from: String?, to: String?) {
        viewModelScope.launch {
            _chatState.value = chatState.value.copy(
                isLoading = true
            )
            val result = pMesService.getAllMessages(from, to)
//            _chatState.value = chatState.value.copy(
//                message = result,
//                isLoading = true
//            )
        }
    }

    fun onMessageChange(message: String) {
        _messageText.value = message
    }

    fun disconnect() {
        viewModelScope.launch {
            p2PSession.closeSession()
        }
    }
}
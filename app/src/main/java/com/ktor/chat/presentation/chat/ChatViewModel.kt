package com.ktor.chat.presentation.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktor.chat.data.remote.ChatSocketSession
import com.ktor.chat.data.remote.MessageService
import com.ktor.chat.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageService: MessageService,
    private val chatSocketSession: ChatSocketSession,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _messageText = mutableStateOf("")
    val messageText: State<String> = _messageText

    private val _chatState = mutableStateOf(ChatMessage())
    val chatState: State<ChatMessage> = _chatState

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    fun connectToChat() {
        getAllMessages()
        savedStateHandle.get<String>("username")?.let { username ->
            viewModelScope.launch {
                val result = chatSocketSession.initSession(username)
                when(result) {
                    is Resource.Success -> {
                        chatSocketSession.getAllMessage()
                            .onEach { message ->
                                val newList = chatState.value.message.toMutableList().apply {
                                    add(0, message)
                                }
                                _chatState.value = chatState.value.copy(
                                    message = newList
                                )
                            }.launchIn(viewModelScope)
                    }
                    is Resource.Failure -> {
                        _toastEvent.emit(result.message ?: "Unknown error")
                    }
                }
            }
        }
    }

    fun onMessageChange(message: String) {
        _messageText.value = message
    }

    fun disconnect() {
        viewModelScope.launch {
            chatSocketSession.closeSession()
        }
    }

    fun getAllMessages() {
        viewModelScope.launch {
            _chatState.value = chatState.value.copy(
                isLoading = true
            )
            val result = messageService.getAllMessages()
            _chatState.value = chatState.value.copy(
                message = result,
                isLoading = false
            )
        }
    }

    fun sendMessage() {
        viewModelScope.launch {
            if (messageText.value.isNotBlank()) {
                chatSocketSession.sendMessage(messageText.value)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }

}
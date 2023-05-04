package com.ktor.chat.presentation.users

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ktor.chat.data.remote.ListOfUsers
import com.ktor.chat.data.remote.dto.AllStoriesItem
import com.ktor.chat.data.remote.dto.OneStoryItem
import com.ktor.chat.data.remote.dto.UserInfo
import com.ktor.chat.presentation.UriPathFinder
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class getUsersViewModel @Inject constructor(
    private val listOfUsers: ListOfUsers
): ViewModel(){

    private val _getUsers = MutableSharedFlow<List<UserInfo>>()
    val getUsers= _getUsers.asSharedFlow()
    var listOfStories : MutableState<MutableList<AllStoriesItem>> = mutableStateOf(mutableListOf())
    var userStory : MutableState<MutableList<OneStoryItem>> = mutableStateOf(mutableListOf())
    fun getUsers(from: String?){
        viewModelScope.launch {
            println("Get Users Called !!!")
            _getUsers.emit(listOfUsers.getAllUsers(from))
        }
    }
    fun addImage(file: File){
        viewModelScope.launch {
            listOfUsers.sendImage(file)
        }
    }

    fun getOneStory(username: String) {
        viewModelScope.launch {
            userStory.value =  listOfUsers.getUserStory(username).toMutableList()
        }
    }

    fun getAllStories(username: String): MutableList<AllStoriesItem>{
        viewModelScope.launch {
            listOfStories.value = listOfUsers.getAllStories(username).toList().toMutableList()
        }
        return listOfStories.value
    }

    private fun changeUriToPath(uris: Uri, context: Context) = UriPathFinder().getPath(context, uris)

    fun onFilePathsListChange(list: Uri, context: Context): String? = changeUriToPath(list, context)
}
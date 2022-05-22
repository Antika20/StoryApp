package com.example.storyapp.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.Repository.RepositoryStory
import com.example.storyapp.Repository.StoryRepo
import com.example.storyapp.body.RegisterBody
import com.example.storyapp.helper.ShowState

import kotlinx.coroutines.launch

class SignupViewModel(private val repositoryStory: RepositoryStory) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun signup(
        registerBody: RegisterBody,
        onRegistered: (isSuccess: Boolean, message: String) -> Unit
    ) = viewModelScope.launch {
        repositoryStory.register(registerBody).collect {
            when (it) {
                is ShowState.Loading -> {
                    _isLoading.value = true
                }
                is ShowState.Success -> {
                    _isLoading.value = false
                    onRegistered(true, "Register Success: ${it.data}")
                }
                is ShowState.Failed -> {
                    _isLoading.value = false
                    onRegistered(false, "Register Failed: ${it.message}")
                }
            }
        }
    }
}
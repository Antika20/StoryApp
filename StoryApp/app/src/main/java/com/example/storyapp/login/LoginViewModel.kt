package com.example.storyapp.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.Repository.RepositoryStory

import com.example.storyapp.body.LoginBody
import com.example.storyapp.helper.ShowState
import com.example.storyapp.model.LoginResult

import kotlinx.coroutines.launch

class LoginViewModel(private val repositoryStory: RepositoryStory) :ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(context: Context, loginBody: LoginBody, onSuccess: (LoginResult) -> Unit) =
        viewModelScope.launch {
            repositoryStory.login(loginBody).collect {
                when (it) {
                    is ShowState.Loading -> {
                        _isLoading.value = true
                    }
                    is ShowState.Success -> {
                        _isLoading.value = false
                        Toast.makeText(context, "Welcome ${it.data.name}", Toast.LENGTH_SHORT)
                            .show()
                        onSuccess(it.data)
                    }
                    is ShowState.Failed -> {
                        _isLoading.value = false
                        Toast.makeText(context, "Login Failed, ${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
}
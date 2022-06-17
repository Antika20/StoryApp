package com.example.storyapp.main

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.Repository.RepositoryStory
import com.example.storyapp.helper.ShowState
import com.example.storyapp.local.LocalData
import com.example.storyapp.model.LoginResult
import com.example.storyapp.model.storyResult
import kotlinx.coroutines.launch

class MainViewModel(private val repositoryStory: RepositoryStory): ViewModel() {

    private var _stories = MutableLiveData<List<storyResult>>()
    val stories get() = _stories

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage



    fun getAllStories(token: String) = viewModelScope.launch {
        repositoryStory.getAllStory(token).collect {
            when (it) {
                is ShowState.Loading -> {
                    _isLoading.value = true
                }
                is ShowState.Success -> {
                    _isLoading.value = false
                    it.data.let { story -> _stories.value = story }
                }
                is ShowState.Failed -> {
                    _isLoading.value = false
                    _errorMessage.value = "Gagal ngambil data =>  $it"
                    Log.d("TAG", "getAllStories: failed = $it")
                }
            }
        }
    }


}
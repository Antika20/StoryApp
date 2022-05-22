package com.example.storyapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.Repository.RepositoryStory
import com.example.storyapp.helper.ShowState
import com.example.storyapp.model.storyResult
import kotlinx.coroutines.launch

class MainViewModel(private val repositoryStory: RepositoryStory) : ViewModel() {

     private lateinit var _stories: LiveData<PagingData<storyResult>>
    val stories get() = _stories

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _googleMapsData = MutableLiveData<List<storyResult>>()
    val googleMapsData: LiveData<List<storyResult>> = _googleMapsData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    fun getallStories(token: String) = viewModelScope.launch {
        _stories = repositoryStory.getAllStory(token)
    }


    suspend fun getAllStoriesWithlocation(token: String) = viewModelScope.launch {
        repositoryStory.getAllStoryWithLocation(token).collect {
            when (it) {
                is ShowState.Loading -> {
                    _isLoading.value = true
                }
                is ShowState.Success -> {
                    _isLoading.value = false
                    it.data.let { story -> _googleMapsData.value = story }
                }
                is ShowState.Failed -> {
                    _isLoading.value = false
                    _errorMessage.value = it.message
                }
            }
        }
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}
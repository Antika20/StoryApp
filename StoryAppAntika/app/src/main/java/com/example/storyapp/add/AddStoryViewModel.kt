package com.example.storyapp.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.Repository.RepositoryStory
import com.example.storyapp.helper.ShowState
import com.example.storyapp.model.storyResult
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryViewModel(private val repositoryStory: RepositoryStory) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun postStory(
        token: String,
        file: MultipartBody.Part,
        desc: String,
        onStoryPosted: (isPosted: Boolean, message: String  ) -> Unit
    ) {
        viewModelScope.launch {
            val description = desc.toRequestBody("text/plain".toMediaType())
            repositoryStory.postStory(token, file, description).collect {
                when (it) {
                    is ShowState.Loading -> {
                        _isLoading.value = true
                    }
                    is ShowState.Success ->{
                        _isLoading.value = false
                        onStoryPosted(true, it.data)
                        Log.d("TAG", "postStory: ${it.data}")
                    }
                    is ShowState.Failed ->{
                        _isLoading.value = false
                        onStoryPosted(false, it.message)
                    }
                }
            }
        }
    }
}
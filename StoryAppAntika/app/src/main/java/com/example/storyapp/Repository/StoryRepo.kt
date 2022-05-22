package com.example.storyapp.Repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.storyapp.body.LoginBody
import com.example.storyapp.body.RegisterBody
import com.example.storyapp.helper.ShowState
import com.example.storyapp.model.LoginResult
import com.example.storyapp.model.storyResult
import com.example.storyapp.response.RegisterResponse
import com.example.storyapp.response.StoryResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryRepo {
    suspend fun login(loginBody: LoginBody): Flow<ShowState<LoginResult>>
    suspend fun register(registerBody: RegisterBody): Flow<ShowState<String>>
    fun getAllStory(token: String): LiveData<PagingData<storyResult>>
    suspend fun getAllStoryWithLocation(token: String): Flow<ShowState<List<storyResult>>>
    suspend fun postStory(token: String, file: MultipartBody.Part, description: RequestBody): Flow<ShowState<String>>
}
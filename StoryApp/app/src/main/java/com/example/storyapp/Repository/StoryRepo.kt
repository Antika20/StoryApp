package com.example.storyapp.Repository

import com.example.storyapp.body.LoginBody
import com.example.storyapp.body.RegisterBody
import com.example.storyapp.helper.ShowState
import com.example.storyapp.model.LoginResult
import com.example.storyapp.model.storyResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryRepo {
    suspend fun login(loginBody: LoginBody): Flow<ShowState<LoginResult>>
    suspend fun register(registerBody: RegisterBody): Flow<ShowState<String>>
    suspend fun getAllStory(token: String): Flow<ShowState<List<storyResult>>>
    suspend fun postStory(token: String, file: MultipartBody.Part, description: RequestBody): Flow<ShowState<String>>
}
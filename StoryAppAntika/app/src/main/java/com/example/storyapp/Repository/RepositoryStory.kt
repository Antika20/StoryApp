package com.example.storyapp.Repository

import androidx.paging.*
import com.example.storyapp.body.LoginBody
import com.example.storyapp.body.RegisterBody
import com.example.storyapp.helper.ShowState
import com.example.storyapp.model.LoginResult
import com.example.storyapp.model.storyResult
import com.example.storyapp.remote.StoryPagingSource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RepositoryStory ( private val remoteData: RemoteData):StoryRepo{

    override suspend fun login(loginBody: LoginBody): Flow<ShowState<LoginResult>> =
        remoteData.login(loginBody)

    override suspend fun register(registerBody: RegisterBody): Flow<ShowState<String>> =
        remoteData.register(registerBody)

    override fun getAllStory(token:String) = Pager(
        config = PagingConfig(pageSize = 5),
        pagingSourceFactory = { StoryPagingSource(token,remoteData)}
    ).liveData

    override suspend fun getAllStoryWithLocation(token: String):
            Flow<ShowState<List<storyResult>>> = remoteData.getAllStoryWithLocation(token)

    override suspend fun postStory(
        token: String,
        file: MultipartBody.Part, description: RequestBody
    ): Flow<ShowState<String>>  = remoteData.postStory(token, file, description)
}

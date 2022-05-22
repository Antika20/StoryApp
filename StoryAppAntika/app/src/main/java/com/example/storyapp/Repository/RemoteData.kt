package com.example.storyapp.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.body.LoginBody
import com.example.storyapp.body.RegisterBody
import com.example.storyapp.helper.ShowState
import com.example.storyapp.model.LoginResult
import com.example.storyapp.model.storyResult
import com.example.storyapp.remote.StoryPagingSource
import com.example.storyapp.response.ListStoryItem
import com.example.storyapp.response.Stories
import com.example.storyapp.response.StoryResponse
import com.example.storyapp.response.toLoginResult
import com.example.storyapp.retrovit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RemoteData(private val apiService: ApiService) {

    companion object {
        const val TAG = "RemoteData"
    }

    suspend fun login(loginBody: LoginBody) = flow<ShowState<LoginResult>> {
        emit(ShowState.loading())
        val response = apiService.login(loginBody)
        response.let {
            if (it.error != true) emit(ShowState.success(it.loginRespon.toLoginResult()))
            else emit(ShowState.failed(it.message ?: ""))
        }

    }.catch {
        Log.d(TAG, "login: failed = ${it.message}")
        emit(ShowState.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    suspend fun register(registerBody: RegisterBody) = flow<ShowState<String>> {
        emit(ShowState.loading())
        val response = apiService.register(registerBody)
        response.let {
            if (it.error != true) emit(ShowState.success(it.message ?: ""))
            else emit(ShowState.failed(it.message ?: ""))
        }
    }.catch {
        Log.d(TAG, "register: failed = ${it.message}")
        emit(ShowState.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    suspend fun getAllStoryWithLocation(token: String) = flow<ShowState<List<storyResult>>> {
        emit(ShowState.loading())
        val response = apiService.getAllStoryWithLocation(token,1,20)
        response.let {
            if (it.error != true) emit(ShowState.success(it.listStory.Stories()))
            else emit(ShowState.failed(it.message?: ""))
        }
    }

    suspend fun  getAllStory(token: String,page:Int,size:Int) = apiService.getAllStory(token,page, size)

//    fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 5
//            ),
//            pagingSourceFactory = {
//               StoryPagingSource(apiService, token)
//            }
//        ).liveData
//    }


//    suspend fun getAllStory(token: String) = flow<ShowState<List<storyResult>>> {
//        emit(ShowState.loading())
//        val response = apiService.getAllStory(token)
//        response.let {
//            Log.d(TAG, "getAllStory: $it")
//            if (it.error != true) emit(ShowState.Success(it.listStory?.Stories() ?: listOf()))
//            else emit(ShowState.failed(it.message ?: ""))
//        }
//    }.catch {
//        Log.d(TAG, "getAllStory: failed = ${it.message}")
//        emit(ShowState.failed(it.message ?: ""))
//    }.flowOn(Dispatchers.IO)

    suspend fun postStory(token: String, file: MultipartBody.Part, description: RequestBody) = flow<ShowState<String>> {
        emit(ShowState.loading())
        val response = apiService.postStory(token,file,description)
        response.let {
            if (it.error != true) emit(ShowState.success(it.message ?: ""))
            else emit(ShowState.failed(it.message ?: ""))
        }
    }.catch {
        Log.d(TAG, "getAllStory: failed = ${it.message}")
        emit(ShowState.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

}

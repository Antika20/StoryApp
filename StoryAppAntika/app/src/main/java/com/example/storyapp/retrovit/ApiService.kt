package com.example.storyapp.retrovit

import com.example.storyapp.body.LoginBody
import com.example.storyapp.body.RegisterBody
import com.example.storyapp.response.LoginResponse
import com.example.storyapp.response.RegisterResponse
import com.example.storyapp.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @POST("login")
    suspend fun login(@Body loginBody: LoginBody): LoginResponse

    @POST("register")
    suspend fun register(@Body registerBody: RegisterBody): RegisterResponse

    @GET("stories")
    suspend fun getAllStory(
        @Header("Authorization") header: String,
        @Query("page") page:Int,
        @Query("size") size:Int,
        @Query("location") location: Int = 0
    ): StoryResponse

    @GET("stories")
    suspend fun getAllStoryWithLocation(
        @Header("Authorization") header: String,
        @Query("page") page:Int,
        @Query("size") size:Int,
        @Query("location") location: Int = 1
    ): StoryResponse


    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Header("Authorization") header: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): RegisterResponse
}
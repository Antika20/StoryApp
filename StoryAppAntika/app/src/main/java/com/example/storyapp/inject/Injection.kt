package com.example.storyapp.inject


import android.content.Context
import com.example.storyapp.Repository.RemoteData
import com.example.storyapp.Repository.RepositoryStory
import com.example.storyapp.retrovit.ApiConfig


object injection {
    fun provideUser (context: Context): RepositoryStory {
        val apiService  = ApiConfig.getApiService()
        val remoteData = RemoteData(apiService)
        return RepositoryStory(remoteData)
    }
}
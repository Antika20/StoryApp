package com.example.storyapp.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.storyapp.model.LoginResult
import com.example.storyapp.retrovit.ApiConfig.userData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow


class LocalData constructor(private val context: Context) {
    private val KEY_TOKEN = stringPreferencesKey("token_key")
    private val KEY_USER_ID = stringPreferencesKey("user_id_key")
    private val KEY_NAME = stringPreferencesKey("name_key")

    suspend fun getLogin() = flow {
        val token = context.userData.data.first()[KEY_TOKEN] ?: ""
        val userId = context.userData.data.first()[KEY_USER_ID] ?: ""
        val name = context.userData.data.first()[KEY_NAME] ?: ""
        emit(LoginResult(name, userId, token))
    }

    suspend fun putLoginIn(loginResult: LoginResult) {
        context.userData.edit {it [KEY_TOKEN] =loginResult.token}
        context.userData.edit { it[KEY_USER_ID] = loginResult.userId }
        context.userData.edit { it[KEY_NAME] = loginResult.name }
    }
}
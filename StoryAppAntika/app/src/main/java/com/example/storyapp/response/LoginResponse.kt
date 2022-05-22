package com.example.storyapp.response

import com.example.storyapp.model.LoginResult
import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("loginResult")
	val loginRespon: LoginRespon? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class LoginRespon(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)

fun LoginRespon?.toLoginResult() = LoginResult(
	this?.name ?: "",
	this?.userId ?: "",
	this?.token ?: ""
)
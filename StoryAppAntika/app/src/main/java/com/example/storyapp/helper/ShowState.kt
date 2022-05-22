package com.example.storyapp.helper



sealed class ShowState<T> {
    class Loading<T> : ShowState<T>()
    data class Success<T>(val data: T) : ShowState<T>()
    data class Failed<T>(val message: String) : ShowState<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: String) = Failed<T>(message)
    }

}
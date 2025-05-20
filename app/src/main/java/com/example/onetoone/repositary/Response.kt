package com.example.onetoone.repositary

sealed class Response<T>(val data : T? = null, val errorMessage: String? = null) {

    class Loading<T>(data: T? = null) : Response<T>(data = data)
    class Success<T>(data: T? = null) : Response<T>(data = data)
    class Error<T>(errorMessage: String) : Response<T>(errorMessage = errorMessage)
}
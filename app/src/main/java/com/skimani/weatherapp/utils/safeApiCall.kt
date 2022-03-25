package com.skimani.weatherapp.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.skimani.weatherapp.network.datasource.NetworkResult
import com.skimani.weatherapp.network.models.ErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

suspend fun <T : Any> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                NetworkResult.Success(response.body()!!)
            } else {
                val gson = Gson()
                val type = object : TypeToken<ErrorResponse>() {}.type
                val errorResponse: ErrorResponse? =
                    gson.fromJson(response.errorBody()!!.charStream(), type)
                NetworkResult.Error(errorResponse?.errorMessage ?: "Something went wrong")
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    NetworkResult.NetworkError("${throwable.code()} ${throwable.message()}?: Network error ")
                }
                else -> {
                    NetworkResult.Error("${throwable.message}?: Error occurred ${throwable.localizedMessage}")
                }
            }
        }
    }
}

package com.buddy.growfarm.utils

import androidx.lifecycle.MutableLiveData
import org.json.JSONObject
import retrofit2.Response

class NetworkResponseHandler {
    companion object {
        fun <T> handleResponse(response: Response<T>, liveData: MutableLiveData<NetworkResult<T>>) {
            if (response.isSuccessful && response.body() != null) {
                liveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                liveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                liveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        }
    }
}
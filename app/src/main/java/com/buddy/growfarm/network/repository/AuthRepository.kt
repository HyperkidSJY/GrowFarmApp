package com.buddy.growfarm.network.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buddy.growfarm.network.api.GrowFarmAPI
import com.buddy.growfarm.network.dto.LoginReqBody
import com.buddy.growfarm.network.dto.PricesReqBody
import com.buddy.growfarm.network.dto.RegisterReqBody
import com.buddy.growfarm.network.dto.UserResponse
import com.buddy.growfarm.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val growFarmAPI: GrowFarmAPI) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    suspend fun userSignIn(phoneNumber: String, password: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val login = LoginReqBody(phoneNumber, password)
        val response = growFarmAPI.postUserSignIn(login)
        handleResponse(response)
    }

    suspend fun userSignUp(
        phoneNumber: String,
        password: String,
        gmail: String,
        name: String
    ) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val register = RegisterReqBody(phoneNumber, password, gmail, name)
        val response = growFarmAPI.postUserSignUp(register)
        handleResponse(response)
    }


    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
}
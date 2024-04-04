package com.buddy.growfarm.network.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buddy.growfarm.network.api.GrowFarmAPI
import com.buddy.growfarm.network.dto.LoginReqBody
import com.buddy.growfarm.network.dto.OtpReqBody
import com.buddy.growfarm.network.dto.OtpResponse
import com.buddy.growfarm.network.dto.RegisterReqBody
import com.buddy.growfarm.network.dto.UserResponse
import com.buddy.growfarm.network.dto.VerifyOtpReqBody
import com.buddy.growfarm.utils.NetworkResponseHandler.Companion.handleResponse
import com.buddy.growfarm.utils.NetworkResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class AuthRepository @Inject constructor(private val growFarmAPI: GrowFarmAPI) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    private val _otpResponseLiveData = MutableLiveData<NetworkResult<OtpResponse>>()
    val otpResponseLiveData: LiveData<NetworkResult<OtpResponse>>
        get() = _otpResponseLiveData

    private val _setProfileLiveData = MutableLiveData<NetworkResult<OtpResponse>>()
    val setProfileLiveData: LiveData<NetworkResult<OtpResponse>>
        get() = _setProfileLiveData

    suspend fun userSignIn(phoneNumber: String, password: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val login = LoginReqBody(phoneNumber, password)
        val response = growFarmAPI.postUserSignIn(login)
        handleResponse(response, _userResponseLiveData)
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
        handleResponse(response, _userResponseLiveData)
    }


    suspend fun sendOTP(phoneNumber: String) {
        val otpReqBody = OtpReqBody(phoneNumber)
        growFarmAPI.sendOtp(otpReqBody)
    }

    suspend fun verifyOTP(phoneNumber: String, otp: String) {
        val verifyOtpReqBody = VerifyOtpReqBody(phoneNumber, otp)
        val response = growFarmAPI.verifyOTP(verifyOtpReqBody)
        handleResponse(response, _otpResponseLiveData)
    }

    suspend fun setupProfile(
        _id: String,
        roles: String,
        pincode: String,
        gender: String,
        file: File
    ) {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imageBody = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val genderPart = gender.toRequestBody("text/plain".toMediaTypeOrNull())
        val rolePart = roles.toRequestBody("text/plain".toMediaTypeOrNull())
        val pincodePart = pincode.toRequestBody("text/plain".toMediaTypeOrNull())
        val idPart = _id.toRequestBody("text/plain".toMediaTypeOrNull())


        val response =
            growFarmAPI.setupProfile(idPart, rolePart, pincodePart, genderPart, imageBody)
        handleResponse(response, _setProfileLiveData)
    }
}
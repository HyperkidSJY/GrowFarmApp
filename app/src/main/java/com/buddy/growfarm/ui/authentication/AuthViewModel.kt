package com.buddy.growfarm.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buddy.growfarm.network.dto.OtpResponse
import com.buddy.growfarm.network.dto.SetProfileResponse
import com.buddy.growfarm.network.dto.UserResponse
import com.buddy.growfarm.network.repository.AuthRepository
import com.buddy.growfarm.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class AuthViewModel@Inject constructor(private val authRepository: AuthRepository ) : ViewModel(){

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = authRepository.userResponseLiveData

    val otpResponseLiveData : LiveData<NetworkResult<OtpResponse>>
        get() = authRepository.otpResponseLiveData

    val setProfileLiveData : LiveData<NetworkResult<OtpResponse>>
        get() = authRepository.setProfileLiveData
    fun loginUser(phoneNumber: String, password: String) {
        viewModelScope.launch {
            authRepository.userSignIn(phoneNumber, password)
        }
    }

    fun registerUser(phoneNumber: String,password: String,gmail: String,name: String){
        viewModelScope.launch {
            authRepository.userSignUp(phoneNumber, password, gmail, name)
        }
    }

    fun sendOTP(phoneNumber: String){
        viewModelScope.launch {
            authRepository.sendOTP(phoneNumber)
        }
    }

    fun verifyOTP(phoneNumber: String,otp: String){
        viewModelScope.launch {
            authRepository.verifyOTP(phoneNumber,otp)
        }
    }

    fun setupProfile(_id : String,roles : String,pincode : String , gender : String , file : File){
        viewModelScope.launch {
            authRepository.setupProfile(_id,roles,pincode,gender,file)
        }
    }
}
package com.buddy.growfarm.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buddy.growfarm.network.dto.UserResponse
import com.buddy.growfarm.network.repository.AuthRepository
import com.buddy.growfarm.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel@Inject constructor(private val authRepository: AuthRepository ) : ViewModel(){

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = authRepository.userResponseLiveData

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

}
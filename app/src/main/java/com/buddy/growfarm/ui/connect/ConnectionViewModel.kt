package com.buddy.growfarm.ui.connect

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buddy.growfarm.network.dto.GetConnectionsResponse
import com.buddy.growfarm.network.dto.OtpResponse
import com.buddy.growfarm.network.repository.ConnectionRepository
import com.buddy.growfarm.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(private val connectionRepository: ConnectionRepository) :
    ViewModel() {

    val getConnectionLiveData: LiveData<NetworkResult<GetConnectionsResponse>>
        get() = connectionRepository.getConnectionsResponse

    val followUserLiveData : LiveData<OtpResponse>
        get() = connectionRepository.followUser

    fun getConnections(id : String){
        viewModelScope.launch {
            connectionRepository.getConnections(id)
        }
    }

    fun followUser(connectId : String , userId : String){
        viewModelScope.launch {
            connectionRepository.followUser(connectId,userId)
            getConnections(userId)
        }
    }

    fun getFollowers(id : String){
        viewModelScope.launch {
            connectionRepository.getFollowers(id)
        }
    }

    fun getFollowing(id : String){
        viewModelScope.launch {
            connectionRepository.getFollowing(id)
        }
    }

    fun unfollowUser(connectId : String , userId : String){
        viewModelScope.launch {
            connectionRepository.unfollowUser(connectId,userId)
            getFollowing(userId)
        }
    }

}
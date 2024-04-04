package com.buddy.growfarm.network.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buddy.growfarm.network.api.GrowFarmAPI
import com.buddy.growfarm.network.dto.GetConnectionsResponse
import com.buddy.growfarm.network.dto.OtpResponse
import com.buddy.growfarm.network.dto.UserIdReqBody
import com.buddy.growfarm.utils.NetworkResponseHandler.Companion.handleResponse
import com.buddy.growfarm.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class ConnectionRepository @Inject constructor(private val growFarmAPI: GrowFarmAPI) {

    private val _getConnectionResLiveData = MutableLiveData<NetworkResult<GetConnectionsResponse>>()
    val getConnectionsResponse: LiveData<NetworkResult<GetConnectionsResponse>>
        get() = _getConnectionResLiveData

    private val _followUser = MutableLiveData<OtpResponse>()
    val followUser: LiveData<OtpResponse>
        get() = _followUser

    suspend fun getConnections(id: String) {
        _getConnectionResLiveData.postValue(NetworkResult.Loading())
        val response = growFarmAPI.getConnections(id)
        handleResponse(response,_getConnectionResLiveData)
    }

    suspend fun followUser(connectId: String, userId: String) {
        val response = growFarmAPI.followUser(connectId, UserIdReqBody(userId))
        _followUser.postValue(response.body())
    }

    suspend fun getFollowers(id: String) {
        _getConnectionResLiveData.postValue(NetworkResult.Loading())
        val response = growFarmAPI.getFollowers(id)
        handleResponse(response,_getConnectionResLiveData)
    }

    suspend fun getFollowing(id: String) {
        _getConnectionResLiveData.postValue(NetworkResult.Loading())
        val response = growFarmAPI.getFollowing(id)
        Log.e("following", response.body().toString())
        handleResponse(response,_getConnectionResLiveData)
    }

    suspend fun unfollowUser(connectId: String, userId: String) {
        val response = growFarmAPI.unfollowUser(connectId, UserIdReqBody(userId))
        _followUser.postValue(response.body())
    }

}
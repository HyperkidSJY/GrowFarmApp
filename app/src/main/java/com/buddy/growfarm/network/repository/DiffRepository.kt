package com.buddy.growfarm.network.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buddy.growfarm.network.api.GrowFarmAPI
import com.buddy.growfarm.network.dto.PricesReqBody
import com.buddy.growfarm.network.dto.PricesResBody
import com.buddy.growfarm.network.dto.UserResponse
import com.buddy.growfarm.utils.NetworkResponseHandler.Companion.handleResponse
import com.buddy.growfarm.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class DiffRepository@Inject constructor(private val growFarmAPI : GrowFarmAPI) {

    private val _recordPricesLiveData = MutableLiveData<NetworkResult<PricesResBody>>()
    val recordPricesLiveData : LiveData<NetworkResult<PricesResBody>>
        get() = _recordPricesLiveData

    suspend fun getPrices(pincode : String){
        _recordPricesLiveData.postValue(NetworkResult.Loading())
        val response = growFarmAPI.getPrices(pincode)
        handleResponse(response,_recordPricesLiveData)
    }
}
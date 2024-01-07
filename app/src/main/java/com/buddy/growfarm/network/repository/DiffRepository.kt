package com.buddy.growfarm.network.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buddy.growfarm.network.api.GrowFarmAPI
import com.buddy.growfarm.network.dto.PricesReqBody
import com.buddy.growfarm.network.dto.PricesResBody
import com.buddy.growfarm.network.dto.UserResponse
import com.buddy.growfarm.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class DiffRepository@Inject constructor(private val growFarmAPI : GrowFarmAPI) {

    private val _recordPricesLiveData = MutableLiveData<NetworkResult<PricesResBody>>()
    val recordPricesLiveData : LiveData<NetworkResult<PricesResBody>>
        get() = _recordPricesLiveData

    suspend fun getPrices(state : String, district : String){
        _recordPricesLiveData.postValue(NetworkResult.Loading())
        val price = PricesReqBody(state,district)
        val response = growFarmAPI.getPrices(price)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<PricesResBody>) {
        if (response.isSuccessful && response.body() != null) {
            _recordPricesLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _recordPricesLiveData.postValue(NetworkResult.Error(errorObj.getString("error")))
        } else {
            _recordPricesLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
}
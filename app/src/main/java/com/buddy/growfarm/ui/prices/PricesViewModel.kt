package com.buddy.growfarm.ui.prices

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buddy.growfarm.network.dto.PricesResBody
import com.buddy.growfarm.network.repository.DiffRepository
import com.buddy.growfarm.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PricesViewModel@Inject constructor(private val diffRepository: DiffRepository) : ViewModel() {

    val pricesResponseLiveData : LiveData<NetworkResult<PricesResBody>>
        get() = diffRepository.recordPricesLiveData

    fun getPrices(pincode : String){
        viewModelScope.launch {
            diffRepository.getPrices(pincode)
        }
    }
}
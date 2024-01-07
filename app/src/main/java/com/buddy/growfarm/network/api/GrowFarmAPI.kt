package com.buddy.growfarm.network.api


import com.buddy.growfarm.network.dto.LoginReqBody
import com.buddy.growfarm.network.dto.PricesReqBody
import com.buddy.growfarm.network.dto.PricesResBody
import com.buddy.growfarm.network.dto.RegisterReqBody
import com.buddy.growfarm.network.dto.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GrowFarmAPI {

    @POST("api/users/auth")
    suspend fun postUserSignIn(@Body loginReqBody: LoginReqBody) : Response<UserResponse>

    @POST("api/users")
    suspend fun postUserSignUp(@Body registerReqBody: RegisterReqBody) : Response<UserResponse>

    @POST("api/prices")
    suspend fun getPrices(@Body pricesReqBody: PricesReqBody) : Response<PricesResBody>
}
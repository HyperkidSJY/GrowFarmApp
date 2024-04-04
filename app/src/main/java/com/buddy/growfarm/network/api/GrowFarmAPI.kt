package com.buddy.growfarm.network.api


import com.buddy.growfarm.network.dto.Comment
import com.buddy.growfarm.network.dto.CommentReqBody
import com.buddy.growfarm.network.dto.GetConnectionsResponse
import com.buddy.growfarm.network.dto.LoginReqBody
import com.buddy.growfarm.network.dto.OtpReqBody
import com.buddy.growfarm.network.dto.OtpResponse
import com.buddy.growfarm.network.dto.PricesResBody
import com.buddy.growfarm.network.dto.RegisterReqBody
import com.buddy.growfarm.network.dto.SetProfileReqBody
import com.buddy.growfarm.network.dto.SetProfileResponse
import com.buddy.growfarm.network.dto.UserIdReqBody
import com.buddy.growfarm.network.dto.UserPostResponse
import com.buddy.growfarm.network.dto.UserResponse
import com.buddy.growfarm.network.dto.VerifyOtpReqBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface GrowFarmAPI {

    @POST("api/users/auth")
    suspend fun postUserSignIn(@Body loginReqBody: LoginReqBody): Response<UserResponse>

    @POST("api/users/regf")
    suspend fun postUserSignUp(@Body registerReqBody: RegisterReqBody): Response<UserResponse>

    @POST("api/users/twilio-sms/send-otp")
    suspend fun sendOtp(@Body otpReqBody: OtpReqBody): Response<OtpResponse>

    @POST("api/users/twilio-sms/verify-otp")
    suspend fun verifyOTP(@Body verifyOtpReqBody: VerifyOtpReqBody): Response<OtpResponse>

    @GET("api/prices/{pincode}")
    suspend fun getPrices(@Path("pincode") pincode : String): Response<PricesResBody>

    @Multipart
    @POST("api/users/regs")
    suspend fun setupProfile(
        @Part("_id") id : RequestBody,
        @Part("roles") roles : RequestBody,
        @Part("pincode") pincode: RequestBody,
        @Part("gender") gender : RequestBody,
        @Part file : MultipartBody.Part
    ) : Response<OtpResponse>

    @Multipart
    @POST("api/post/addpost")
    suspend fun addPost(
        @Part("postTitle") postTitle: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ) : Response<OtpResponse>

    @GET("api/post/getuserpost/{id}")
    suspend fun getUserPosts(@Path("id") id : String) : Response<UserPostResponse>

    @POST("api/post/like/{id}")
    suspend fun likedPost(@Path("id") id : String,@Body userId : UserIdReqBody ) : Response<OtpResponse>

    @GET("api/post/gethomepagepost/{id}")
    suspend fun getHomePagePosts(@Path("id") id : String) : Response<UserPostResponse>

    @DELETE("api/post/deletepost/{id}")
    suspend fun deletePosts(@Path("id") id : String) : Response<OtpResponse>

    @GET("api/users/getuserslist/{id}")
    suspend fun getConnections(@Path("id") id : String) : Response<GetConnectionsResponse>

    @POST("api/users/follow/{id}")
    suspend fun followUser(@Path("id") id : String, @Body userId : UserIdReqBody ) : Response<OtpResponse>

    @GET("api/users/getfollowers/{id}")
    suspend fun getFollowers(@Path("id") id : String) : Response<GetConnectionsResponse>

    @GET("api/users/getfollowing/{id}")
    suspend fun getFollowing(@Path("id") id : String) : Response<GetConnectionsResponse>

    @POST("api/users/unfollow/{id}")
    suspend fun unfollowUser(@Path("id") id : String, @Body userId : UserIdReqBody ) : Response<OtpResponse>

    @POST("api/post/comment/{postId}")
    suspend fun addComment(@Path("postId") postId : String, @Body comment : CommentReqBody) : Response<OtpResponse>

    @GET("api/post/getcomments/{id}")
    suspend fun getComments(@Path("id") id:String) : Response<Comment>
}
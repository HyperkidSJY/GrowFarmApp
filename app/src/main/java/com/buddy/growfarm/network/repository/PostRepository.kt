package com.buddy.growfarm.network.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buddy.growfarm.network.api.GrowFarmAPI
import com.buddy.growfarm.network.dto.Comment
import com.buddy.growfarm.network.dto.CommentReqBody
import com.buddy.growfarm.network.dto.OtpResponse
import com.buddy.growfarm.network.dto.UserIdReqBody
import com.buddy.growfarm.network.dto.UserPostResponse
import com.buddy.growfarm.utils.NetworkResponseHandler.Companion.handleResponse
import com.buddy.growfarm.utils.NetworkResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


class PostRepository @Inject constructor(private val growFarmAPI: GrowFarmAPI) {

    private val _userPostsLiveData = MutableLiveData<NetworkResult<UserPostResponse>>()
    val userPostsLiveData: LiveData<NetworkResult<UserPostResponse>>
        get() = _userPostsLiveData

    private val _addPostLiveData = MutableLiveData<NetworkResult<OtpResponse>>()
    val addPostResponse: LiveData<NetworkResult<OtpResponse>>
        get() = _addPostLiveData

    private val _getComments = MutableLiveData<NetworkResult<Comment>>()
    val getComments: LiveData<NetworkResult<Comment>>
        get() = _getComments


    suspend fun addPost(
        postTitle: String,
        userId: String,
        description: String,
        file: File
    ) {
        _addPostLiveData.postValue(NetworkResult.Loading())
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imageBody = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val postTitlePart = postTitle.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val userIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())

        val response = growFarmAPI.addPost(postTitlePart, userIdPart, descriptionPart, imageBody)
        handleResponse(response, _addPostLiveData)
//        Log.e("posts",response.body()?.message.toString())
    }

    suspend fun getUserPosts(id: String) {
        _userPostsLiveData.postValue(NetworkResult.Loading())
        val response = growFarmAPI.getUserPosts(id)
        handleResponse(response, _userPostsLiveData)
    }

    suspend fun getHomePagePosts(id: String) {
        _userPostsLiveData.postValue(NetworkResult.Loading())
        val response = growFarmAPI.getHomePagePosts(id)
//        Log.e("Home",response.body().toString())
        handleResponse(response, _userPostsLiveData)
    }


    suspend fun likePosts(postId: String, userId: String) {
        _addPostLiveData.postValue(NetworkResult.Loading())
        val response = growFarmAPI.likedPost(postId, UserIdReqBody(userId))
        handleResponse(response, _addPostLiveData)
    }

    suspend fun deletePost(postId: String) {
        _addPostLiveData.postValue(NetworkResult.Loading())
        val response = growFarmAPI.deletePosts(postId)
        handleResponse(response, _addPostLiveData)
    }

    suspend fun addComment(postId: String, userId: String, comment: String) {
        _addPostLiveData.postValue(NetworkResult.Loading())
        val response = growFarmAPI.addComment(postId, CommentReqBody(userId, comment))
        handleResponse(response, _addPostLiveData)
    }

    suspend fun getComments(postId: String) {
        _getComments.postValue(NetworkResult.Loading())
        val response = growFarmAPI.getComments(postId)
        handleResponse(response, _getComments)
    }
}
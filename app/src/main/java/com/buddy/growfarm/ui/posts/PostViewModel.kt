package com.buddy.growfarm.ui.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buddy.growfarm.network.dto.Comment
import com.buddy.growfarm.network.dto.OtpResponse
import com.buddy.growfarm.network.dto.UserPostResponse
import com.buddy.growfarm.network.repository.PostRepository
import com.buddy.growfarm.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {

    val userPostResponse: LiveData<NetworkResult<UserPostResponse>>
        get() = postRepository.userPostsLiveData

    val addPostResponse: LiveData<NetworkResult<OtpResponse>>
        get() = postRepository.addPostResponse

    val getComments : LiveData<NetworkResult<Comment>>
        get() = postRepository.getComments

    fun getUserPosts(id: String) {
        viewModelScope.launch {
            postRepository.getUserPosts(id)
        }
    }

    fun createPosts(postTitle: String, userId: String, description: String, file: File) {
        viewModelScope.launch {
            postRepository.addPost(postTitle, userId,description , file)
        }
    }

    fun likedPosts(postId : String, userId : String,isHome : Boolean){
        viewModelScope.launch {
            postRepository.likePosts(postId,userId)
            if (isHome){
                getHomePagePosts(userId)
            }else{
                getUserPosts(userId)
            }
        }
    }

    fun getHomePagePosts(id: String){
        viewModelScope.launch {
            postRepository.getHomePagePosts(id)
        }
    }

    fun deletePosts(id : String,userId: String){
        viewModelScope.launch {
            postRepository.deletePost(id)
            getUserPosts(userId)
        }
    }

    fun addComment(postId : String, userId : String , comment : String){
        viewModelScope.launch {
            postRepository.addComment(postId,userId,comment)
            getComment(postId)
        }
    }

    fun getComment(postId : String) {
        viewModelScope.launch {
            postRepository.getComments(postId)
        }
    }
}
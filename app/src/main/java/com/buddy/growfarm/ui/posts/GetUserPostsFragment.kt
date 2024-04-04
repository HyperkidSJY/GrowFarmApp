package com.buddy.growfarm.ui.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.buddy.growfarm.R
import com.buddy.growfarm.databinding.FragmentGetUserPostsBinding
import com.buddy.growfarm.utils.NetworkResult
import com.buddy.growfarm.utils.SessionManage
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GetUserPostsFragment : Fragment() {

    private var _binding: FragmentGetUserPostsBinding? = null
    private val binding get() = _binding!!

    private val postViewModel by viewModels<PostViewModel>()

    @Inject
    lateinit var session: SessionManage

    private lateinit var adapter: PostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGetUserPostsBinding.inflate(inflater, container, false)
        adapter = PostsAdapter(requireContext(), false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservers()

//        postViewModel.getUserPosts("65a0fe95f93ea63f2492c3ce")
        postViewModel.getUserPosts(session.getUserID().toString())

        binding.rvUserPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUserPosts.adapter = adapter
        adapter.setLikeClickListener { postId ->
            postViewModel.likedPosts(postId, session.getUserID().toString(),false)
        }
        adapter.setPopMenuClickListener { postId ->
            postViewModel.deletePosts(postId,session.getUserID().toString())
        }
        adapter.setCommentClickListener { postId ->
            val action = GetUserPostsFragmentDirections.actionGetUserPostsFragmentToCommentSectionFragment(postId)
            findNavController().navigate(action)
        }

        binding.btnFollowers.setOnClickListener {
            val action =
                GetUserPostsFragmentDirections.actionGetUserPostsFragmentToManageConnectionsFragment(true)
            findNavController().navigate(action)
        }
        binding.btnFollowing.setOnClickListener {
            val action =
                GetUserPostsFragmentDirections.actionGetUserPostsFragmentToManageConnectionsFragment(false)
            findNavController().navigate(action)
        }

    }

    private fun bindObservers() {

        postViewModel.userPostResponse.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    adapter.differ.submitList(it.data?.result)
                    Glide.with(requireContext()).load(it.data?.profilePicture).into(binding.ivProfilePic)
                    binding.btnFollowing.text = it.data?.following.toString() + "\nFollowing"
                    binding.btnFollowers.text = it.data?.followers.toString() + "\nFollowers"
                    if(it.data?.result.isNullOrEmpty()){
                        binding.tvNoPost.visibility = View.VISIBLE
                        binding.rvUserPosts.visibility = View.GONE
                    }else {
                        binding.tvNoPost.visibility = View.GONE
                        binding.rvUserPosts.visibility = View.VISIBLE
                    }
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

    }

    override fun onDestroy() {
        _binding = null

        super.onDestroy()
    }
}
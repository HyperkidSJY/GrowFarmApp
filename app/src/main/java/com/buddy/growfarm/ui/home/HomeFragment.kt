package com.buddy.growfarm.ui.home

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
import com.buddy.growfarm.databinding.FragmentHomeBinding
import com.buddy.growfarm.ui.posts.PostViewModel
import com.buddy.growfarm.ui.posts.PostsAdapter
import com.buddy.growfarm.utils.NetworkResult
import com.buddy.growfarm.utils.SessionManage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val postViewModel by viewModels<PostViewModel>()

    @Inject
    lateinit var session: SessionManage

    private lateinit var adapter : PostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        adapter = PostsAdapter(requireContext(),true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefresh.setOnRefreshListener {
            postViewModel.getHomePagePosts(session.getUserID().toString())
        }

        bindObservers()

//        postViewModel.getUserPosts("65a0fe95f93ea63f2492c3ce")
        postViewModel.getHomePagePosts(session.getUserID().toString())

        binding.rvUserPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUserPosts.adapter = adapter
        adapter.setLikeClickListener {postId ->
            postViewModel.likedPosts(postId,session.getUserID().toString(),true)
        }
        adapter.setCommentClickListener { postId ->
            val action = HomeFragmentDirections.actionHomeFragmentToCommentSectionFragment(postId)
            findNavController().navigate(action)
        }
    }

    private fun bindObservers() {

        postViewModel.userPostResponse.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    adapter.differ.submitList(it.data?.result)
                    if(it.data?.result.isNullOrEmpty()) {
                        binding.tvNoPost.visibility = View.VISIBLE
                        binding.rvUserPosts.visibility = View.GONE
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
            binding.swipeRefresh.isRefreshing = false
        })
    }

    override fun onDestroy() {
        _binding = null

        super.onDestroy()
    }
}
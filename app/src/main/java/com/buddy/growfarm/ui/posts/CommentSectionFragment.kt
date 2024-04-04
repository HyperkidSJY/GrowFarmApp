package com.buddy.growfarm.ui.posts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.buddy.growfarm.databinding.FragmentCommentSectionBinding
import com.buddy.growfarm.utils.NetworkResult
import com.buddy.growfarm.utils.SessionManage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CommentSectionFragment : Fragment() {

    private var _binding: FragmentCommentSectionBinding? = null
    private val binding get() = _binding!!

    private val postViewModel by viewModels<PostViewModel>()

    private var postId : String = ""

    @Inject
    lateinit var session: SessionManage

    private lateinit var adapter: CommentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentSectionBinding.inflate(inflater, container, false)
        postId = arguments?.getString("postId") ?: ""
        adapter = CommentAdapter(requireContext())
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservers()
        postViewModel.getComment(postId)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.btnAddComment.setOnClickListener {
            postViewModel.addComment(postId,session.getUserID().toString(),binding.tilAddComment.text.toString())
        }
    }

    private fun bindObservers() {

        postViewModel.getComments.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    adapter.differ.submitList(it.data?.comments)
                    binding.tilAddComment.setText("")
                    if(it.data?.comments.isNullOrEmpty()){
                        binding.tvNoPost.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    }else {
                        binding.tvNoPost.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
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
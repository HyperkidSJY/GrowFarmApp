package com.buddy.growfarm.ui.posts

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.buddy.growfarm.R
import com.buddy.growfarm.databinding.FragmentCreatePostBinding
import com.buddy.growfarm.utils.NetworkResult
import com.buddy.growfarm.utils.SessionManage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class CreatePostFragment : Fragment() {

    private var _binding: FragmentCreatePostBinding? = null

    private val binding get() = _binding!!

    private lateinit var mImageUri: Uri

    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        mImageUri = it!!
        binding.ivPost.setImageURI(it)
    }

    @Inject
    lateinit var session: SessionManage

    private val postViewModel by viewModels<PostViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOpenGallery.setOnClickListener {
            contract.launch("image/*")
        }

        binding.btnPost.setOnClickListener {
            addPost()
        }

        bindObservers()

    }

    private fun addPost() {
        val filesDir = requireActivity().applicationContext.filesDir
        val file = File(filesDir, "image.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(mImageUri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)
        postViewModel.createPosts(
            binding.tilName.text.toString(),
            session.getUserID().toString(),
            binding.etText.text.toString(),
            file
        )
    }

    private fun bindObservers() {
        postViewModel.addPostResponse.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    Toast.makeText(requireContext(),"Post added Successfully",Toast.LENGTH_SHORT).show()
                    binding.tilName.setText("")
                    binding.etText.setText("")
                    binding.ivPost.setImageURI(null)
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is NetworkResult.Error -> {

                }
            }
        })
    }
}
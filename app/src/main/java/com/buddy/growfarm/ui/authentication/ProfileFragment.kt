package com.buddy.growfarm.ui.authentication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.buddy.growfarm.AppActivity
import com.buddy.growfarm.R
import com.buddy.growfarm.databinding.FragmentProfileBinding
import com.buddy.growfarm.utils.NetworkResult
import com.buddy.growfarm.utils.SessionManage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var session: SessionManage

    private lateinit var mImageUri: Uri

    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        mImageUri = it!!
        binding.ivProfilePic.setImageURI(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var gender = "Male"
        var role = "Farmer"

        binding.radioGroup.setOnCheckedChangeListener{_,_ ->
            val rb: RadioButton = binding.radioGroup.findViewById(binding.radioGroup.checkedRadioButtonId)
            gender = rb.text.toString()
        }

        binding.rgChooseRoles.setOnCheckedChangeListener { _,_  ->
            val rb : RadioButton = binding.rgChooseRoles.findViewById(binding.rgChooseRoles.checkedRadioButtonId)
            role = rb.text.toString()
        }

        binding.btnComplete.setOnClickListener {
           addProfilePic(gender,role)
        }

        binding.ivProfilePic.setOnClickListener {
            contract.launch("image/*")
        }

        bindObServer()
    }

    private fun bindObServer() {
        authViewModel.setProfileLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    session.setLoggedIn(true)
                    findNavController().navigate(R.id.action_profileFragment_to_appActivity)
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is NetworkResult.Error -> {
                    showValidationErrors(it.message.toString())
                }
            }
        })
    }

    private fun addProfilePic(gender : String , role : String) {
        val filesDir = requireActivity().applicationContext.filesDir
        val file = File(filesDir, "image.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(mImageUri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)
        authViewModel.setupProfile(
            session.getUserID().toString(),
            role,
            binding.tilPincode.text.toString(),
            gender,
            file
        )
    }

    private fun showValidationErrors(error: String) {
        binding.txtError.text =
            String.format(resources.getString(R.string.txt_error_message, error))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
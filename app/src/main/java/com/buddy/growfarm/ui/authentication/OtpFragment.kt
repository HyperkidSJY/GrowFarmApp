package com.buddy.growfarm.ui.authentication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.buddy.growfarm.R
import com.buddy.growfarm.databinding.FragmentOtpBinding
import com.buddy.growfarm.utils.NetworkResult
import com.buddy.growfarm.utils.SessionManage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OtpFragment : Fragment() {

    private var _binding : FragmentOtpBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var session : SessionManage

    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOtpBinding.inflate(inflater,container,false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val phoneNumber = session.getPhone().toString()

        binding.tvPhoneNumber.text = phoneNumber

        binding.btnSendOTP.setOnClickListener {
            Toast.makeText(requireContext(),"OTP send successfully",Toast.LENGTH_SHORT).show()
            authViewModel.sendOTP(phoneNumber)
        }

        var otp : String = ""
        binding.otpView.setText("")
        binding.otpView.setOtpCompletionListener{
            otp = it
        }

        binding.btnVerify.setOnClickListener{
            authViewModel.verifyOTP(phoneNumber,otp)
        }
        bindObservers()
    }

    private fun bindObservers() {
        authViewModel.otpResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when(it){
                is  NetworkResult.Success -> {
                    findNavController().navigate(R.id.action_otpFragment_to_profileFragment)
                }
                is NetworkResult.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                }
                is NetworkResult.Error ->{
                    Log.e("error",it.message.toString())
                    showValidationErrors(it.message.toString())
                }
            }
        })
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
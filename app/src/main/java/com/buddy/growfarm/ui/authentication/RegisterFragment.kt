package com.buddy.growfarm.ui.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.buddy.growfarm.R
import com.buddy.growfarm.databinding.FragmentRegisterBinding
import com.buddy.growfarm.network.dto.RegisterReqBody
import com.buddy.growfarm.utils.NetworkResult
import com.buddy.growfarm.utils.SessionManage
import com.buddy.growfarm.utils.ValidateCredentials
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var session : SessionManage


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAlreadyUser.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btnSignUp.setOnClickListener {
            val validationResult = validateCredentials()
            if(validationResult.first){
                authViewModel.registerUser(getUserReq().phoneNumber,getUserReq().password,getUserReq().gmail,getUserReq().name)
            }else{
                binding.txtError.text = validationResult.second
            }
        }

        bindObservers()


    }

    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    session.saveUserID(it.data!!.id)
                    session.saveUserPhone(it.data.phoneNumber)
                    findNavController().navigate(R.id.action_registerFragment_to_otpFragment)
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

    private fun getUserReq() : RegisterReqBody {
        val name = binding.tilName.text.toString()
        val email = binding.tilEmail.text.toString()
        val password = binding.tilPassword.text.toString()
        val phoneNumber = binding.tilPhoneNumber.text.toString().trim()

        return RegisterReqBody(phoneNumber,password,email,name)
    }

    private fun validateCredentials(): Pair<Boolean, String> {
        val userReqBody = getUserReq()
        return ValidateCredentials.validateRegisterCredentials(userReqBody.gmail,userReqBody.phoneNumber,userReqBody.password,userReqBody.name)
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
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
import com.buddy.growfarm.databinding.FragmentLoginBinding
import com.buddy.growfarm.utils.NetworkResult
import com.buddy.growfarm.utils.SessionManage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var session : SessionManage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener {
            authViewModel.loginUser(binding.tilPhoneNumber.toString(),binding.tilPassword.toString())
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().popBackStack()
        }

        bindObservers()
    }

    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when(it){
                is  NetworkResult.Success -> {
                    session.saveUserID(it.data!!.id)
                    session.saveUserPhone(it.data.phoneNumber)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is NetworkResult.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                }
                is NetworkResult.Error ->{
                    showValidationErrors(it.message.toString())
                }
            }
        })
    }

    private fun showValidationErrors(error: String) {
        binding.txtError.text = String.format(resources.getString(R.string.txt_error_message, error))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
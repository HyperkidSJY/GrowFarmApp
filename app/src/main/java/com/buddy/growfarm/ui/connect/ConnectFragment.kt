package com.buddy.growfarm.ui.connect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.buddy.growfarm.databinding.FragmentConnectBinding
import com.buddy.growfarm.utils.NetworkResult
import com.buddy.growfarm.utils.SessionManage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ConnectFragment : Fragment() {

    private var _binding: FragmentConnectBinding? = null
    private val binding get() = _binding!!

    private val connectionViewModel by viewModels<ConnectionViewModel>()

    @Inject
    lateinit var session: SessionManage

    private lateinit var adapter: UserConnectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConnectBinding.inflate(inflater, container, false)
        adapter = UserConnectionAdapter(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservers()
        connectionViewModel.getConnections(session.getUserID().toString())

        binding.rvConnect.layoutManager = GridLayoutManager(requireContext(),2)
        binding.rvConnect.adapter = adapter

        adapter.setConnectClickListener { postId ->
            connectionViewModel.followUser(postId,session.getUserID().toString())
        }

    }

    private fun bindObservers() {

        connectionViewModel.getConnectionLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    adapter.differ.submitList(it.data?.users)
                    if(it.data?.users.isNullOrEmpty()){
                        binding.tvNoPost.visibility = View.VISIBLE
                        binding.rvConnect.visibility = View.GONE
                    }else {
                        binding.tvNoPost.visibility = View.GONE
                        binding.rvConnect.visibility = View.VISIBLE
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
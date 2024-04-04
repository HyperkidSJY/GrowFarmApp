package com.buddy.growfarm.ui.connect

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.buddy.growfarm.R
import com.buddy.growfarm.databinding.FragmentConnectBinding
import com.buddy.growfarm.databinding.FragmentManageConnectionsBinding
import com.buddy.growfarm.utils.NetworkResult
import com.buddy.growfarm.utils.SessionManage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ManageConnectionsFragment : Fragment() {

    private var _binding: FragmentManageConnectionsBinding? = null
    private val binding get() = _binding!!

    private val connectionViewModel by viewModels<ConnectionViewModel>()

    @Inject
    lateinit var session: SessionManage

    private lateinit var adapter: ManageConnectionsAdapter

    private var isFollowers = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentManageConnectionsBinding.inflate(inflater, container, false)
        isFollowers = arguments?.getBoolean("isFollowers") ?: false
        adapter = if (isFollowers) {
            ManageConnectionsAdapter(requireContext(),true)
        } else {
            ManageConnectionsAdapter(requireContext(),false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservers()
        if(isFollowers){
            connectionViewModel.getFollowers(session.getUserID().toString())
        }else{
            connectionViewModel.getFollowing(session.getUserID().toString())
        }
        binding.rvManageConnections.layoutManager = LinearLayoutManager(requireContext())
        binding.rvManageConnections.adapter = adapter

        adapter.setUnFollowClickListener {
            connectionViewModel.unfollowUser(it,session.getUserID().toString())
        }
    }

    private fun bindObservers() {

        connectionViewModel.getConnectionLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    Log.e("follow",it.data?.users.toString())
                    adapter.differ.submitList(it.data?.users)
                    if(it.data?.users.isNullOrEmpty()){
                        if(isFollowers){
                            binding.tvNoPost.text = "No Followers"
                        }
                        binding.tvNoPost.visibility = View.VISIBLE
                        binding.rvManageConnections.visibility = View.GONE
                    }else {
                        binding.tvNoPost.visibility = View.GONE
                        binding.rvManageConnections.visibility = View.VISIBLE
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
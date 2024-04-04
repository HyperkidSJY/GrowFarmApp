package com.buddy.growfarm.ui.prices

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.buddy.growfarm.databinding.FragmentPricesBinding
import com.buddy.growfarm.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PricesFragment : Fragment() {

    private var _binding : FragmentPricesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter : PricesAdapter

    private val pricesViewModel by viewModels<PricesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPricesBinding.inflate(inflater,container,false)
        adapter = PricesAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservers()

        pricesViewModel.getPrices("392001")

        binding.rvPrices.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPrices.adapter = adapter
    }

    private fun bindObservers() {

        pricesViewModel.pricesResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    adapter.differ.submitList(it.data?.records)
                    binding.llTable.visibility = View.VISIBLE
                    if(it.data?.records.isNullOrEmpty()){
                        binding.tvNoPost.visibility = View.VISIBLE
                        binding.llTable.visibility = View.GONE
                        binding.llPrices.visibility = View.GONE
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
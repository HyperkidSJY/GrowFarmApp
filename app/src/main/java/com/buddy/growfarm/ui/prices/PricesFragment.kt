package com.buddy.growfarm.ui.prices

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buddy.growfarm.databinding.FragmentPricesBinding


class PricesFragment : Fragment() {

    private var _binding : FragmentPricesBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPricesBinding.inflate(inflater,container,false)

        return binding.root
    }

}
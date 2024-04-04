package com.buddy.growfarm.ui.prices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.buddy.growfarm.databinding.PricesListItemBinding
import com.buddy.growfarm.network.dto.Record


class PricesAdapter() : RecyclerView.Adapter<PricesAdapter.PriceViewHolder>() {

    inner class PriceViewHolder(binding: PricesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvCommodity = binding.tvCommodity
        val tvModalPrice = binding.tvModalPrice
        val tvMaxPrice = binding.maxPrice
        val tvMarket = binding.tvMarket
        val tvMinPrice = binding.tvMinPrice
    }

    private val differCallback = object : DiffUtil.ItemCallback<Record>() {
        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem.commodity == newItem.commodity
        }

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        return PriceViewHolder(
            PricesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        val post = differ.currentList[position]

        holder.tvCommodity.text = post.commodity
        holder.tvModalPrice.text = "Rs." + post.modal_price
        holder.tvMinPrice.text = "Rs." + post.min_price
        holder.tvMaxPrice.text = "Rs." + post.max_price
        holder.tvMarket.text = post.market
    }

}




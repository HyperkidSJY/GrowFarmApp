package com.buddy.growfarm.ui.connect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.buddy.growfarm.R
import com.buddy.growfarm.databinding.ConnectionsListItemBinding
import com.buddy.growfarm.network.dto.CRes
import com.bumptech.glide.Glide

class ManageConnectionsAdapter(private val context: Context, private val isFollower: Boolean) :
    RecyclerView.Adapter<ManageConnectionsAdapter.ManageViewHolder>() {

    private var unfollowClickListener: ((String) -> Unit)? = null

    inner class ManageViewHolder(binding: ConnectionsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivProfileImage = binding.ivProfilePic
        val tvProfileName = binding.tvProfileName
        val tvRoles = binding.tvRoles
        val btnUnFollow = binding.btnUnFollow
    }

    private val differCallback = object : DiffUtil.ItemCallback<CRes>() {
        override fun areItemsTheSame(oldItem: CRes, newItem: CRes): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: CRes, newItem: CRes): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageViewHolder {
        return ManageViewHolder(
            ConnectionsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ManageViewHolder, position: Int) {
        val user = differ.currentList[position]
        Glide.with(context).load(user.profilePicture).into(holder.ivProfileImage)
        holder.tvProfileName.text = user.name
        holder.tvRoles.text = user.roles

        if(isFollower){
            holder.btnUnFollow.visibility = View.GONE
        }
        holder.btnUnFollow.setOnClickListener {
            unfollowClickListener?.let { it(user._id) }
        }
    }

    fun setUnFollowClickListener(listener: (String) -> Unit) {
        unfollowClickListener = listener
    }
}
package com.buddy.growfarm.ui.connect

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.buddy.growfarm.R
import com.buddy.growfarm.databinding.UserConnectListItemBinding
import com.buddy.growfarm.network.dto.CRes
import com.bumptech.glide.Glide

class UserConnectionAdapter(private val context: Context) :
    RecyclerView.Adapter<UserConnectionAdapter.ConnectionViewHolder>() {

    private var connectClickListener: ((String) -> Unit)? = null

    inner class ConnectionViewHolder(binding: UserConnectListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val ivProfileImage = binding.ivProfilePic
        val tvName = binding.tvProfileName
        val tvRoles = binding.tvProfileRole
        val btnConnect = binding.btnConnect
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionViewHolder {
        return ConnectionViewHolder(
            UserConnectListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ConnectionViewHolder, position: Int) {
        val user = differ.currentList[position]
        Glide.with(context).load(user.profilePicture).into(holder.ivProfileImage)
        holder.tvName.text = user.name
        holder.tvRoles.text = user.roles

        holder.btnConnect.setOnClickListener {
            connectClickListener?.let { it(user._id) }
        }
    }

    fun setConnectClickListener(listener: (String) -> Unit) {
        connectClickListener = listener
    }
}
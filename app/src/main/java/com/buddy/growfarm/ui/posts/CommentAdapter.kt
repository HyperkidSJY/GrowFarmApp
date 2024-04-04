package com.buddy.growfarm.ui.posts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.buddy.growfarm.databinding.CommentListItemBinding
import com.buddy.growfarm.network.dto.GetCommentResponse

class CommentAdapter(private val context: Context) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {


    inner class CommentViewHolder(binding: CommentListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.tvName
        val commentDescription = binding.tvCommentDescription
    }

    private val differCallback = object : DiffUtil.ItemCallback<GetCommentResponse>() {
        override fun areItemsTheSame(oldItem: GetCommentResponse, newItem: GetCommentResponse): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: GetCommentResponse, newItem: GetCommentResponse): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentViewHolder {
        return CommentViewHolder(
            CommentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {
        val comment = differ.currentList[position]

        holder.tvName.text = comment.username
        holder.commentDescription.text = comment.commentDescription
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}
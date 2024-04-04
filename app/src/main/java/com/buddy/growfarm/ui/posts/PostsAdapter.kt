package com.buddy.growfarm.ui.posts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.buddy.growfarm.R
import com.buddy.growfarm.databinding.UserPostListItemBinding
import com.bumptech.glide.Glide
import com.buddy.growfarm.network.dto.Result

class PostsAdapter(private val context: Context, private val isHome : Boolean = false) :
    RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    private var likeClickListener: ((String) -> Unit)? = null
    private var popMenuClickListener: ((String) -> Unit)? = null
    private var commentClickListener: ((String) -> Unit)? = null

    inner class PostViewHolder(binding: UserPostListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvProfileName = binding.tvProfileName
        val tvPostTitle = binding.tvPostTitle
        val tvRoles = binding.tvRoles
        val tvDescription = binding.tvDescription
        val ivPost = binding.ivPost
        val btnLike = binding.btnLike
        val btnPopupMenu = binding.ivButton
        val btnComment = binding.btnComment
    }

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            UserPostListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = differ.currentList[position]

        Glide.with(context).load(post.postMedia[0]).into(holder.ivPost)
        holder.tvProfileName.text = post.name
        holder.tvRoles.text = post.roles
        holder.tvDescription.text = post.description
        holder.tvPostTitle.text = post.postTitle
        holder.btnLike.text = post.likes.size.toString()


        holder.btnPopupMenu.setOnClickListener { view ->
            val popupMenu = PopupMenu(context, view)
            popupMenu.inflate(R.menu.pop_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_delete -> {
                        popMenuClickListener?.invoke(post._id)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        if(!isHome){
            holder.btnPopupMenu.visibility = View.VISIBLE
        }else {
            holder.btnPopupMenu.visibility = View.GONE
        }

        holder.btnLike.setOnClickListener {
            likeClickListener?.let { it(post._id) }
        }
        holder.btnComment.setOnClickListener {
            commentClickListener?.let {it(post._id)}
        }
    }

    fun setLikeClickListener(listener: (String) -> Unit) {
        likeClickListener = listener
    }


    fun setCommentClickListener(listener: (String) -> Unit) {
        commentClickListener = listener
    }

    fun setPopMenuClickListener(listener: (String) -> Unit){
        popMenuClickListener = listener
    }

}
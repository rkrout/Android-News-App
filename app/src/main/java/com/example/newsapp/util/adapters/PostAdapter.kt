package com.example.newsapp.util.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.data.models.PaginatedData
import com.example.newsapp.data.models.Post
import com.example.newsapp.databinding.HeadlineBinding
import com.example.newsapp.databinding.PagingFooterBinding
import com.example.newsapp.databinding.PostBinding
import com.example.newsapp.ui.postDetails.PostDetailsActivity
import com.example.newsapp.util.*
import com.example.newsapp.util.extensions.load

class PostAdapter(
    private val context: Context,
    private val onRetryClick: () -> Unit,
    private var paginatedPosts: PaginatedData<Post> = PaginatedData()
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class PostViewHolder(private val postBinding: PostBinding): RecyclerView.ViewHolder(postBinding.root){
        init {
            postBinding.root.setOnClickListener { onPostClick(adapterPosition) }
        }

        fun bind(post: Post){
            postBinding.title.text = post.title
            postBinding.createdAt.text = post.createdAt
            postBinding.image.load(post.imageUrl)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == paginatedPosts.data.size) VIEW_TYPE_FOOTER
        else VIEW_TYPE_POST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)

        if(viewType == VIEW_TYPE_FOOTER){
            val footerBinding: PagingFooterBinding = PagingFooterBinding.inflate(layoutInflater, parent, false)
            return PagingFooterViewHolder(footerBinding, onRetryClick)
        }

        val postBinding: PostBinding = PostBinding.inflate(layoutInflater, parent, false)
        return PostViewHolder(postBinding)
    }

    override fun getItemCount(): Int {
        return paginatedPosts.data.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PostViewHolder -> holder.bind(paginatedPosts.data[position])

            is PagingFooterViewHolder -> holder.bind(paginatedPosts.status)
        }
    }

    private fun onPostClick(position: Int){
        val intent = Intent(context, PostDetailsActivity::class.java)
        intent.putExtra(EXTRA_POST, paginatedPosts.data[position])
        context.startActivity(intent)
    }

    fun setData(paginatedPosts: PaginatedData<Post>){
        this.paginatedPosts = paginatedPosts
        notifyDataSetChanged()
    }
}
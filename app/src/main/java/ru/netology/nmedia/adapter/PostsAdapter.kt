package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.countFormat
typealias OnLikeListener = (Post)-> Unit

internal class PostsAdapter(
        private val onLikeClicked:OnLikeListener,
        private val onShareClicked: OnLikeListener
):ListAdapter<Post, PostsAdapter.ViewHolder>(DifCallBack){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = PostListItemBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(binding, onLikeClicked,onShareClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post =getItem(position)
        holder.bind(post)
    }

    class ViewHolder(
        private val binding: PostListItemBinding,
        private val onLikeClicked: OnLikeListener,
        private val onShareClicked:OnLikeListener)
        : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init {
            binding.like.setOnClickListener { post }
        }

        fun bind(post: Post) {
            this.post = post

            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likeCount.text =  countFormat (post.likesCount)
                shareCount.text = countFormat(post.sharesCount)
                viewsCount.text = countFormat(post.viewsCount)
                like.setImageResource(getLikeIconResId(post.likedByMe)
                )
              like.setOnClickListener {
                  onLikeClicked(post)
              }
                share.setOnClickListener {
                    onShareClicked(post)
                }
            }


        }
        @DrawableRes
        private fun getLikeIconResId(liked: Boolean) =
            if (liked) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_outline_favorite_border_24


    }

    private object DifCallBack : DiffUtil.ItemCallback<Post>(){
        override fun areItemsTheSame(oldItem: Post, newItem: Post)=
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post)=
            oldItem == newItem
        }
    }


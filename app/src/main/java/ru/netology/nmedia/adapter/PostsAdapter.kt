package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.countFormat

internal class PostsAdapter(
        private val interactionListener: PostInteractionListener
):ListAdapter<Post, PostsAdapter.ViewHolder>(DifCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = PostListItemBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: PostListItemBinding,
        listener: PostInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init {
            binding.like.setOnClickListener {
                listener.onLikeClicked(post)

                binding.share.setOnClickListener {
                    listener.onShareClicked(post)
                }
            }
        }

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.menu).apply {
                inflate(R.menu.option_posts)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(post)
                            true
                        }
                        R.id.edit->{
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }


            fun bind(post: Post) {
                this.post = post

                with(binding) {
                    author.text = post.author
                    published.text = post.published
                    content.text = post.content
                    likeCount.text = countFormat(post.likesCount)
                    shareCount.text = countFormat(post.sharesCount)
                    viewsCount.text = countFormat(post.viewsCount)
                    like.setImageResource(getLikeIconResId(post.likedByMe))
                    menu.setOnClickListener { popupMenu.show() }

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


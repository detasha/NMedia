package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
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
            }

            binding.share.setOnClickListener {
                listener.onShareClicked(post)
            }
            binding.menu.setOnClickListener {
                popupMenu.show()
            }
            binding.videoBanner.setOnClickListener {
                listener.onPlayVideoClicked(post)
            }

            binding.root.setOnClickListener { listener.onPostClicked(post) }

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
                        R.id.edit -> {
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
                like.text = countFormat(post.likesCount)
                share.text = countFormat(post.sharesCount)
                like.isChecked = post.likedByMe
                videoGroup.isVisible = post.video != null

            }
        }

    }


        private object DifCallBack : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Post, newItem: Post) =
                oldItem == newItem
        }
    }

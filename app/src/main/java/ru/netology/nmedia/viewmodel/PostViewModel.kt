package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.impl.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()

    val data get() = repository.data

    fun onLikeClicked(post:Post) = repository.like(post.id)
    fun onShareClicked(post:Post) = repository.share(post.id)

}
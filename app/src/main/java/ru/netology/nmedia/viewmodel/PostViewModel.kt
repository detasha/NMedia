package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.impl.PostRepositoryInMemoryImpl

class PostViewModel: ViewModel(),PostInteractionListener {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()

    val data get() = repository.data
    val currentPost =MutableLiveData<Post?>(null)


    fun onSaveButtonClicked(content:String){
        if(content.isBlank())return
     val post = currentPost.value?.copy(
         content = content)?: Post(
         id=PostRepository.NEW_POST_ID,
         author = "Author",
         content =content,
         published = "Today",
         likedByMe = false
     )
        repository.save(post)
        currentPost.value = null
    }

    fun onCloseEditClicked() {
        currentPost.value = null
    }

    //region PostInteractionListener
    override fun onLikeClicked(post: Post)  = repository.like(post.id)

    override fun onShareClicked(post:Post) = repository.share(post.id)

    override fun onRemoveClicked(post: Post) = repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
    }

    //endregion PostInteractionListener

}
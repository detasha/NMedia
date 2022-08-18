package ru.netology.nmedia.repository.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository

class PostRepositoryInMemoryImpl : PostRepository {

    private var nextId= GENERATED_POST_AMOUNT.toLong()
    private var posts
        get() = checkNotNull(data.value)
        set(value) {
            data.value = value
        }


    override val data: MutableLiveData<List<Post>>

    init {
        val initialPosts = List(GENERATED_POST_AMOUNT) { index ->

            Post(
                id = index + 1L,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Контент поста ${index + 1}",
                published = "21 мая в 18:36",
                likesCount = 0,
                sharesCount = 0,
                viewsCount = 10,
                likedByMe = false
            )
        }
        data = MutableLiveData(initialPosts)

    }

    override fun like(postId: Long) {
        posts = posts.map { post ->
            if (post.id == postId) post.copy(
                likedByMe = !post.likedByMe,
                likesCount = post.likesCount + if (!post.likedByMe) 1 else -1
            )
            else post
        }
    }
       override fun share(postId: Long) {
            posts = posts.map { post ->
                if (post.id == postId) post.copy(
                    sharesCount = post.sharesCount + 1
                )
                else post
            }


        }

    override fun delete(postId: Long) {
        posts = posts.filterNot { it.id == postId}
        }

    override fun save(post: Post) {
        if(post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)

    }


    private fun insert(post:Post){
      data.value = listOf(
          post.copy(
            id = ++nextId
        )
      )+ posts
    }

    private fun update(post: Post){
        data.value = posts.map {
            if(it.id == post.id) post else it
        }
    }

    private companion object {
      const val GENERATED_POST_AMOUNT = 1000
    }
}



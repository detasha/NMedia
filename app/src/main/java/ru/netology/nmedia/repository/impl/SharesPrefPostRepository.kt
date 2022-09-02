package ru.netology.nmedia.repository.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import kotlinx.serialization.encodeToString
import kotlin.properties.Delegates


class SharesPrefPostRepository (application: Application):
    PostRepository {

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )

    private var nextId: Long by Delegates.observable(
        prefs.getLong(NEXT_ID_PREFS_KEY, 0L)
    ) { _, _, newValue ->
        prefs.edit { putLong(NEXT_ID_PREFS_KEY, newValue) }
    }


    private var posts
        get() = checkNotNull(data.value)
        set(value) {
            prefs.edit {
                val serializedPosts = Json.encodeToString(value)
                putString(POSTS_PREFS_KEY, serializedPosts)
            }
            data.value = value
        }


    override val data: MutableLiveData<List<Post>>

    init {
        val serializedPosts = prefs.getString(POSTS_PREFS_KEY, null)
        val posts:List<Post> = if (serializedPosts != null) {
           Json.decodeFromString(serializedPosts)
        }  else emptyList()
        data = MutableLiveData(posts)
    }



    override fun like(postId: Long) {
        posts = posts.map {
            if (it.id == postId) it.copy(
                likedByMe = !it.likedByMe,
                likesCount = it.likesCount + if (!it.likedByMe) 1 else -1
            )
            else it
        }
    }
       override fun share(postId: Long) {
            posts = posts.map {
                if (it.id == postId) it.copy(
                    sharesCount = it.sharesCount + 1
                )
                else it
            }


        }

    override fun delete(postId: Long) {
        posts = posts.filterNot { it.id == postId}
        }

    override fun save(post: Post) {
        if(post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)

    }


    private fun insert(post:Post){
      posts = listOf(
          post.copy(
            id = ++nextId
        )
      )+ posts
    }

   private fun update(post: Post){
        posts = posts.map {
            if(it.id == post.id) post else it
        }
    }

    private companion object {
        const val POSTS_PREFS_KEY = "posts"
        const val NEXT_ID_PREFS_KEY = "posts"
    }
}



package ru.netology.nmedia.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository

class PostRepositoryInMemoryImpl : PostRepository {


    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        likesCount = 5,
        sharesCount = 10,
        viewsCount = 10,
        likedByMe = false
    )

    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        val currentPost = checkNotNull(data.value) {
            "Data should not be null"
        }
       val likedPost = currentPost.copy(
           likedByMe = !currentPost.likedByMe,
                   likesCount = currentPost.likesCount + if (!currentPost.likedByMe) 1 else -1
       )
        data.value = likedPost
    }
    override fun share() {
        val currentPost = checkNotNull(data.value) {
            "Data should not be null"
        }

        data.value = currentPost.copy(
            sharesCount = currentPost.sharesCount + 1
        )
    }
}
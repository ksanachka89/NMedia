package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>

    fun like(postId: Long)
    fun share(postId: Long)
    fun delete(postId: Long)
    fun save(post: Post)

    companion object { // скажет, какой у нас идентификатор для нового поста
        const val NEW_POST_ID = 0L
    }
}
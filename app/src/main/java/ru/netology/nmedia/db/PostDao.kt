package ru.netology.nmedia.db

import ru.netology.nmedia.dto.Post

interface PostDao {
    fun getAll(): List<Post> // получить из бд список всех постов
    fun save(post: Post): Post // сохранить новый пост либо обновить существующий
    fun likeById(postId: Long)
    fun share(postId: Long)
    fun removeById(postId: Long) // удалять посты по id
}
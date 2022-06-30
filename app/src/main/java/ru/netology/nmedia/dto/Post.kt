package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 999_999,
    val likedByMe: Boolean = false,
    var shares: Int = 999,
    val shared: Boolean = false,
    val views: Int = 1_100_000
)
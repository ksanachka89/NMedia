package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 999_999,
    var likedByMe: Boolean = false,
    var shares: Int = 999,
    var views: Int = 1_100_000
)
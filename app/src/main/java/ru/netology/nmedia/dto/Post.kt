package ru.netology.nmedia.dto

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 0,
    val likedByMe: Boolean = false,
    var shares: Int = 0,
    val views: Int = 1,
    val video: String? = null
)
package ru.netology.nmedia.service

import com.google.gson.annotations.SerializedName

class Like( // распарсить данные контента
    @SerializedName("userId") // защита от обфускации
    val userId: Long,

    @SerializedName("userName")
    val userName: String,

    @SerializedName("postId")
    val postId: Long,

    @SerializedName("postAuthor")
    val postAuthor: String
)
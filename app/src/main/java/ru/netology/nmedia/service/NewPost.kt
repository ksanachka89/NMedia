package ru.netology.nmedia.service

import com.google.gson.annotations.SerializedName

class NewPost(

    @SerializedName("userId")
    val userId: Long,

    @SerializedName("userName")
    val userName: String,

    @SerializedName("postId")
    val postId: Long,

    @SerializedName("postAuthor")
    val postAuthor: String,

    @SerializedName("contentText")
    val contentText: String

)
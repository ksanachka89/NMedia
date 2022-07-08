package ru.netology.nmedia.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.dto.Post

class PostViewModel : ViewModel() {

    private val repository: PostRepository = InMemoryPostRepository()
    val data get() = repository.data

    fun onLikeClicked(post: Post) = repository.like(post.id)
    fun onShareClicked(post: Post) = repository.share(post.id)
}
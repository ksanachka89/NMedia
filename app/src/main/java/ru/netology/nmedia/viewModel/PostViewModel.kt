package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel : ViewModel(), PostInteractionListener {

    private val repository: PostRepository = InMemoryPostRepository()
    val data by repository::data
    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent =
        SingleLiveEvent<String?>() // текст поста, ктр редактируется / null, если новый пост
    val playVideo = SingleLiveEvent<String>()

    val currentPost = MutableLiveData<Post?>(null)

    fun onSaveButtonClicked(content: String) { // контент из EditText
        if (content.isBlank()) return
        val post = currentPost.value?.copy( // если не пустой (редактирование)
            content = content
        ) ?: Post( // если null (новый пост)
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = content,
            published = "Now"
        )
        repository.save(post)
        currentPost.value = null
    }

    fun onAddClicked() {
        navigateToPostContentScreenEvent.call()
    }

    override fun onLikeClicked(post: Post) = repository.like(post.id)
    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        sharePostContent.value = post.content
    }

    override fun onRemoveClicked(post: Post) = repository.delete(post.id)
    override fun onEditClicked(post: Post) { // в меню
        currentPost.value = post
        navigateToPostContentScreenEvent.value = post.content
    }

    override fun onPlayVideoClicked(post: Post) {
        val url: String = requireNotNull(post.video) { // проверяем, что есть url
            "Url must not be null"
        }
        playVideo.value = url
    }
}
package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.FilePostRepository
import ru.netology.nmedia.data.impl.SQLiteRepository
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostInteractionListener {

    private val repository: PostRepository =
        SQLiteRepository(
            dao = AppDb.getInstance(
                context = application
            ).postDao
        )

    val data by repository::data
    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent =
        SingleLiveEvent<String>() // текст поста, ктр редактируется / null, если новый пост
    val navigateToSinglePostScreenEvent =
        SingleLiveEvent<Long>()
    val playVideo = SingleLiveEvent<String>()

    private val currentPost = MutableLiveData<Post?>(null)

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

    override fun onPostClicked(post: Post) {
        currentPost.value = post
        navigateToSinglePostScreenEvent.value = post.id
    }

    fun addSinglePost(postId: Long) {
        currentPost.value = data.value?.firstOrNull { post ->
            post.id == postId
        }
    }

    fun onLikeClickedSinglePost() {
        currentPost.value?.let { repository.like(it.id) }
    }

    fun onShareClickedSinglePost() {
        currentPost.value?.let { repository.share(it.id) }
        sharePostContent.value = currentPost.value?.content
    }

    fun onRemoveClickedSinglePost() {
        currentPost.value?.let { repository.delete(it.id) }
    }

    fun onEditClickedSinglePost() {
        navigateToPostContentScreenEvent.value = currentPost.value?.content
    }

    fun onPlayClickedSinglePost() {
        val url: String = requireNotNull(currentPost.value?.video) { // проверяем, что есть url
            "Url must not be null"
        }
        playVideo.value = url
    }
}
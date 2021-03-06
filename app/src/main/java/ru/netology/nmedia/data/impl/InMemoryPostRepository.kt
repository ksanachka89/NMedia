package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {

    private var nextId = 1L

    private var posts = listOf(
        Post(
            nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            shared = false
        ),
        Post(
            nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Затем выросли в университет интернет-профессий: учили дизайнеров, аналитиков, программистов, менеджеров, маркетологов… Но обучать новым профессиям — это не предел. Мы продолжаем расти",
            published = "24 мая в 10:15",
            likedByMe = false,
            shared = false
        ),
        Post(
            nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Мы начинали с курсов об онлайн-маркетингеЗатем выросли в университет интернет-профессий: учили дизайнеров, аналитиков, программистов, менеджеров, маркетологов… Но обучать новым профессиям — это не предел. Мы продолжаем растиСегодня мы даём знания не только начинающим, но и тем, кто давно в профессии. Специалисты изучают новые инструменты, топ-менеджеры — получают степень MBA, руководители бизнеса — обучают своих сотрудников и обучаются сами",
            published = "26 мая в 18:30",
            likedByMe = false,
            shared = false
        ),
        Post(
            nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Нетология помогает расти на всех этапах карьеры — получать знания на старте и открывать новые высоты",
            published = "27 мая в 18:00",
            likedByMe = false,
            shared = false
        ),
        Post(
            nextId++, author = "Нетология. Университет интернет-профессий будущего",
            content = "Мы верим, что мир состоит из талантливых и способных людей",
            published = "29 мая в 09:00",
            likedByMe = false,
            shared = false
        ),
        Post(
            nextId++, author = "Нетология. Университет интернет-профессий будущего",
            content = "Мы знаем, что в каждом из нас уже есть та внутренняя сила, которая заставляет всегда хотеть больше, целиться выше, бежать быстрее",
            published = "1 июня в 10:30",
            likedByMe = false,
            shared = false
        ),
        Post(
            nextId++, author = "Нетология. Университет интернет-профессий будущего",
            content = "Наша миссия — помочь встать на путь роста и пройти с человеком по этому пути как можно дальше.",
            published = "3 июня в 12:30",
            likedByMe = false,
            shared = false
        ),
        Post(
            nextId++, author = "Нетология. Университет интернет-профессий будущего",
            content = "Сделать так, чтобы желание перемен стало сильнее страха перемен",
            published = "5 июня в 18:03",
            likedByMe = false,
            shared = false
        ),
        Post(
            nextId++, author = "Нетология. Университет интернет-профессий будущего",
            content = "Обучить новому, влюбить в знания",
            published = "7 июня в 15:00",
            likedByMe = false,
            shared = false
        ),
        Post(
            nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Стать импульсом к действию",
            published = "10 июня в 18:30",
            likedByMe = false,
            shared = false
        ),
        Post(
            nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Новая Нетология - это 4 уровня",
            published = "15 июня в 12:00",
            likedByMe = false,
            shared = false
        )
    )

    override val data = MutableLiveData(posts) // присваивается новый список

    override fun like(postId: Long) {
        posts = posts.map { it ->
            if (it.id == postId) {
                it.copy(likedByMe = !it.likedByMe)
                    .also { if (it.likedByMe) it.likes++ else it.likes-- }
            } else it
        }
        data.value = posts
    }

    override fun share(postId: Long) {
        posts = posts.map { it ->
            if (it.id == postId) {
                it.copy(shares = it.shares + 1)
            } else it
        }
        data.value = posts
    }

    override fun delete(postId: Long) {
        data.value = posts.filter {
            it.id != postId // оставляем только те посты, ктр не хотим удалить
        }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) // если пост новый, т.к. id=0
        else update(post)
    }

    private fun update(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private fun insert(post: Post) { // новый пост
        posts = listOf(
            post.copy(id = nextId++)
        ) + posts
        data.value = posts
    }
}
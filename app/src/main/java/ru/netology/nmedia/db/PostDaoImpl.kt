package ru.netology.nmedia.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dto.Post

class PostDaoImpl(
    private val db: SQLiteDatabase
) : PostDao {

    override fun getAll() = db.query( // выборка данных по условию
        PostsTable.NAME,
        PostsTable.ALL_COLUMNS_NAMES,
        null, null, null, null,
        "${PostsTable.Column.ID.columnName} DESC"
    ).use { cursor -> // вернуть список постов
        List(cursor.count) { // в курсоре записано кол-во записей
            cursor.moveToNext() // изначально курсор стоит за предельной позицией
            cursor.toPost()
        }
    }

    override fun save(post: Post): Post { // insert + update включены
        val values = ContentValues().apply {
            put(PostsTable.Column.AUTHOR.columnName, post.author)// кладем по ключу
            put(PostsTable.Column.CONTENT.columnName, post.content)
            put(PostsTable.Column.PUBLISHED.columnName, post.published)
            put(PostsTable.Column.LIKED_BY_ME.columnName, post.likedByMe)
            put(PostsTable.Column.LIKES.columnName, post.likes)
            put(PostsTable.Column.SHARES.columnName, post.shares)
            put(PostsTable.Column.VIEWS.columnName, post.views)
            put(PostsTable.Column.VIDEO_URL.columnName, post.video)
        }
        val id = if (post.id != 0L) { // если пост существует
            db.update( // обновляем его в бд
                PostsTable.NAME,
                values, // на такие значения
                "${PostsTable.Column.ID.columnName} = ?", // обновим те записи, ктр равны этому значению
                arrayOf(post.id.toString())
            )
            post.id // вернется id
        } else { //post.id == 0
            db.insert(PostsTable.NAME, null, values) // делаем вставку новой записи, id обновится
        }
        return db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMNS_NAMES,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString()),
            null, null, null // тут сортировка не нужна
        ).use { cursor ->
            cursor.moveToNext()
            cursor.toPost()
        }
    }

    override fun likeById(postId: Long) {
        db.execSQL( // свой произвольный запрос
            """
           UPDATE ${PostsTable.NAME} SET
           ${PostsTable.Column.LIKES.columnName} = ${PostsTable.Column.LIKES.columnName} +
           CASE WHEN ${PostsTable.Column.LIKED_BY_ME.columnName} THEN -1 ELSE 1 END,
            ${PostsTable.Column.LIKED_BY_ME.columnName} =
           CASE WHEN ${PostsTable.Column.LIKED_BY_ME.columnName} THEN 0 ELSE 1 END
           WHERE ${PostsTable.Column.ID.columnName} = ?;
        """.trimIndent(),
            arrayOf(postId.toString())
        )
    }

    override fun share(postId: Long) {
        db.execSQL(
            """
           UPDATE ${PostsTable.NAME} SET
           ${PostsTable.Column.SHARES.columnName} = ${PostsTable.Column.SHARES.columnName} + 1 
           WHERE ${PostsTable.Column.ID.columnName} = ?;
        """.trimIndent(),
            arrayOf(postId.toString())
        )
    }

    override fun removeById(postId: Long) {
        db.delete(
            PostsTable.NAME,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(postId.toString())
        )
    }
}
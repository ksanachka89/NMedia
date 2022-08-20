package ru.netology.nmedia.db

import android.database.Cursor
import ru.netology.nmedia.dto.Post

fun Cursor.toPost() = Post( // достаем записи
    id = getLong(getColumnIndexOrThrow(PostsTable.Column.ID.columnName)),
    author = getString(getColumnIndexOrThrow(PostsTable.Column.AUTHOR.columnName)),
    content = getString(getColumnIndexOrThrow(PostsTable.Column.CONTENT.columnName)),
    published = getString(getColumnIndexOrThrow(PostsTable.Column.PUBLISHED.columnName)),
    likes = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKES.columnName)),
    likedByMe = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKED_BY_ME.columnName)) != 0, // всё, что не 0, вернет true
    shares = getInt(getColumnIndexOrThrow(PostsTable.Column.SHARES.columnName)),
    views = getInt(getColumnIndexOrThrow(PostsTable.Column.VIEWS.columnName)),
    video = getString(getColumnIndexOrThrow(PostsTable.Column.VIDEO_URL.columnName))
)
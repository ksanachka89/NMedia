package ru.netology.nmedia.db

object PostsTable { // фиксируем названия
    const val NAME = "posts"

    // скрипт для создания таблицы
    val DDL = """
        CREATE TABLE $NAME (
            ${Column.ID.columnName} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Column.AUTHOR.columnName} TEXT NOT NULL,
            ${Column.CONTENT.columnName} TEXT NOT NULL,
            ${Column.PUBLISHED.columnName} TEXT NOT NULL,
            ${Column.LIKED_BY_ME.columnName} BOOLEAN NOT NULL DEFAULT 0,
            ${Column.LIKES.columnName} INTEGER NOT NULL DEFAULT 0,
            ${Column.SHARES.columnName} INTEGER NOT NULL DEFAULT 0,
            ${Column.VIEWS.columnName} INTEGER NOT NULL DEFAULT 0,
            ${Column.VIDEO_URL.columnName} TEXT
        );
        """.trimIndent()

    // массив строк всех колонок
    val ALL_COLUMNS_NAMES = Column.values().map {
        it.columnName
    }.toTypedArray()

    enum class Column(val columnName: String) {
        ID("id"),
        AUTHOR("author"),
        CONTENT("content"),
        PUBLISHED("published"),
        LIKED_BY_ME("likeByMe"),
        LIKES("likes"),
        SHARES("shares"),
        VIEWS("views"),
        VIDEO_URL("videoUrl")
    }
}
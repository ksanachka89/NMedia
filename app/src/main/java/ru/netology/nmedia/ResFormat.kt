package ru.netology.nmedia

import kotlin.math.floor

fun resFormat(number: Int): String {
    return when {
        number in 0..999 -> number.toString()
        number < 10_000 && number % 1_000 < 100 -> "${number / 1_000}K"
        number in 1_100..9_999 -> "${floor((number.toDouble() / 1_000) * 10) / 10}K"
        number in 10_000..999_999 -> "${number / 1_000}K"
        number % 1_000_000 < 100_000 -> "${number / 1_000_000}M"
        number in 1_100_000..999_999_999 -> "${floor((number.toDouble() / 1_000_000) * 10) / 10}M"
        else -> "0"
    }
}
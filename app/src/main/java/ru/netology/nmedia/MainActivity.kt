package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel


class MainActivity : AppCompatActivity() {
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { post ->
            with(binding) {
                amountOfLikes?.text = resFormat(post.likes)
                amountOfShares?.text = resFormat(post.shares)
                amountOfViews?.text = resFormat(post.views)

                author.text = post.author
                published.text = post.published
                content.text = post.content

                likes?.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_baseline_favorite_border_24
                )

                likes?.setOnClickListener {
                    viewModel.onLikeClicked()
                }

                shares?.setOnClickListener {
                    viewModel.onShareClicked()
                }
            }
        }
    }
}
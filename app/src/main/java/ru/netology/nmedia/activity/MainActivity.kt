package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel


class MainActivity : AppCompatActivity() {
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter // чтобы список обновился

        viewModel.data.observe(this) { posts -> // поставили новый список
            adapter.submitList(posts) // сравнивает старый и новый списки
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }

        viewModel.sharePostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, postContent)
            }
            val shareIntent = Intent.createChooser( // заголовок
                intent,
                getString(R.string.description_share_post)
            )
            startActivity(shareIntent)
        }

        viewModel.playVideo.observe(this) { videoUrl ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        val postContentActivityLauncher =
            registerForActivityResult(NewPostActivity.ResultContract) { postContent ->
                postContent ?: return@registerForActivityResult
                viewModel.onSaveButtonClicked(postContent)
            }

        viewModel.navigateToPostContentScreenEvent.observe(this) { postContent ->
            postContentActivityLauncher.launch(postContent)
        }
    }
}

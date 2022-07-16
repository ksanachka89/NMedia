package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.hideKeyboard
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

        binding.saveButton.setOnClickListener {
            with(binding.contentEditText) {
                val content = text.toString()
                viewModel.onSaveButtonClicked(content)
                clearFocus()
                hideKeyboard()
            }
        }

        binding.cancelButton.setOnClickListener {
            viewModel.onCancelButtonClicked()
            binding.group.visibility = View.GONE
        }

        viewModel.currentPost.observe(this) { currentPost ->
            with(binding.contentEditText) {
                val content = currentPost?.content
                binding.shortTextContent.text = content
                setText(content)
                if (content != null) {
                    requestFocus() // установить фокус
                    binding.group.visibility = View.VISIBLE
                } else {
                    clearFocus()
                    hideKeyboard()
                    binding.group.visibility = View.GONE
                }
            }
        }
    }
}

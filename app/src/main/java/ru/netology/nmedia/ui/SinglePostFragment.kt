package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.SinglePostViewBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.resFormat
import ru.netology.nmedia.viewModel.PostViewModel

class SinglePostFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels()
    private val args by navArgs<SinglePostFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navigateToPostContentScreenEvent.observe(this) { initialContent ->
            val direction =
                SinglePostFragmentDirections.singlePostFragmentToPostContentFragment(
                    initialContent
                ) // навигация
            findNavController().navigate(direction)
        }

        viewModel.playVideo.observe(this) { videoUrl ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = SinglePostViewBinding.inflate(layoutInflater, container, false)
        .also { binding ->
            val postId = args.postId
            viewModel.addSinglePost(postId)

            viewModel.data.observe(viewLifecycleOwner) { posts ->
                posts.firstOrNull { post ->
                    post.id == postId
                }?.let { bind(it, binding) }
            }

            val popupMenu by lazy {
                PopupMenu(context, binding.singlePostView.menu).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                viewModel.onRemoveClickedSinglePost()
                                val direction =
                                    SinglePostFragmentDirections.singlePostFragmentToFeedFragment()
                                findNavController().navigate(direction)
                                true
                            }
                            R.id.edit -> {
                                viewModel.onEditClickedSinglePost()
                                true
                            }
                            else -> false
                        }
                    }
                }
            }

            with(binding.singlePostView) {
                likes.setOnClickListener {
                    viewModel.onLikeClickedSinglePost()
                }
                shares.setOnClickListener {
                    viewModel.onShareClickedSinglePost()
                }
                videoBanner.setOnClickListener {
                    viewModel.onPlayClickedSinglePost()
                }
                playVideoButton.setOnClickListener {
                    viewModel.onPlayClickedSinglePost()
                }
                menu.setOnClickListener { popupMenu.show() }
            }
        }.root

    private fun bind(post: Post, binding: SinglePostViewBinding) {
        with(binding.singlePostView) {
            shares.text = resFormat(post.shares)
            amountOfViews.text = resFormat(post.views)
            likes.text = resFormat(post.likes)

            author.text = post.author
            published.text = post.published
            content.text = post.content
            likes.isChecked = post.likedByMe

            videoGroup.isVisible = post.video != null
        }
    }
}

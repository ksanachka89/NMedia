package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.resFormat

internal class PostsAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder { // создаем View и оборачиваем во ViewHolder
        val inflater =
            LayoutInflater.from(parent.context) // создали inflater из родителя - ViewGroup
        val binding = PostBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position) // вернет пост, ктр находится в этой позиции
        holder.bind(post)
    }

    class ViewHolder(
        private val binding: PostBinding,
        listener: PostInteractionListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post // приватное св-во, ктр когда-то будет инициализировано

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.menu).apply {
                inflate(R.menu.options_post)//будет раздуваться меню options_post
                setOnMenuItemClickListener { menuItem ->//слушатель нажатия на меню
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.likes.setOnClickListener {
                listener.onLikeClicked(post)
            }
            binding.shares.setOnClickListener {
                listener.onShareClicked(post)
            }

            binding.videoBanner.setOnClickListener {
                listener.onPlayVideoClicked(post)
            }
            binding.playVideoButton.setOnClickListener {
                listener.onPlayVideoClicked(post)
            }

            binding.menu.setOnClickListener { popupMenu.show() }

            binding.content.setOnClickListener {
                listener.onPostClicked(post)
            }
            binding.avatar.setOnClickListener {
                listener.onPostClicked(post)
            }
            binding.author.setOnClickListener {
                listener.onPostClicked(post)
            }
            binding.published.setOnClickListener {
                listener.onPostClicked(post)
            }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                shares.text = resFormat(post.shares)
                amountOfViews.text = resFormat(post.views)
                likes.text = resFormat(post.likes)

                author.text = post.author
                published.text = post.published
                content.text = post.content
                likes.isChecked = post.likedByMe

                videoGroup.isVisible = post.video != null // видима, если есть видео
            }
        }
    }

    private object DiffCallBack : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem

    }
}
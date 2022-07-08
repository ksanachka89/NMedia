package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.resFormat

typealias onButtonClicked = (Post) -> Unit

class PostsAdapter(
    private val onLikeClicked: onButtonClicked,
    private val onShareClicked: onButtonClicked
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallBack) {

    class ViewHolder(
        private val binding: PostListItemBinding,
        private val onLikeClicked: onButtonClicked,
        private val onShareClicked: onButtonClicked
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post // приватное св-во, ктр когда-то будет инициализировано

        init {
            binding.likes.setOnClickListener {
                onLikeClicked(post)
            }
            binding.shares.setOnClickListener {
                onShareClicked(post)
            }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                amountOfLikes.text = resFormat(post.likes)
                amountOfShares.text = resFormat(post.shares)
                amountOfViews.text = resFormat(post.views)

                author.text = post.author
                published.text = post.published
                content.text = post.content

                likes.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_baseline_favorite_border_24
                )
            }
        }
    }

    private object DiffCallBack : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem

    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder { // создаем View и оборачиваем во ViewHolder
        val inflater =
            LayoutInflater.from(parent.context) // создали inflater из родителя - ViewGroup
        val binding = PostListItemBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(binding, onLikeClicked, onShareClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position) // вернет пост, ктр находится в этой позиции
        holder.bind(post)
    }
}
package com.example.pencast.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pencast.databinding.CardFeedBinding
import com.example.pencast.ui.article.Article

class FeedAdapter(
    private val feedClickListener: FeedClickListener,
    private val isFavouriteClickListener: FeedClickListener
) :
    ListAdapter<Article,
            FeedAdapter.ViewHolder>(
        FeedDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(feedClickListener, isFavouriteClickListener, item)
    }

    class ViewHolder private constructor(val binding: CardFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            feedClickListener: FeedClickListener,
            isFavouriteClickListener: FeedClickListener,
            item: Article
        ) {
            binding.feed = item
            binding.feedClickListener = feedClickListener
            binding.isFavouriteClickListener = isFavouriteClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardFeedBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class FeedDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.articleId == newItem.articleId
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}

class FeedClickListener(val feedClickListener: (feed: Article) -> Unit) {
    fun onClick(feed: Article) = feedClickListener(feed)
}



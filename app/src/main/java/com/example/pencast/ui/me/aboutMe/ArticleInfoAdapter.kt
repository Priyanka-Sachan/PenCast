package com.example.pencast.ui.me.aboutMe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pencast.databinding.CardAboutMeArticleBinding
import com.example.pencast.ui.article.ArticleInfo

class ArticleInfoAdapter(private val articleInfoClickListener: ArticleInfoClickListener) :
    ListAdapter<ArticleInfo,
            ArticleInfoAdapter.ViewHolder>(ArticleInfoDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, articleInfoClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: CardAboutMeArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ArticleInfo, articleInfoClickListener: ArticleInfoClickListener) {
            binding.articleInfo = item
            binding.articleInfoClickListener = articleInfoClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardAboutMeArticleBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ArticleInfoDiffCallback : DiffUtil.ItemCallback<ArticleInfo>() {
    override fun areItemsTheSame(oldItem: ArticleInfo, newItem: ArticleInfo): Boolean {
        return oldItem.articleId == newItem.articleId
    }

    override fun areContentsTheSame(oldItem: ArticleInfo, newItem: ArticleInfo): Boolean {
        return oldItem == newItem
    }
}

class ArticleInfoClickListener(val articleInfoClickListener: (article: ArticleInfo) -> Unit) {
    fun onClick(article: ArticleInfo) = articleInfoClickListener(article)
}

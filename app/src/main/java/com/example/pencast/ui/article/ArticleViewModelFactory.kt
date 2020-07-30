package com.example.pencast.ui.article

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ArticleViewModelFactory(
    private val application: Application,
    private val article: Article
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(application, article) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
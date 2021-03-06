package com.example.pencast.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pencast.databinding.FragmentArticleBinding


class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentArticleBinding.inflate(inflater)
        val args = ArticleFragmentArgs.fromBundle(requireArguments())
        val articleViewModelFactory =
            ArticleViewModelFactory(requireActivity().application, args.article)
        articleViewModel =
            ViewModelProvider(this, articleViewModelFactory).get(ArticleViewModel::class.java)
        binding.articleViewModel = articleViewModel

        binding.articleAuthorInfo.setOnClickListener {
            findNavController().navigate(
                ArticleFragmentDirections.actionNavigationArticleToNavigationProfile(
                    articleViewModel.article.author
                )
            )
        }
        return binding.root
    }
}
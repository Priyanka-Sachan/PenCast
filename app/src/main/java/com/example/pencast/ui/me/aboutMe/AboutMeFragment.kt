package com.example.pencast.ui.me.aboutMe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.pencast.databinding.FragmentAboutMeBinding
import com.example.pencast.ui.me.MeFragmentDirections

class AboutMeFragment : Fragment() {

    private lateinit var binding: FragmentAboutMeBinding

    companion object {
        fun newInstance() =
            AboutMeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAboutMeBinding.inflate(inflater)
        val aboutMeViewModel = ViewModelProvider(this).get(AboutMeViewModel::class.java)
        binding.aboutMeViewModel = aboutMeViewModel

        val aboutMeArticleWorkAdapter = ArticleInfoAdapter(ArticleInfoClickListener {
            binding.emptyArticleList.visibility = View.GONE
            binding.aboutMeArticleWorkRecyclerView.visibility = View.VISIBLE
            aboutMeViewModel.getArticle(it.articleId)
        })
        binding.aboutMeArticleWorkRecyclerView.adapter = aboutMeArticleWorkAdapter
        aboutMeViewModel.articleWorkList.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                binding.aboutMeArticleWorkRecyclerView.visibility = View.VISIBLE
                binding.emptyArticleList.visibility = View.GONE
                aboutMeArticleWorkAdapter.submitList(it)
            }
        })

        val aboutMeArticleInterestedAdapter = ArticleInfoAdapter(ArticleInfoClickListener {
            binding.emptyInterestedList.visibility = View.GONE
            binding.aboutMeArticleInterestedRecyclerView.visibility = View.VISIBLE
            aboutMeViewModel.getArticle(it.articleId)

        })
        binding.aboutMeArticleInterestedRecyclerView.adapter = aboutMeArticleInterestedAdapter
        aboutMeViewModel.articleInterestedList.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                binding.aboutMeArticleInterestedRecyclerView.visibility=View.VISIBLE
                binding.emptyInterestedList.visibility=View.GONE
                aboutMeArticleInterestedAdapter.submitList(it)
            }
        })

        aboutMeViewModel.article.observe(viewLifecycleOwner, Observer {
            NavHostFragment.findNavController(requireParentFragment()).navigate(
                MeFragmentDirections.actionNavigationMeToNavigationArticle(it)
            )
        })
        return binding.root
    }
}
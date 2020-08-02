package com.example.pencast.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pencast.R

class FeedFragment : Fragment() {

    private lateinit var feedViewModel: FeedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        feedViewModel =
            ViewModelProvider(this).get(FeedViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        val feedRecyclerView: RecyclerView = view.findViewById(R.id.feed_recycler_view)
        val feedAdapter = FeedAdapter(FeedClickListener {
            findNavController().navigate(FeedFragmentDirections.actionNavigationFeedToNavigationArticle(it))
        }, FavouriteFeedClickListener {
           feedViewModel.isFavourite(it)

        })
        feedRecyclerView.adapter = feedAdapter

        feedViewModel.feeds.observe(viewLifecycleOwner, Observer {
            feedAdapter.submitList(it)
        })

        val newArticle =
            view.findViewById<com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton>(
                R.id.create_new_article
            )
        newArticle.setOnClickListener {
            findNavController().navigate(R.id.navigation_add_article)
        }
        return view
    }
}
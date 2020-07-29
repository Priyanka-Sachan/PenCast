package com.example.pencast.ui.addArticle

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.pencast.R
import com.example.pencast.databinding.FragmentAddArticleBinding

class AddArticleFragment : Fragment() {

    private lateinit var addArticleViewModel: AddArticleViewModel

    private lateinit var binding: FragmentAddArticleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddArticleBinding.inflate(inflater)
        addArticleViewModel = ViewModelProvider(this).get(AddArticleViewModel::class.java)

        binding.addArticleSubmit.setOnClickListener {
            if (binding.addArticleDetails.text.isNotEmpty() &&
                binding.addArticleImage.text.isNotEmpty() &&
                binding.addArticleSubmit.text.isNotEmpty() &&
                binding.addArticleSubTitle.text.isNotEmpty()
            )
                addArticleViewModel.submitArticle(
                    binding.addArticleTitle.text.toString(),
                    binding.addArticleSubTitle.text.toString(),
                    binding.addArticleDetails.text.toString(),
                    binding.addArticleImage.text.toString()
                )
        }
        return binding.root
    }
}
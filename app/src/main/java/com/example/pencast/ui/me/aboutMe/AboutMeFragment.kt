package com.example.pencast.ui.me.aboutMe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pencast.databinding.FragmentAboutMeBinding

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
        val aboutMeViewModel=ViewModelProvider(this).get(AboutMeViewModel::class.java)
        return binding.root
    }

}
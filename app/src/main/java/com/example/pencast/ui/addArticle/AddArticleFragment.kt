package com.example.pencast.ui.addArticle

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pencast.databinding.FragmentAddArticleBinding

class AddArticleFragment : Fragment() {

    private val IMAGE_PICKER_REQUEST_CODE = 1

    private lateinit var addArticleViewModel: AddArticleViewModel

    private lateinit var binding: FragmentAddArticleBinding

    private var selectedPhotoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddArticleBinding.inflate(inflater)
        addArticleViewModel = ViewModelProvider(this).get(AddArticleViewModel::class.java)

        binding.addArticleSubmit.setOnClickListener {
            if (binding.addArticleDetails.text?.isNotEmpty() == true &&
                selectedPhotoUri != null &&
                binding.addArticleTitle.text?.isNotEmpty() == true &&
                binding.addArticleSubTitle.text?.isNotEmpty() == true
            ) {
                binding.addArticleLoading.visibility = View.VISIBLE
                addArticleViewModel.submitArticle(
                    binding.addArticleTitle.text.toString(),
                    binding.addArticleSubTitle.text.toString(),
                    binding.addArticleDetails.text.toString(),
                    selectedPhotoUri
                )
            } else
                Toast.makeText(activity, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }

        binding.addArticleImage.setOnClickListener {
            val imagePickerIntent = Intent(Intent.ACTION_PICK)
            imagePickerIntent.type = "image/*"
            startActivityForResult(imagePickerIntent, IMAGE_PICKER_REQUEST_CODE)
        }

        addArticleViewModel.navigateToArticle.observe(viewLifecycleOwner, Observer {
            binding.addArticleLoading.visibility = View.VISIBLE
            findNavController().navigate(
                AddArticleFragmentDirections.actionNavigationAddArticleToNavigationArticle(
                    it
                )
            )
        })
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            try {
                selectedPhotoUri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            activity?.contentResolver,
                            selectedPhotoUri
                        )
                        binding.addArticleImage.setImageBitmap(bitmap)
                    } else {
                        val source = ImageDecoder.createSource(
                            requireActivity().contentResolver,
                            selectedPhotoUri!!
                        )
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        binding.addArticleImage.setImageBitmap(bitmap)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
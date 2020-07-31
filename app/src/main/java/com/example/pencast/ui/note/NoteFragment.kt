package com.example.pencast.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pencast.R
import com.example.pencast.database.NoteDatabase
import com.example.pencast.databinding.FragmentNoteBinding
import kotlinx.coroutines.InternalCoroutinesApi

class NoteFragment : Fragment() {

    private lateinit var binding: FragmentNoteBinding
    private lateinit var noteViewModel: NoteViewModel

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNoteBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        val dataSource = NoteDatabase.getInstance(application).noteDao
        val noteViewModelFactory = NoteViewModelFactory(dataSource, application)
        noteViewModel =
            ViewModelProvider(this, noteViewModelFactory).get(NoteViewModel::class.java)
        binding.noteViewModel = noteViewModel
        binding.lifecycleOwner = this

        binding.saveNoteButton.setOnClickListener {
            if (binding.noteTitle.text.toString()
                    .isNotEmpty() && binding.noteContent.text.toString().isNotEmpty()
            ) {
                val note = Note(
                    System.currentTimeMillis(),
                    binding.noteTitle.text.toString(),
                    binding.noteContent.text.toString(),
                    false,
                    System.currentTimeMillis()
                )
                noteViewModel.saveNote(note)
            }
        }

//        noteViewModel.navigateToNoteList.observe(viewLifecycleOwner, Observer {
//            findNavController().popBackStack(R.id.navigation_note_list, false)
//        })

        binding.noteBottomAppBar.setNavigationOnClickListener {
            Toast.makeText(context, "Navigation clicked", Toast.LENGTH_SHORT).show()
        }

        binding.noteBottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.note_image -> {
                    Toast.makeText(context, "Image clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.note_search -> {
                    Toast.makeText(context, "Search clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        return binding.root
    }
}
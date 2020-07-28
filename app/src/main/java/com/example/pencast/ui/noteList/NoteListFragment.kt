package com.example.pencast.ui.noteList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pencast.R

class NoteListFragment : Fragment() {

    private lateinit var noteListViewModel: NoteListViewModel

    private lateinit var bottomAppBar: com.google.android.material.bottomappbar.BottomAppBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_note_list, container, false)

        noteListViewModel = ViewModelProvider(this).get(NoteListViewModel::class.java)

        bottomAppBar = view.findViewById(R.id.note_list_bottom_app_bar)

        bottomAppBar.setNavigationOnClickListener {
            Toast.makeText(context, "Navigation clicked", Toast.LENGTH_SHORT).show()
        }

        bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.note_list_delete -> {
                    Toast.makeText(context, "Delete clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.note_list_search -> {
                    Toast.makeText(context, "Search clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        val newNote: com.google.android.material.floatingactionbutton.FloatingActionButton =
            view.findViewById(R.id.new_note)
        newNote.setOnClickListener {
            findNavController().navigate(R.id.navigation_note)
        }
        return view
    }
}
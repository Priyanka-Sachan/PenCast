package com.example.pencast.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pencast.R
import com.example.pencast.ui.noteList.NoteListViewModel

class NoteFragment : Fragment() {

    private lateinit var noteViewModel: NoteListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_note, container, false)

        noteViewModel = ViewModelProvider(this).get(NoteListViewModel::class.java)

        val bottomAppBar: com.google.android.material.bottomappbar.BottomAppBar =
            view.findViewById(R.id.note_bottom_app_bar)

        bottomAppBar.setNavigationOnClickListener {
            Toast.makeText(context, "Navigation clicked", Toast.LENGTH_SHORT).show()
        }

        bottomAppBar.setOnMenuItemClickListener { menuItem ->
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
        return view
    }
}
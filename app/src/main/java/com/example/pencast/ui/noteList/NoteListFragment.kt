package com.example.pencast.ui.noteList

import android.os.Bundle
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
import com.example.pencast.database.NoteDatabase
import com.example.pencast.ui.note.Note
import kotlinx.coroutines.InternalCoroutinesApi

class NoteListFragment : Fragment() {

    private lateinit var noteListViewModel: NoteListViewModel

    private lateinit var bottomAppBar: com.google.android.material.bottomappbar.BottomAppBar

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_note_list, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = NoteDatabase.getInstance(application).noteDao
        val noteListViewModelFactory = NoteListViewModelFactory(dataSource, application)
        noteListViewModel =
            ViewModelProvider(this, noteListViewModelFactory).get(NoteListViewModel::class.java)

        val noteListAdapter = NoteListAdapter(NoteClickListener {
            findNavController().navigate(
                NoteListFragmentDirections.actionNavigationNoteListToNavigationNote(
                    it
                )
            )
        })
        val noteRecyclerView = view.findViewById<RecyclerView>(R.id.note_recycler_view)
        noteRecyclerView.adapter = noteListAdapter

        noteListViewModel.noteList.observe(viewLifecycleOwner, Observer {
            noteListAdapter.submitList(it)
        })

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
            findNavController().navigate(
                NoteListFragmentDirections.actionNavigationNoteListToNavigationNote(
                    Note(
                        0,
                        "",
                        "",
                        false,
                        0
                    )
                )
            )
        }
        return view
    }
}
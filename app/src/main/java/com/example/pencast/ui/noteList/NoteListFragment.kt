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
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.example.pencast.R
import com.example.pencast.database.NoteDatabase
import com.example.pencast.databinding.FragmentNoteListBinding
import com.example.pencast.ui.note.Note
import kotlinx.coroutines.InternalCoroutinesApi

class NoteListFragment : Fragment() {

    private lateinit var binding: FragmentNoteListBinding

    private lateinit var noteListViewModel: NoteListViewModel

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNoteListBinding.inflate(inflater)

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

        binding.noteRecyclerView.adapter = noteListAdapter
        val selectionTracker = SelectionTracker.Builder(
            "notes-selection",
            binding.noteRecyclerView,
            NoteKeyProvider(noteListAdapter),
            MyItemDetailsLookup(binding.noteRecyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        noteListAdapter.tracker = selectionTracker

        noteListViewModel.noteList.observe(viewLifecycleOwner, Observer {
            noteListAdapter.submitList(it)
        })

        var selectedNotesList = ArrayList<Note>()
        selectionTracker.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    val list = ArrayList<Note>()
                    selectionTracker.selection.forEach {
                        list.add(noteListViewModel.getNote(it))
                    }
                    selectedNotesList = list
                }
            })

        binding.noteListBottomAppBar.setNavigationOnClickListener {
            Toast.makeText(context, "Navigation clicked", Toast.LENGTH_SHORT).show()
        }

        binding.noteListBottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.note_list_delete -> {
                    selectedNotesList.forEach {
                        noteListViewModel.deleteNote(it)
                    }
                    true
                }
                R.id.note_list_search -> {
                    Toast.makeText(context, "Search clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        binding.newNote.setOnClickListener {
            findNavController().navigate(
                NoteListFragmentDirections.actionNavigationNoteListToNavigationNote(
                    Note(
                        0,
                        "",
                        "",
                        0,
                        false,
                        0
                    )
                )
            )
        }
        return binding.root
    }
}
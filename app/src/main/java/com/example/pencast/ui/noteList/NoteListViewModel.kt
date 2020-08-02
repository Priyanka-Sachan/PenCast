package com.example.pencast.ui.noteList

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.pencast.database.NoteDao
import com.example.pencast.ui.note.Note
import kotlinx.coroutines.*

class NoteListViewModel(var database: NoteDao, var application: Application) : ViewModel() {

    private var noteListViewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + noteListViewModelJob)

    private var _noteList = database.getAllNotes()
    val noteList: LiveData<List<Note>>
        get() = _noteList

    private var currentNote: Note

    init {
        currentNote = Note(0, "", "", 0, false, 0)
    }

    fun getNote(noteId: Long): Note {
        uiScope.launch {
            get(noteId)
        }
        return currentNote
    }

    private suspend fun get(noteId: Long) {
        withContext(Dispatchers.IO) {
            currentNote = database.get(noteId)!!
        }
    }

    fun deleteNote(note: Note) {
        uiScope.launch {
            delete(note)
        }
    }

    private suspend fun delete(note: Note) {
        withContext(Dispatchers.IO) {
            database.delete(note)
        }
    }

    override fun onCleared() {
        super.onCleared()
        noteListViewModelJob.cancel()
    }
}
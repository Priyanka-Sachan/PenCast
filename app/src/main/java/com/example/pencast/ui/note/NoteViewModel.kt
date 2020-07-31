package com.example.pencast.ui.note

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pencast.database.NoteDao
import kotlinx.coroutines.*

class NoteViewModel(var oldNote: Note, var database: NoteDao, var application: Application) :
    ViewModel() {

    private var noteViewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + noteViewModelJob)

    private var _navigateToNoteList = MutableLiveData<Boolean>()
    val navigateToNoteList: LiveData<Boolean>
        get() = _navigateToNoteList

    private var _note = Note(0, " ", " ",0, false, 0)
    val note: Note?
        get() = _note

    init {
        _navigateToNoteList.value = false
        getNote()
    }

    private fun getNote() {
        if (oldNote.noteId != 0L)
            _note = oldNote
    }

    fun saveNote(note: Note) {
        if (oldNote.noteId == 0L)
            insertNote(note)
        else
            updateNote(note)
    }

    private fun insertNote(note: Note) {
        uiScope.launch {
            insert(note)
        }
    }

    private suspend fun insert(note: Note) {
        withContext(Dispatchers.IO) {
            database.insert(note)
        }
        _navigateToNoteList.value = true
    }

    private fun updateNote(note: Note) {
        uiScope.launch {
            update(note)
        }
    }

    private suspend fun update(note: Note) {
        withContext(Dispatchers.IO) {
            database.update(note)
        }
    }

    fun doneNavigating() {
        _navigateToNoteList.value = false
    }

    override fun onCleared() {
        super.onCleared()
        noteViewModelJob.cancel()
    }
}
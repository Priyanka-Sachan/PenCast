package com.example.pencast.ui.noteList

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.pencast.database.NoteDao
import com.example.pencast.ui.note.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class NoteListViewModel(var database: NoteDao, var application: Application) : ViewModel() {

    private var noteListViewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + noteListViewModelJob)

    private var _noteList = database.getAllNotes()
    val noteList: LiveData<List<Note>>
        get() = _noteList

    override fun onCleared() {
        super.onCleared()
        noteListViewModelJob.cancel()
    }
}
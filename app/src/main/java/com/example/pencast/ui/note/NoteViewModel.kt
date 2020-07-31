package com.example.pencast.ui.note

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pencast.database.NoteDao
import kotlinx.coroutines.*

class NoteViewModel(var database: NoteDao, var application: Application) : ViewModel() {

    private var noteViewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + noteViewModelJob)

    private var _navigateToNoteList = MutableLiveData<Boolean>()
    val navigateToNoteList: LiveData<Boolean>
        get() = _navigateToNoteList

    init {
        _navigateToNoteList.value = false
    }

    fun saveNote(note: Note) {
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

    override fun onCleared() {
        super.onCleared()
        noteViewModelJob.cancel()
    }
}
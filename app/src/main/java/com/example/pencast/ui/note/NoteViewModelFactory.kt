package com.example.pencast.ui.note

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pencast.database.NoteDao

class NoteViewModelFactory(
    private val note: Note,
    private val dataSource: NoteDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(note, dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
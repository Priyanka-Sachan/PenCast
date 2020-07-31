package com.example.pencast.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pencast.ui.note.Note


@Dao
interface NoteDao {
    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("DELETE FROM notes_table")
    fun deleteAllNotes()

    @Query("SELECT * FROM notes_table WHERE noteId=:key")
    fun get(key: Long): Note?

    @Query("SELECT * FROM notes_table ORDER BY noteId DESC")
    fun getAllNotes(): LiveData<List<Note>>

}

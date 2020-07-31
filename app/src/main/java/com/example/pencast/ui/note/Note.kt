package com.example.pencast.ui.note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey
    var noteId: Long,
    var title: String,
    var text: String,
    var isSynced: Boolean,
    var lastUpdated: Long
)
package com.example.pencast.ui.note

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey
    var noteId: Long,
    var title: String,
    var text: String,
    var group:Int,
    var isSynced: Boolean,
    var lastUpdated: Long
) : Parcelable {
    constructor() : this(0, "", "", 0,false, 0)
}
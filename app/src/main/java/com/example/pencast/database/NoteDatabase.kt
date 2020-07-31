package com.example.pencast.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pencast.ui.note.Note
import kotlinx.coroutines.InternalCoroutinesApi

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
    //companion object so that we can use its methods without instancing the class
    companion object {
        //Value of instance is up-to-date,these variables will never be cached..and all writes and reads will be done from main memory
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        @InternalCoroutinesApi
        fun getInstance(context: Context): NoteDatabase {
            kotlinx.coroutines.internal.synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDatabase::class.java,
                        "note_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}

package com.olabode.wilson.daggernoteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.olabode.wilson.daggernoteapp.models.Note

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes_table WHERE trash == 0")
    fun getAllNotes(): LiveData<List<Note>>


    @Query("SELECT * FROM notes_table WHERE favourite == 1")
    fun getFavouriteNotes(): LiveData<List<Note>>


    @Query("SELECT * FROM notes_table WHERE trash == 1")
    fun getTrashedNotes(): LiveData<List<Note>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)


    @Delete
    suspend fun deleteNote(note: Note)


    @Update
    suspend fun upDateNote(note: Note)
}
package com.olabode.wilson.daggernoteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.olabode.wilson.daggernoteapp.models.Note

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */


@Dao
interface NotesDao {

    //READ
    @Query("SELECT * FROM notes_table WHERE trash == 0 ORDER BY created_date DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE favourite == 1")
    fun getFavouriteNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE trash == 1")
    fun getTrashedNotes(): LiveData<List<Note>>

    //CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    // DELETE
    @Delete
    suspend fun deleteNote(note: Note)

    @Update
    suspend fun upDateNote(note: Note)

    @Query("DELETE FROM notes_table WHERE trash == 1")
    suspend fun emptyTrash()

    @Query("SELECT COUNT(id) FROM notes_table WHERE trash = 0")
    fun countNoteTable(): LiveData<Int>

    @Query("SELECT COUNT(id) FROM notes_table WHERE favourite =  1 ")
    fun countFavouriteNotes(): LiveData<Int>

    @Query("SELECT COUNT(id) FROM notes_table WHERE trash =  1 ")
    fun countTrashNotes(): LiveData<Int>
}
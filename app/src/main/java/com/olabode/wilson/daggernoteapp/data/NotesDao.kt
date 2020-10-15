package com.olabode.wilson.daggernoteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.olabode.wilson.daggernoteapp.models.Note

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes_table WHERE trash == 0 ORDER BY LOWER(title)  ASC")
    fun getNotesByName(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE trash == 0 ORDER BY date_last_modified  DESC")
    fun getNotesByLastModified(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE trash == 0 ORDER BY created_date  DESC")
    fun getNotesByDateCreated(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE favourite == 1 AND  trash == 0 ORDER BY LOWER(title)  ASC")
    fun getFavNotesByName(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE favourite == 1 AND  trash == 0 ORDER BY date_last_modified  DESC")
    fun getFavNotesByLastModified(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE favourite == 1 AND trash == 0 ORDER BY created_date  DESC")
    fun getFavNotesByDateCreated(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE trash == 1")
    fun getTrashedNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE trash == 1 ORDER BY date_trashed ASC")
    fun getTrashedNotesByDateAddedRecent(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE trash == 1 ORDER BY date_trashed DESC")
    fun getTrashedNotesByDateAddedOlder(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long

    @Query("SELECT * FROM notes_table WHERE noteId =:id")
    suspend fun getNoteById(id: Long): Note

    @Delete
    suspend fun deleteNote(note: Note)

    @Update
    suspend fun upDateNote(note: Note)

    @Query("DELETE FROM notes_table WHERE trash == 1")
    suspend fun emptyTrash()

    @Query("SELECT COUNT(noteId) FROM notes_table WHERE trash = 0")
    fun countNoteTable(): LiveData<Int>

    @Query("SELECT COUNT(noteId) FROM notes_table WHERE favourite =  1 ")
    fun countFavouriteNotes(): LiveData<Int>

    @Query("SELECT COUNT(noteId) FROM notes_table WHERE trash =  1 ")
    fun countTrashNotes(): LiveData<Int>
}

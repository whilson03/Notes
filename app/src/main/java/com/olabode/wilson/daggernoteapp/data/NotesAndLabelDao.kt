package com.olabode.wilson.daggernoteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.olabode.wilson.daggernoteapp.models.LabelsWithNote
import com.olabode.wilson.daggernoteapp.models.NotesAndLabelCrossRef
import com.olabode.wilson.daggernoteapp.models.NotesWithLabel

/**
 *   Created by OLABODE WILSON on 2020-04-01.
 */

@Dao
interface NotesAndLabelDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(join: NotesAndLabelCrossRef)

    @Delete
    suspend fun delete(join: NotesAndLabelCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMultipleLabelsAndNote(join: List<NotesAndLabelCrossRef>)

    @Transaction
    @Query("SELECT * FROM label WHERE labelId =:id")
    fun getNotesByLabel(id: Long): LiveData<List<LabelsWithNote>>

    @Transaction
    @Query("SELECT * FROM notes_table WHERE noteId =:id")
    fun getLabelsByNote(id: Long): LiveData<List<NotesWithLabel>>
}
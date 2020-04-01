package com.olabode.wilson.daggernoteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.olabode.wilson.daggernoteapp.models.LabelsWithNote
import com.olabode.wilson.daggernoteapp.models.NotesAndLabelCrossRef

/**
 *   Created by OLABODE WILSON on 2020-04-01.
 */

@Dao
interface NotesAndLabelDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(join: NotesAndLabelCrossRef)


    @Transaction
    @Query("SELECT * FROM label")
    fun getNotesByLabel(): LiveData<List<LabelsWithNote>>
}
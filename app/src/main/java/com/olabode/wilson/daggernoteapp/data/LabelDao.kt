package com.olabode.wilson.daggernoteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.olabode.wilson.daggernoteapp.models.Label
import com.olabode.wilson.daggernoteapp.models.LabelsWithNote

/**
 *   Created by OLABODE WILSON on 2020-03-29.
 */
@Dao
interface LabelDao {

    @Transaction
    @Query("SELECT * FROM Label")
    fun getLabelsWithNote(): LiveData<List<LabelsWithNote>>


    @Query("SELECT * FROM LABEL")
    fun getAllLabels(): LiveData<List<Label>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLabel(label: Label)

    @Delete
    suspend fun deleteLabel(label: Label)

    @Update
    suspend fun updateLabel(label: Label)
}
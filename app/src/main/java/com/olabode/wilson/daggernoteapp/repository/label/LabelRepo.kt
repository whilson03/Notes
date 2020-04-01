package com.olabode.wilson.daggernoteapp.repository.label

import androidx.lifecycle.LiveData
import com.olabode.wilson.daggernoteapp.models.Label

/**
 *   Created by OLABODE WILSON on 2020-03-30.
 */

interface LabelRepo {

    fun getAllLabels(): LiveData<List<Label>>

    suspend fun insertLabel(label: Label)

    suspend fun updateLabel(label: Label)

    suspend fun deleteLabel(label: Label)
}
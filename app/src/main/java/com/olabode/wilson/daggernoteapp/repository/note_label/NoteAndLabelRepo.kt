package com.olabode.wilson.daggernoteapp.repository.note_label

import androidx.lifecycle.LiveData
import com.olabode.wilson.daggernoteapp.models.LabelsWithNote
import com.olabode.wilson.daggernoteapp.models.NotesAndLabelCrossRef

/**
 *   Created by OLABODE WILSON on 2020-04-01.
 */


interface NoteAndLabelRepo {
    suspend fun insert(notesWithLabel: NotesAndLabelCrossRef)

    fun getNotesByLabel(): LiveData<List<LabelsWithNote>>
}
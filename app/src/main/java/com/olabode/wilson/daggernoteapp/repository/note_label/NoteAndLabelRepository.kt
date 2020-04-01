package com.olabode.wilson.daggernoteapp.repository.note_label

import androidx.lifecycle.LiveData
import com.olabode.wilson.daggernoteapp.data.NotesAndLabelDao
import com.olabode.wilson.daggernoteapp.models.LabelsWithNote
import com.olabode.wilson.daggernoteapp.models.NotesAndLabelCrossRef
import com.olabode.wilson.daggernoteapp.models.NotesWithLabel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 *   Created by OLABODE WILSON on 2020-04-01.
 */

@Singleton
class NoteAndLabelRepository @Inject constructor(private val notesAndLabelDao: NotesAndLabelDao) :
    NoteAndLabelRepo {

    override suspend fun insert(notesWithLabel: NotesAndLabelCrossRef) {
        withContext(Dispatchers.IO) {
            notesAndLabelDao.insert(notesWithLabel)
        }
    }

    override fun getNotesByLabel(id: Long): LiveData<List<LabelsWithNote>> {
        return notesAndLabelDao.getNotesByLabel(id)
    }

    override fun getLabelsByNote(noteId: Long): LiveData<List<NotesWithLabel>> {
        return notesAndLabelDao.getLabelsByNote(noteId)
    }
}
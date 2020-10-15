package com.olabode.wilson.daggernoteapp.ui.labelview

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.models.LabelsWithNote
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.repository.NotesRepository
import com.olabode.wilson.daggernoteapp.repository.note_label.NoteAndLabelRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 *   Created by OLABODE WILSON on 4/12/20.
 */
class LabelNoteViewModel @Inject constructor(
    private val noteAndLabelRepository: NoteAndLabelRepository,
    private val repository: NotesRepository
) : ViewModel() {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun getAllNotesForLabel(labelId: Long): LiveData<List<LabelsWithNote>> {
        return noteAndLabelRepository.getNotesByLabel(labelId)
    }

    fun addRemoveFavourite(note: Note) {
        uiScope.launch {
            if (note.isFavourite) {
                repository.removeFromFavourite(note)
            } else {
                repository.addToFavourite(note)
            }
        }
    }

    fun moveToTrash(note: Note) {
        uiScope.launch {
            note.trashedDate = Date()
            repository.addToTrash(note)
        }
    }

    fun undoMoveToTrash(note: Note) {
        uiScope.launch {
            note.trashedDate = null
            repository.removeFromTrash(note)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}

package com.olabode.wilson.daggernoteapp.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.models.LabelsWithNote
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.models.NotesAndLabelCrossRef
import com.olabode.wilson.daggernoteapp.models.NotesWithLabel
import com.olabode.wilson.daggernoteapp.repository.NotesRepository
import com.olabode.wilson.daggernoteapp.repository.note_label.NoteAndLabelRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteViewModel @Inject constructor(
    private val repository: NotesRepository,
    private val noteAndLabelRepository: NoteAndLabelRepository
) : ViewModel() {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    var oldTitle: String = ""
    var oldBody: String = ""


    fun saveNewNote(note: Note) {
        uiScope.launch {
            repository.insertNote(note)
        }
    }


    fun getAllLabelsForNote(noteId: Long): LiveData<List<NotesWithLabel>> {
        return noteAndLabelRepository.getLabelsByNote(noteId)
    }


    fun getAllNotesForLabel(labelId: Long): LiveData<List<LabelsWithNote>> {
        return noteAndLabelRepository.getNotesByLabel(labelId)
    }


    fun addNoteToLabel(labelId: Long, noteId: Long) {
        uiScope.launch {
            noteAndLabelRepository.insert(NotesAndLabelCrossRef(labelId, noteId))
        }
    }


    fun updateNote(note: Note) {
        uiScope.launch {
            repository.updateNote(note)
        }
    }

    fun resetFields() {
        oldTitle = ""
        oldBody = ""
    }

    override fun onCleared() {
        super.onCleared()
        resetFields()
    }
}

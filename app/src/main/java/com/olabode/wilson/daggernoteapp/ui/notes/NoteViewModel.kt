package com.olabode.wilson.daggernoteapp.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.models.NotesAndLabelCrossRef
import com.olabode.wilson.daggernoteapp.models.NotesWithLabel
import com.olabode.wilson.daggernoteapp.repository.NotesRepository
import com.olabode.wilson.daggernoteapp.repository.label.LabelRepository
import com.olabode.wilson.daggernoteapp.repository.note_label.NoteAndLabelRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class NoteViewModel @Inject constructor(
    private val repository: NotesRepository,
    private val noteAndLabelRepository: NoteAndLabelRepository,
    labelRepository: LabelRepository
) : ViewModel() {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    val allLabels = labelRepository.getAllLabels()

    var oldTitle: String = ""
    var oldBody: String = ""

    lateinit var noteToUpdate: Note
    var isNewNoteCreated: Boolean = false

    private val _newNoteId = MutableLiveData<Long>()
    val newNoteId: LiveData<Long>
        get() = _newNoteId

    var note = Note(
        title = "",
        body = "",
        dateLastUpdated = Date(),
        dateCreated = Date()
    )

    fun saveNewNote(note: Note) {
        uiScope.launch {
            val noteId = repository.insertNote(note)
            _newNoteId.value = noteId
            noteToUpdate = repository.getNoteById(noteId)
        }
    }

    fun getAllLabelsForNote(noteId: Long): LiveData<List<NotesWithLabel>> {
        return noteAndLabelRepository.getLabelsByNote(noteId)
    }

    fun addNoteToLabel(labelId: Long, noteId: Long) {
        uiScope.launch {
            noteAndLabelRepository.insert(NotesAndLabelCrossRef(labelId, noteId))
        }
    }

    fun removeNoteFromLabel(labelId: Long, noteId: Long) {
        uiScope.launch {
            noteAndLabelRepository.removeLabelFromNote(NotesAndLabelCrossRef(labelId, noteId))
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
        isNewNoteCreated = false
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        resetFields()
    }

    fun delete(note: Note) {
        uiScope.launch {
            repository.deleteNote(note)
        }
    }
}

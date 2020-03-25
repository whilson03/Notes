package com.olabode.wilson.daggernoteapp.ui.trash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.repository.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrashViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel() {
    val trashList: LiveData<List<Note>> = repository.getAllTrashNotes()
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun deleteAllNotesFromTrash() {
        uiScope.launch {
            repository.deleteAllFromTrash()
        }
    }


    fun deleteNote(note: Note) {
        uiScope.launch {
            repository.deleteNote(note)
        }
    }

    fun removeFromTrash(note: Note) {
        uiScope.launch {
            repository.removeFromTrash(note)
        }
    }

}

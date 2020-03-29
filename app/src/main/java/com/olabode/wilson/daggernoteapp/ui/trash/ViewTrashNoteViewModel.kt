package com.olabode.wilson.daggernoteapp.ui.trash

import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.repository.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewTrashNoteViewModel @Inject constructor(private val repository: NotesRepository) :
    ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)


    fun removeFromTrash(note: Note) {
        uiScope.launch {
            note.trashedDate = null
            repository.removeFromTrash(note)
        }
    }

    fun delete(note: Note) {
        uiScope.launch {
            repository.deleteNote(note)
        }
    }

}

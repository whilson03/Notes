package com.olabode.wilson.daggernoteapp.ui.notes

import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.repository.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)


    fun saveNewNote(note: Note) {
        uiScope.launch {
            repository.insertNote(note)
        }
    }


    fun updateNote(note: Note) {
        uiScope.launch {
            repository.updateNote(note)
        }
    }
}

package com.olabode.wilson.daggernoteapp.ui.trash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.repository.NotesRepository
import javax.inject.Inject

class TrashViewModel @Inject constructor(repository: NotesRepository) : ViewModel() {
    val trashList: LiveData<List<Note>> = repository.getAllTrashNotes()
}

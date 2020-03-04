package com.olabode.wilson.daggernoteapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.repository.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    val notesList: LiveData<List<Note>> = repository.getAllNotes()

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}
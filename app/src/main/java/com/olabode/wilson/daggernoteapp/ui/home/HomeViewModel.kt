package com.olabode.wilson.daggernoteapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.repository.NotesRepository
import com.olabode.wilson.daggernoteapp.utils.Util.SORT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


class HomeViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val defaultNotesList: LiveData<List<Note>> = repository.getAllNotes()

    private val notesOrderByName = repository.getNotesByName()

    private val noteByLastModified = repository.getNotesByLastModified()

    private val notesByDateCreated = repository.getNotesByDateCreated()

    private val _sortOrder = MutableLiveData<SORT>()

    private val sortOrder: LiveData<SORT>
        get() = _sortOrder

    init {
        _sortOrder.value = SORT.DEFAULT
    }


    fun getNotes(): LiveData<List<Note>> {
        return Transformations.switchMap(sortOrder) {
            sortNotes(it)
        }
    }


    private fun sortNotes(sort: SORT): LiveData<List<Note>> {
        return when (sort) {
            SORT.DATE_LAST_MODIFIED -> {
                noteByLastModified
            }
            SORT.NAME -> {
                notesOrderByName
            }
            SORT.DATE_CREATED -> {
                notesByDateCreated
            }
            else -> {
                defaultNotesList
            }
        }
    }

    fun addRemoveFavourite(note: Note) {
        uiScope.launch {
            if (note.isFavourite == 1) {
                repository.removeFromFavourite(note)
            } else {
                repository.addToFavourite(note)
            }
        }
    }


    fun moveToTrash(note: Note) {
        uiScope.launch { repository.addToTrash(note) }
    }

    fun undoMoveToTrash(note: Note) {
        uiScope.launch { repository.removeFromTrash(note) }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun filter(filter: SORT) {
        _sortOrder.value = filter
    }

}
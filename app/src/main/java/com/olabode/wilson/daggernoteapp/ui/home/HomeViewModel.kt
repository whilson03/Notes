package com.olabode.wilson.daggernoteapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.data.Result
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.repository.NotesRepository
import com.olabode.wilson.daggernoteapp.utils.Util.SORT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


class HomeViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val notesOrderByName = repository.getNotesByName()

    private val noteByLastModified = repository.getNotesByLastModified()

    private val notesByDateCreated = repository.getNotesByDateCreated()

    private val _sortOrder = MutableLiveData<SORT>()

    private val sortOrder: LiveData<SORT>
        get() = _sortOrder

    init {
        _sortOrder.value = SORT.DATE_CREATED
    }


    fun getNotes(): LiveData<Result<List<Note>>> {
        return Transformations.switchMap(sortOrder) {
            sortNotes(it)
        }
    }


    private fun sortNotes(sort: SORT): LiveData<Result<List<Note>>> {
        return when (sort) {
            SORT.DATE_LAST_MODIFIED -> {
                noteByLastModified
            }
            SORT.NAME -> {
                notesOrderByName
            }
            else -> {
                notesByDateCreated
            }
        }
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

    fun filter(filter: SORT) {
        _sortOrder.value = filter
    }

}
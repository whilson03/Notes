package com.olabode.wilson.daggernoteapp.ui.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.data.Result
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.repository.NotesRepository
import com.olabode.wilson.daggernoteapp.utils.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class FavouritesViewModel @Inject constructor(private val repository: NotesRepository) :
    ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val favNotesOrderByName = repository.getFavNotesByName()

    private val favNoteByLastModified = repository.getFavNotesByLastModified()

    private val favNotesByDateCreated = repository.getFavNotesByDateCreated()

    private val _sortOrder = MutableLiveData<Util.SORT>()

    private val sortOrder: LiveData<Util.SORT>
        get() = _sortOrder

    init {
        _sortOrder.value = Util.SORT.DATE_CREATED
    }

    fun getAllFavouriteNotes(): LiveData<Result<List<Note>>> {
        return Transformations.switchMap(sortOrder) {
            sortNotes(it)
        }
    }

    private fun sortNotes(sort: Util.SORT): LiveData<Result<List<Note>>> {
        return when (sort) {
            Util.SORT.DATE_LAST_MODIFIED -> {
                favNoteByLastModified
            }
            Util.SORT.NAME -> {
                favNotesOrderByName
            }
            Util.SORT.DATE_CREATED -> {
                favNotesByDateCreated
            }
            else -> {
                favNotesByDateCreated
            }
        }
    }

    fun filter(filter: Util.SORT) {
        _sortOrder.value = filter
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
            note.isFavourite = false
            note.trashedDate = Date()
            repository.addToTrash(note)
        }
    }

    fun undoMoveToTrash(note: Note) {
        uiScope.launch {
            note.isFavourite = true
            note.trashedDate = null
            repository.removeFromTrash(note)
        }
    }
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}

package com.olabode.wilson.daggernoteapp.ui.trash

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
import javax.inject.Inject

class TrashViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel() {
    private val trashListByOlder: LiveData<Result<List<Note>>> =
        repository.getTrashedNotesByDateAddedOlder()
    private val trashListByRecent = repository.getAllTrashedNotesByDateAddedRecent()

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val _sortOrder = MutableLiveData<Util.SORT>()

    private val sortOrder: LiveData<Util.SORT>
        get() = _sortOrder

    init {
        _sortOrder.value = Util.SORT.DATE_ADDED_TO_TRASH_RECENT
    }

    fun getTrashedNotes(): LiveData<Result<List<Note>>> {
        return Transformations.switchMap(sortOrder) {
            sortNotes(it)
        }
    }


    private fun sortNotes(sort: Util.SORT): LiveData<Result<List<Note>>> {
        return when (sort) {
            Util.SORT.DATE_ADDED_TO_TRASH_RECENT -> {
                trashListByRecent
            }
            else -> {
                trashListByOlder
            }
        }
    }


    fun filter(filter: Util.SORT) {
        _sortOrder.value = filter
    }

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
            note.trashedDate = null
            repository.removeFromTrash(note)
        }
    }

}

package com.olabode.wilson.daggernoteapp.ui.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.repository.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouritesViewModel @Inject constructor(private val repository: NotesRepository) :
    ViewModel() {
    val favouritesList: LiveData<List<Note>> = repository.getAllFavouriteNotes()
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

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
        uiScope.launch {
            repository.addToTrash(note)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}

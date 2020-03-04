package com.olabode.wilson.daggernoteapp.ui.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.repository.NotesRepository
import javax.inject.Inject

class FavouritesViewModel @Inject constructor(repository: NotesRepository) : ViewModel() {
    val favouritesList: LiveData<List<Note>> = repository.getAllFavouriteNotes()

}

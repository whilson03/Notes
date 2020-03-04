package com.olabode.wilson.daggernoteapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.repository.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

class HomeViewModel @Inject constructor(repository: NotesRepository) : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    val notesList: LiveData<List<Note>> = repository.getAllNotes()


//    init {
//        uiScope.launch {
//            withContext(Dispatchers.IO){
//                repository.insert(Note(2,"hello","wassup",
//                    0,0,Date(12),Date(2)))
//                repository.insert(Note(4,"hello","wassup",
//                    0,0,Date(12),Date(2)))
//                repository.insert(Note(7,"hello","wassup",
//                    0,0,Date(12),Date(2)))
//                repository.insert(Note(21,"hello","wassup",
//                    0,0,Date(12),Date(2)))
//                repository.insert(Note(22,"hello","wassup",
//                    0,0,Date(12),Date(2)))
//                repository.insert(Note(23,"hello","wassup",
//                    0,0,Date(12),Date(2)))
//                repository.insert(Note(25,"hello","wassup",
//                    0,0,Date(12),Date(2)))
//
//            }
//        }
//    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}
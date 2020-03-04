package com.olabode.wilson.daggernoteapp.repository

import androidx.lifecycle.LiveData
import com.olabode.wilson.daggernoteapp.models.Note

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */

interface NoteRepo {
    fun getAllNotes(): LiveData<List<Note>>
}
package com.olabode.wilson.daggernoteapp.adapters.listeners

import com.olabode.wilson.daggernoteapp.models.Note

class NoteClickListener(val clickListener: (position: Int, note: Note) -> Unit) {

    fun onClick(position: Int, note: Note) = clickListener(position, note)
}
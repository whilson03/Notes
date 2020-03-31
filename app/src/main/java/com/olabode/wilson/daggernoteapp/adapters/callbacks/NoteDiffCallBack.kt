package com.olabode.wilson.daggernoteapp.adapters.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.olabode.wilson.daggernoteapp.models.Note

class NoteDiffCallBack : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.noteId == newItem.noteId
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}

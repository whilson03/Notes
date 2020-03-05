package com.olabode.wilson.daggernoteapp.adapters.callbacks

import com.olabode.wilson.daggernoteapp.models.Note
import smartadapter.widget.DiffUtilExtension


class NoteDiffCallBack : DiffUtilExtension.DiffPredicate<Note> {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}


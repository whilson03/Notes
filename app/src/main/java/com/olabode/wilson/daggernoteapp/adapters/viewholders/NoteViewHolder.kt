package com.olabode.wilson.daggernoteapp.adapters.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.olabode.wilson.daggernoteapp.databinding.ItemNoteBinding
import com.olabode.wilson.daggernoteapp.models.Note


/**
 *   Created by OLABODE WILSON on 2020-03-01.
 */

class NoteViewHolder private constructor(val binding: ItemNoteBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Note, position: Int) {
        binding.note = item
        binding.position = position
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): NoteViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemNoteBinding.inflate(
                layoutInflater
                , parent, false
            )
            return NoteViewHolder(
                binding
            )
        }
    }

}
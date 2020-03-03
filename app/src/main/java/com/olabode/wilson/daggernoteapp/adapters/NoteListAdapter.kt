package com.olabode.wilson.daggernoteapp.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.olabode.wilson.daggernoteapp.adapters.callbacks.NoteDiffCallBack
import com.olabode.wilson.daggernoteapp.adapters.viewholders.NoteViewHolder
import com.olabode.wilson.daggernoteapp.models.Note

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */
class NoteListAdapter : RecyclerView.Adapter<NoteViewHolder>() {

    private val DIFF_CALLBACK = NoteDiffCallBack()

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item, position)
    }

    fun submitList(list: List<Note>?) {
        differ.submitList(list)
    }
}




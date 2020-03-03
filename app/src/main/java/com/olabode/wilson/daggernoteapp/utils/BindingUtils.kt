package com.olabode.wilson.daggernoteapp.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.olabode.wilson.daggernoteapp.adapters.NoteListAdapter
import com.olabode.wilson.daggernoteapp.models.Note

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */

@BindingAdapter("listNotess")
fun bindVideosRecyclerView(recyclerView: RecyclerView, data: List<Note>?) {
    val adapter = recyclerView.adapter as NoteListAdapter
    adapter.submitList(data)
    recyclerView.smoothScrollToPosition(0)
}

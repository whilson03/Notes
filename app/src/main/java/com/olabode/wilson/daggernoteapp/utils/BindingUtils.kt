package com.olabode.wilson.daggernoteapp.utils

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.olabode.wilson.daggernoteapp.adapters.NoteListAdapter
import com.olabode.wilson.daggernoteapp.models.Note

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */

@BindingAdapter("listNotes")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Note>?) {
    val adapter = recyclerView.adapter as NoteListAdapter
    adapter.submitList(data)
    recyclerView.smoothScrollToPosition(0)
}

/**
 * Binding adapter used to hide the spinner once data is available
 */
@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

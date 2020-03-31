package com.olabode.wilson.daggernoteapp.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.models.Note

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */

/**
 * Binding adapter used to hide the spinner once data is available
 */
@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}


@BindingAdapter("favourite")
fun ImageView.bindIcon(note: Note) {
    if (note.noteId.toInt() == 1) {
        setImageResource(R.drawable.ic_star_filled)
    } else {
        setImageResource(R.drawable.ic_star_border)
    }
}
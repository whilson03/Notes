package com.olabode.wilson.daggernoteapp.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
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


fun Context.showToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

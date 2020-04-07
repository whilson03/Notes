package com.olabode.wilson.daggernoteapp.ui.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.utils.Util

/**
 *   Created by OLABODE WILSON on 2020-03-19.
 */
class NoteDialog(val note: Note) : AppCompatDialogFragment() {

    // Use this instance of the interface to deliver action events
    private lateinit var listener: NoteDialogListener

    interface NoteDialogListener {
        fun onNoteOptionClick(note: Note, action: Util.ACTION)
    }

    fun setNoteDialogClickListener(listener: NoteDialogListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            retainInstance = true
            // Use the Builder class for convenient dialog construction
            val builder = MaterialAlertDialogBuilder(it)
            builder.setTitle(getString(R.string.more))
                .setItems(R.array.more_options,
                    DialogInterface.OnClickListener { _, which ->
                        listener.onNoteOptionClick(note, getAction(which))
                        dismiss()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun getAction(index: Int) = when (index) {
        0 -> Util.ACTION.SHARE
        1 -> Util.ACTION.DELETE
        else -> Util.ACTION.COPY
    }

}
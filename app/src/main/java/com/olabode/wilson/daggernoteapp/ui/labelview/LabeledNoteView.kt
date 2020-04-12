package com.olabode.wilson.daggernoteapp.ui.labelview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.olabode.wilson.daggernoteapp.R
import dagger.android.support.DaggerFragment

/**
 * A simple [Fragment] subclass.
 */
class LabeledNoteView : DaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_labeled_note_view, container, false)
    }

}

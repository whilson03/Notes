package com.olabode.wilson.daggernoteapp.ui.labelview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.olabode.wilson.daggernoteapp.R

class LabeledNote : Fragment() {

    companion object {
        fun newInstance() = LabeledNote()
    }

    private lateinit var viewModel: LabeledNoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.labeled_note_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LabeledNoteViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

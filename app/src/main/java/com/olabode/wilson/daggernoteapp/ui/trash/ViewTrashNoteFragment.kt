package com.olabode.wilson.daggernoteapp.ui.trash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.olabode.wilson.daggernoteapp.R

class ViewTrashNoteFragment : Fragment() {

    companion object {
        fun newInstance() = ViewTrashNoteFragment()
    }

    private lateinit var viewModel: ViewTrashNoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_trash_note_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ViewTrashNoteViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

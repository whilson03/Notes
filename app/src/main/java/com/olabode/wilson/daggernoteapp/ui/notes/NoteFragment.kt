package com.olabode.wilson.daggernoteapp.ui.notes

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.databinding.NoteFragmentBinding
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.item_note.*
import java.util.*
import javax.inject.Inject

class NoteFragment : DaggerFragment() {

    companion object {
        fun newInstance() = NoteFragment()
    }

    private lateinit var viewModel: NoteViewModel
    private lateinit var binding: NoteFragmentBinding

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NoteFragmentBinding.inflate(inflater, container, false)
        note = NoteFragmentArgs.fromBundle(arguments!!).note
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(NoteViewModel::class.java)


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.notes_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                if (note == null) {
                    performSave()
                } else {
                    updateNote(note!!)
                }

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun updateNote(note: Note) {
        if (binding.note.text.toString().trim().isEmpty()) {
            showToastMessage("Note Cannot be Blank")
            return
        } else {
            note.title =
                if (binding.title.text.toString().isEmpty()) "No title" else title.text.toString()
            note.body = binding.note.text.toString()
            note.dateLastUpdated = Date()

            viewModel.updateNote(note)
            showToastMessage("updating...")
            this.findNavController().navigateUp()
        }

    }

    private fun performSave() {
        if (binding.note.text.toString().trim().isEmpty()) {
            showToastMessage("Note Cannot be Blank")
            return
        } else {
            val note = Note(
                title = if (binding.title.text.toString().isEmpty()) "No title" else title.text.toString(),
                body = binding.note.text.toString(), dateCreated = Date(), dateLastUpdated = Date()
            )
            viewModel.saveNewNote(note)
            showToastMessage("Saving...")
            this.findNavController().navigateUp()
        }
    }


    private fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

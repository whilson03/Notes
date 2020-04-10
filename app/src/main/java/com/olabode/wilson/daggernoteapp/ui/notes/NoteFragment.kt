package com.olabode.wilson.daggernoteapp.ui.notes

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.adapters.LabelChipListAdapter
import com.olabode.wilson.daggernoteapp.databinding.NoteFragmentBinding
import com.olabode.wilson.daggernoteapp.labels.dialog.LabelDialog
import com.olabode.wilson.daggernoteapp.models.Label
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import java.util.*
import javax.inject.Inject


class NoteFragment : DaggerFragment() {

    companion object {
        fun newInstance() = NoteFragment()
    }

    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var viewModel: NoteViewModel
    private lateinit var binding: NoteFragmentBinding
    private var allLabels: List<Label> = mutableListOf()
    private var currentNoteLabels: List<Label> = mutableListOf()

    private var newNoteId: Long? = null
    private var currentNote: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NoteFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        setFont()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(NoteViewModel::class.java)
        binding.chipsRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val chipsAdapter = LabelChipListAdapter()
        binding.chipsRecyclerView.adapter = chipsAdapter

        currentNote = NoteFragmentArgs.fromBundle(arguments!!).note

        if (currentNote == null && !viewModel.isNewNoteCreated) {
            viewModel.saveNewNote(viewModel.note)
            viewModel.isNewNoteCreated = true
        }

        if (currentNote != null) {
            viewModel.noteToUpdate = currentNote!!
            setUpNote(viewModel.noteToUpdate)

            viewModel.getAllLabelsForNote(viewModel.noteToUpdate.noteId).observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer { noteWithLabel ->
                    noteWithLabel?.let {
                        currentNoteLabels = it[0].labels
                        chipsAdapter.submitList(it[0].labels)
                    }
                })
        }


        viewModel.allLabels.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let { allLabels = it }
        })


        viewModel.newNoteId.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                showToastMessage(it.toString())
                newNoteId = it
                viewModel.getAllLabelsForNote(it).observe(
                    viewLifecycleOwner,
                    androidx.lifecycle.Observer { noteWithLabel ->
                        noteWithLabel?.let { l ->
                            currentNoteLabels = l[0].labels
                            chipsAdapter.submitList(l[0].labels)
                        }
                    })
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.notes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                performSave(viewModel.noteToUpdate)
            }
            R.id.share_note -> {

            }
            android.R.id.home -> {

            }

            R.id.copy_note -> {

            }

            R.id.tag -> {
                performLabelAction()
            }

        }
        return super.onOptionsItemSelected(item)
    }


    private fun performLabelAction() {
        val dialog = LabelDialog(currentNoteLabels, allLabels)
        fragmentManager?.let { it1 -> dialog.show(it1, "LabelDialogFragment") }
        dialog.setOnLabelActionListener(object : LabelDialog.LabelActionListener {

            override fun onActionAddLabelToNote(label: Label) {
                viewModel.addNoteToLabel(label.labelId, viewModel.noteToUpdate.noteId)
            }

            override fun onActionRemoveLabelFromNote(label: Label) {
                viewModel.removeNoteFromLabel(label.labelId, viewModel.noteToUpdate.noteId)
            }
        })

    }


    private fun performSave(note: Note) {
        if (binding.body.text.toString().trim().isEmpty()) {
            showToastMessage(getString(R.string.blank_note_prompt))
            return
        } else {
            if (!viewModel.isNewNoteCreated) {
                note.dateLastUpdated = Date()
            }
            note.title =
                if (binding.title.text.toString().isEmpty()) getString(R.string.no_title) else binding.title.text.toString()
            note.body = binding.body.text.toString()
            viewModel.updateNote(note)
            this.findNavController().navigateUp()
        }
    }


    private fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun setUpNote(note: Note) {
        binding.title.setText(note.title)
        binding.body.setText(note.body)
    }

    private fun setFont() {
        val preferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        when (val fontSize =
            preferences.getString(getString(R.string.key_font_size), "0")!!.toInt()) {
            14 -> setEditTextSize(fontSize.toFloat())
            20 -> setEditTextSize(fontSize.toFloat())
            28 -> setEditTextSize(fontSize.toFloat())

        }
    }

    /**
     * increase edit text size dynamically based on the specified size
     * @param size
     */
    private fun setEditTextSize(size: Float) {
        binding.body.textSize = size
        binding.title.textSize = size + 2
    }
}

package com.olabode.wilson.daggernoteapp.ui.notes

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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

    private lateinit var viewModel: NoteViewModel
    private lateinit var binding: NoteFragmentBinding
    private var labels: List<Label> = mutableListOf()
    private var noteLabels: List<Label> = mutableListOf()

    @Inject
    lateinit var factory: ViewModelProviderFactory
    private var noteToUpdate: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NoteFragmentBinding.inflate(inflater, container, false)
        noteToUpdate = NoteFragmentArgs.fromBundle(arguments!!).note

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


        noteToUpdate?.let { note ->
            binding.title.setText(note.title)
            binding.body.setText(note.body)
            viewModel.oldBody = note.body
            viewModel.oldTitle = note.title



            viewModel.getAllLabelsForNote(note.noteId).observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer { noteWithLabel ->
                    noteWithLabel?.let {
                        chipsAdapter.submitList(it[0].labels)
                    }
                })

            viewModel.getAllLabelsForNote(noteToUpdate!!.noteId)
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    it?.let {
                        noteLabels = it[0].labels
                    }
                })
        }



        viewModel.allLabels.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                labels = it
            }
        })
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                if (binding.body.text.toString().trim().isNotEmpty()) {
                    if (noteToUpdate == null) {
                        saveDraft()
                    } else {
                        if (viewModel.oldTitle.isNotEmpty() && viewModel.oldBody.isNotEmpty()) {
                            if (viewModel.oldTitle.trim() != binding.title.text.toString().trim()
                                || viewModel.oldBody != binding.body.text.toString().trim()
                            ) {
                                updateAsDraft(noteToUpdate!!)
                            }
                        }
                    }
                }
                viewModel.resetFields()
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,  // LifecycleOwner
            callback
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.notes_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                if (noteToUpdate == null) {
                    hidekeyboard()
                    performSave()
                } else {
                    hidekeyboard()
                    updateNote(noteToUpdate!!)
                }
                return true
            }
            R.id.share_note -> {
                hidekeyboard()
                if (validate()) {
                    shareNote(
                        binding.title.text.toString().trim(),
                        binding.body.text.toString().trim()
                    )
                    return true
                }
            }
            android.R.id.home -> {
                hidekeyboard()
                if (binding.body.text.toString().trim().isNotEmpty()) {
                    if (noteToUpdate == null) {
                        saveDraft()
                    } else {
                        if (viewModel.oldTitle.isNotEmpty() && viewModel.oldBody.isNotEmpty()) {
                            if (viewModel.oldTitle.trim() != binding.title.text.toString().trim()
                                || viewModel.oldBody != binding.body.text.toString().trim()
                            ) {
                                updateAsDraft(noteToUpdate!!)
                                Log.i("UP", "UPDATING")
                            }
                        }
                    }
                }
            }

            R.id.copy_note -> {
                if (binding.body.text.toString().trim().isNotEmpty()) {
                    if (binding.title.text.toString().trim().isEmpty()) {
                        binding.title.setText("")
                    }
                    copyToClipBoard(
                        binding.title.text.toString().trim(),
                        binding.body.text.toString().trim()
                    )

                } else {
                    showToastMessage(getString(R.string.note_is_blank))
                }
            }

            R.id.tag -> {
                performLabelAction()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun performLabelAction() {
        val dialog =
            LabelDialog(noteLabels, labels)
        fragmentManager?.let { it1 -> dialog.show(it1, "LabelDialogFragment") }
        dialog.setOnLabelActionListener(object : LabelDialog.LabelActionListener {

            override fun onActionAddLabelToNote(label: Label) {
                if (noteToUpdate != null) {
                    viewModel.addNoteToLabel(label.labelId, noteToUpdate!!.noteId)
                }

            }

            override fun onActionRemoveLabelFromNote(label: Label) {
                if (noteToUpdate != null) {
                    viewModel.removeNoteFromLabel(label.labelId, noteToUpdate!!.noteId)
                }
            }
        })

    }

    private fun saveDraft() {
        val note = Note(
            title = if (binding.title.text.toString().isEmpty()) getString(R.string.no_title) else binding.title.text.toString(),
            body = binding.body.text.toString(), dateCreated = Date(), dateLastUpdated = Date()
        )
        viewModel.saveNewNote(note)
    }

    private fun updateAsDraft(note: Note) {
        note.title =
            if (binding.title.text.toString().isEmpty()) getString(R.string.no_title) else binding.title.text.toString()
        note.body = binding.body.text.toString()
        note.dateLastUpdated = Date()
        viewModel.updateNote(note)
    }

    private fun validate(): Boolean {
        if (binding.body.text.toString().trim().isNotEmpty()) {
            if (binding.title.text.toString().trim().isEmpty()) {
                binding.title.setText(getString(R.string.no_title))
            }
            return true
        } else {
            showToastMessage(getString(R.string.empty_note_prompt))
        }
        return false
    }

    private fun updateNote(note: Note) {
        if (binding.body.text.toString().trim().isEmpty()) {
            showToastMessage(getString(R.string.blank_note_prompt))
            return
        } else {
            note.title =
                if (binding.title.text.toString().isEmpty()) getString(R.string.no_title) else binding.title.text.toString()
            note.body = binding.body.text.toString()
            note.dateLastUpdated = Date()

            viewModel.updateNote(note)
            showToastMessage(getString(R.string.updating))
            this.findNavController().navigateUp()
        }

    }

    private fun performSave() {
        if (binding.body.text.toString().trim().isEmpty()) {
            showToastMessage(getString(R.string.blank_note_prompt))
            return
        } else {
            val note = Note(
                title = if (binding.title.text.toString().isEmpty()) getString(R.string.no_title) else binding.title.text.toString(),
                body = binding.body.text.toString(), dateCreated = Date(), dateLastUpdated = Date()
            )
            viewModel.saveNewNote(note)

            this.findNavController().navigateUp()
        }
    }


    private fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    private fun shareNote(title: String, body: String) {
        val message = title + "\n" + body
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, title)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(Intent.createChooser(intent, getString(R.string.share_chooser_text)))

    }


    private fun copyToClipBoard(title: String, body: String) {
        val copy = title + "\n" + body
        val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(title, copy)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, getString(R.string.text_copied), Toast.LENGTH_SHORT).show()
    }


    private fun hidekeyboard() {
        val imm: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
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

package com.olabode.wilson.daggernoteapp.ui.notes

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.databinding.NoteFragmentBinding
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
        setFont()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(NoteViewModel::class.java)
        note?.let {
            binding.title.setText(it.title)
            binding.note.setText(it.body)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.notes_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                if (note == null) {
                    hidekeyboard()
                    performSave()
                } else {
                    hidekeyboard()
                    updateNote(note!!)
                }
                return true
            }
            R.id.share_note -> {
                hidekeyboard()
                if (validate()) {
                    shareNote(
                        binding.title.text.toString().trim(),
                        binding.note.text.toString().trim()
                    )
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun validate(): Boolean {
        if (binding.note.text.toString().trim().isNotEmpty()) {
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
        if (binding.note.text.toString().trim().isEmpty()) {
            showToastMessage(getString(R.string.blank_note_prompt))
            return
        } else {
            note.title =
                if (binding.title.text.toString().isEmpty()) getString(R.string.no_title) else binding.title.text.toString()
            note.body = binding.note.text.toString()
            note.dateLastUpdated = Date()

            viewModel.updateNote(note)
            showToastMessage(getString(R.string.updating))
            this.findNavController().navigateUp()
        }

    }

    private fun performSave() {
        if (binding.note.text.toString().trim().isEmpty()) {
            showToastMessage(getString(R.string.blank_note_prompt))
            return
        } else {
            val note = Note(
                title = if (binding.title.text.toString().isEmpty()) getString(R.string.no_title) else binding.title.text.toString(),
                body = binding.note.text.toString(), dateCreated = Date(), dateLastUpdated = Date()
            )
            viewModel.saveNewNote(note)
            showToastMessage(getString(R.string.saving))
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
//            28 -> setEditTextSize(fontSize.toFloat())
        }
    }

    /**
     * increase edit text size dynamically based on the specified size.
     *
     * @param size
     */
    private fun setEditTextSize(size: Float) {
        binding.note.textSize = size
        binding.title.textSize = size + 2
    }


}

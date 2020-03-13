package com.olabode.wilson.daggernoteapp.ui.trash

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.databinding.ViewTrashNoteFragmentBinding
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ViewTrashNoteFragment : DaggerFragment() {

    companion object {
        fun newInstance() = ViewTrashNoteFragment()
    }

    private lateinit var viewModel: ViewTrashNoteViewModel
    private lateinit var binding: ViewTrashNoteFragmentBinding
    private lateinit var note: Note

    @Inject
    lateinit var factory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewTrashNoteFragmentBinding.inflate(inflater, container, false)

        note = ViewTrashNoteFragmentArgs.fromBundle(arguments!!).note
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.note.setText(note.body)
        binding.title.setText(note.title)
        viewModel = ViewModelProviders.of(this, factory).get(ViewTrashNoteViewModel::class.java)

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.view_trash_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_permanently -> {
                viewModel.delete(note)
                findNavController().navigateUp()
                return true
            }
            R.id.restore -> {
                viewModel.removeFromTrash(note)
                findNavController().navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



}

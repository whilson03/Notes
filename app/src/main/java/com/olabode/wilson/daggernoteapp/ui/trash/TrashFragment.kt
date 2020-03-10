package com.olabode.wilson.daggernoteapp.ui.trash

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.adapters.NoteListAdapter
import com.olabode.wilson.daggernoteapp.databinding.TrashFragmentBinding
import com.olabode.wilson.daggernoteapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class TrashFragment : DaggerFragment() {

    companion object {
        fun newInstance() = TrashFragment()
    }

    private lateinit var viewModel: TrashViewModel

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private lateinit var binding: TrashFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TrashFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(TrashViewModel::class.java)

        val adapter = NoteListAdapter(context!!)
        binding.recyclerView.adapter = adapter

        viewModel.trashList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }


    private fun confirmDeleteDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.trash_dialog_title))
            .setMessage(getString(R.string.trash_dialog_message))
            .setPositiveButton(getString(R.string.confirm_yes)) { _, _ ->
                Toast.makeText(context, getString(R.string.deleting_note_text), Toast.LENGTH_SHORT)
                    .show()
                viewModel.deleteAllNotesFromTrash()
            }.setNegativeButton(
                getString(R.string.confirm_no)
            ) { _, _ -> }.show()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.trash_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                confirmDeleteDialog()
            }
        }

        return super.onOptionsItemSelected(item)
    }

}

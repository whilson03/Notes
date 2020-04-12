package com.olabode.wilson.daggernoteapp.ui.trash

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.adapters.NoteListAdapter
import com.olabode.wilson.daggernoteapp.data.Result
import com.olabode.wilson.daggernoteapp.databinding.TrashFragmentBinding
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.ui.dialogs.TrashDialog
import com.olabode.wilson.daggernoteapp.utils.NoteItemDecoration
import com.olabode.wilson.daggernoteapp.utils.Util
import com.olabode.wilson.daggernoteapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class TrashFragment : DaggerFragment() {

    companion object {
        fun newInstance() = TrashFragment()
    }

    private lateinit var viewModel: TrashViewModel
    private var deleteMenu: MenuItem? = null

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private lateinit var binding: TrashFragmentBinding

    private lateinit var layoutManager: StaggeredGridLayoutManager

    private lateinit var adapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        binding = TrashFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModeSpan = Util.getViewModeSpanCount(context!!)
        viewModel = ViewModelProvider(this, factory).get(TrashViewModel::class.java)

        layoutManager = StaggeredGridLayoutManager(viewModeSpan, LinearLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = layoutManager
        adapter = NoteListAdapter(context!!, layoutManager)
        binding.recyclerView.addItemDecoration(NoteItemDecoration(2))
        binding.recyclerView.adapter = adapter


        viewModel.getTrashedNotes().observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Result.Success -> {
                        hideEmptyState()
                        adapter.submitList(it.data)
                        scrollToTop()
                    }
                    is Result.Empty -> {
                        showEmptyState()
                        // submit empty list to the adapter
                        adapter.submitList(emptyList<Note>())
                    }
                }
            }
        })




        adapter.setOnItemClickListener(object : NoteListAdapter.OnItemClickListener {
            override fun onItemClick(note: Note, titleView: TextView, bodyView: TextView) {
                findNavController().navigate(
                    TrashFragmentDirections.actionTrashFragmentToViewTrashNoteFragment(
                        note
                    )
                )
            }
        })

        adapter.setLongListener(object : NoteListAdapter.OnItemLongClickListener {
            override fun onItemLongClick(note: Note, view: View) {
                val dialog =
                    TrashDialog(note)
                fragmentManager?.let { it1 -> dialog.show(it1, "TrashDialogFragment") }
                dialog.setNoteDialogClickListener(object : TrashDialog.NoteDialogListener {
                    override fun onNoteOptionClick(note: Note, action: Util.ACTION) {
                        when (action) {
                            Util.ACTION.DELETE -> viewModel.deleteNote(note)
                            else -> viewModel.removeFromTrash(note)
                        }
                    }
                })
            }
        })
    }

    private fun scrollToTop() {
        binding.recyclerView.post { binding.recyclerView.smoothScrollToPosition(0) }
    }


    private fun showEmptyState() {
        binding.emptyTrashIcon.visibility = View.VISIBLE
        binding.emptyNoteText.visibility = View.VISIBLE
        deleteMenu?.isVisible = false
    }

    private fun hideEmptyState() {
        binding.emptyTrashIcon.visibility = View.GONE
        binding.emptyNoteText.visibility = View.GONE
        deleteMenu?.isVisible = true
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
        deleteMenu = menu.findItem(R.id.delete)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                confirmDeleteDialog()
            }

            R.id.sort_recent -> {
                viewModel.filter(Util.SORT.DATE_ADDED_TO_TRASH_RECENT)
            }

            R.id.sort_older -> {
                viewModel.filter(Util.SORT.DATE_ADDED_TO_TRASH_RECENT_OLDER)
            }

            R.id.note_view_mode -> {
                if (layoutManager.spanCount == 1) {
                    Util.setGridMode(context!!, true)
                    layoutManager.spanCount = Util.getViewModeSpanCount(context!!)
                    item.icon = ContextCompat.getDrawable(context!!, R.drawable.ic_view_list)
                } else {
                    Util.setGridMode(context!!, false)
                    layoutManager.spanCount = Util.getViewModeSpanCount(context!!)
                    item.icon = ContextCompat.getDrawable(context!!, R.drawable.ic_view_grid)
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }
}

package com.olabode.wilson.daggernoteapp.ui.trash

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.adapters.NoteListAdapter
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

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private lateinit var binding: TrashFragmentBinding

    private lateinit var layoutManager: StaggeredGridLayoutManager

    private lateinit var adapter: NoteListAdapter

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
        val viewModeSpan = Util.getViewModeSpanCount(context!!)
        viewModel = ViewModelProviders.of(this, factory).get(TrashViewModel::class.java)

        layoutManager = StaggeredGridLayoutManager(viewModeSpan, LinearLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = layoutManager
        adapter = NoteListAdapter(context!!, layoutManager)
        binding.recyclerView.addItemDecoration(NoteItemDecoration(2))
        binding.recyclerView.adapter = adapter

        viewModel.trashList.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isEmpty()) {
                    binding.emptyTrashIcon.visibility = View.VISIBLE
                    binding.emptyNoteText.visibility = View.VISIBLE
                } else {
                    binding.emptyTrashIcon.visibility = View.GONE
                    binding.emptyNoteText.visibility = View.GONE
                }
                adapter.submitList(it)
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
                    override fun onNoteOptionClick(note: Note, position: Int) {
                        when (position) {
                            0 -> {
                                viewModel.deleteNote(note)
                            }
                            1 -> {
                                viewModel.removeFromTrash(note)
                            }
                        }
                    }
                })
            }
        })
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
        val deleteMenu = menu.findItem(R.id.delete)
        viewModel.trashList.observe(viewLifecycleOwner, Observer {
            it?.let {
                deleteMenu.isVisible = it.isNotEmpty()
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                confirmDeleteDialog()
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

    private fun hideShowOptionsMenu() {

    }

}

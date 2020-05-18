package com.olabode.wilson.daggernoteapp.ui.labelview

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.adapters.NoteListAdapter
import com.olabode.wilson.daggernoteapp.databinding.FragmentLabeledNoteViewBinding
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.ui.dialogs.NoteDialog
import com.olabode.wilson.daggernoteapp.utils.NoteItemDecoration
import com.olabode.wilson.daggernoteapp.utils.Util
import com.olabode.wilson.daggernoteapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import java.util.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class LabeledNoteView : DaggerFragment() {
    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var viewModel: LabelNoteViewModel
    private lateinit var binding: FragmentLabeledNoteViewBinding
    private lateinit var adapter: NoteListAdapter
    private var icon: Drawable? = null
    private var background: ColorDrawable? = null
    private lateinit var layoutManager: StaggeredGridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val labelId = LabeledNoteViewArgs.fromBundle(arguments!!).labelId

        retainInstance = true
        val viewModeSpan = Util.getViewModeSpanCount(context!!)
        binding = FragmentLabeledNoteViewBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, factory).get(LabelNoteViewModel::class.java)


        icon = ContextCompat.getDrawable(
            Objects.requireNonNull<FragmentActivity>(activity),
            R.drawable.ic_delete
        )

        background = ColorDrawable(Color.TRANSPARENT)

        layoutManager = StaggeredGridLayoutManager(viewModeSpan, LinearLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = layoutManager
        adapter = NoteListAdapter(context!!, layoutManager)
        binding.recyclerView.addItemDecoration(NoteItemDecoration(2))
        binding.recyclerView.adapter = adapter


        adapter.setOnItemClickListener(object : NoteListAdapter.OnItemClickListener {
            override fun onItemClick(note: Note, titleView: TextView, bodyView: TextView) {
                val action =
                    LabeledNoteViewDirections.actionLabeledNoteViewToNoteFragment(
                        note, getString(R.string.edit_note)
                    )

                findNavController().navigate(action)
            }
        })

        adapter.setOnToggleListener(object : NoteListAdapter.OnToggleListener {
            override fun onItemToggle(note: Note, isChecked: Boolean) {
                favouriteAction(note)
            }
        })

        adapter.setLongListener(object : NoteListAdapter.OnItemLongClickListener {
            override fun onItemLongClick(note: Note, view: View) {
                val dialog = NoteDialog(note)
                fragmentManager?.let { it1 -> dialog.show(it1, "NoteDialogFragment") }
                dialog.setNoteDialogClickListener(object : NoteDialog.NoteDialogListener {
                    override fun onNoteOptionClick(note: Note, action: Util.ACTION) {
                        when (action) {
                            Util.ACTION.SHARE -> shareNote(note.title, note.body)
                            Util.ACTION.DELETE -> viewModel.moveToTrash(note)
                            else -> copyToClipBoard(note.title, note.body)
                        }
                    }
                })
            }
        })


        //observe list of body from the view model
        viewModel.getAllNotesForLabel(labelId).observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                it?.let {

                    val list = it[0].notes.filter { note ->
                        !note.isTrashItem
                    }
                    setupEmptyState(list)
                    adapter.submitList(list)
                }
            })

        setHasOptionsMenu(true)
        setupSwipeDelete()
        return binding.root
    }


    private fun setupEmptyState(list: List<Note>) {
        if (list.isEmpty()) {
            showEmptyState()
        } else {
            hideEmptyState()
        }
    }

    private fun showEmptyState() {
        binding.emptyNoteIcon.visibility = View.VISIBLE
        binding.emptyNoteText.visibility = View.VISIBLE
    }

    private fun hideEmptyState() {
        binding.emptyNoteIcon.visibility = View.GONE
        binding.emptyNoteText.visibility = View.GONE
    }

    private fun favouriteAction(note: Note) {
        viewModel.addRemoveFavourite(note)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_label_view, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.note_view_mode -> {
                if (layoutManager.spanCount == 1) {
                    Util.setGridMode(context!!, true)
                    layoutManager.spanCount = Util.getViewModeSpanCount(context!!)
                    item.icon = ContextCompat.getDrawable(context!!, R.drawable.ic_view_list)
                    adapter.notifyDataSetChanged()
                } else {
                    Util.setGridMode(context!!, false)
                    layoutManager.spanCount = Util.getViewModeSpanCount(context!!)
                    item.icon = ContextCompat.getDrawable(context!!, R.drawable.ic_view_grid)
                    adapter.notifyDataSetChanged()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun scrollToTop() {
        binding.recyclerView.post { binding.recyclerView.smoothScrollToPosition(0) }
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


    private fun setupSwipeDelete() {
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT or
                        ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                val itemView = viewHolder.itemView
                val backgroundCornerOffset =
                    20 //so background is behind the rounded corners of itemView

                val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
                val iconTop = itemView.top + (itemView.height - icon!!.intrinsicHeight) / 2
                val iconBottom = iconTop + icon!!.intrinsicHeight


                when {
                    dX > 0 -> { // Swiping to the right
                        val iconLeft = itemView.left + iconMargin
                        val iconRight = iconLeft + icon!!.intrinsicWidth
                        icon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                        background!!.setBounds(
                            itemView.left, itemView.top,
                            itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
                        )
                    }
                    dX < 0 -> { // Swiping to the left
                        val iconLeft = itemView.right - iconMargin - icon!!.intrinsicWidth
                        val iconRight = itemView.right - iconMargin
                        icon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                        background!!.setBounds(
                            itemView.right + dX.toInt() - backgroundCornerOffset,
                            itemView.top, itemView.right, itemView.bottom
                        )
                    }
                    else -> // view is unSwiped
                        background!!.setBounds(0, 0, 0, 0)
                }
                background!!.draw(c)
                icon!!.draw(c)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        val note = adapter.getNoteAt(viewHolder.adapterPosition)
                        viewModel.moveToTrash(note)
                        Snackbar.make(
                            viewHolder.itemView,
                            getString(R.string.undo_snackbar_message),
                            Snackbar.LENGTH_SHORT
                        )
                            .setAction(getString(R.string.undo)) {
                                viewModel.undoMoveToTrash(note)
                            }.show()

                    }
                    ItemTouchHelper.RIGHT -> {
                        val note = adapter.getNoteAt(viewHolder.adapterPosition)
                        viewModel.moveToTrash(note)
                        Snackbar.make(
                            viewHolder.itemView,
                            getString(R.string.undo_snackbar_message),
                            Snackbar.LENGTH_SHORT
                        )
                            .setAction(getString(R.string.undo)) {
                                viewModel.undoMoveToTrash(note)
                            }.show()

                    }
                }
            }
        }).attachToRecyclerView(binding.recyclerView)
    }
}



package com.olabode.wilson.daggernoteapp.ui.favourite

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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.adapters.NoteListAdapter
import com.olabode.wilson.daggernoteapp.databinding.FavouritesFragmentBinding
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.utils.NoteItemDecoration
import com.olabode.wilson.daggernoteapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import java.util.*
import javax.inject.Inject

class FavouritesFragment : DaggerFragment() {

    companion object {
        fun newInstance() =
            FavouritesFragment()
    }

    @Inject
    lateinit var factory: ViewModelProviderFactory
    lateinit var binding: FavouritesFragmentBinding
    private lateinit var icon: Drawable
    private lateinit var background: ColorDrawable
    private lateinit var adapter: NoteListAdapter
    private lateinit var viewModel: FavouritesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavouritesFragmentBinding.inflate(inflater, container, false)
        icon = ContextCompat.getDrawable(
            Objects.requireNonNull<FragmentActivity>(activity),
            R.drawable.ic_delete
        )!!
        background = ColorDrawable(Color.RED)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(FavouritesViewModel::class.java)
        adapter = NoteListAdapter(context!!)
        binding.recyclerView.addItemDecoration(NoteItemDecoration(2))
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : NoteListAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                navigateToEditNote(note)
            }
        })

        adapter.setOnToggleListener(object : NoteListAdapter.OnToggleListener {
            override fun onItemToggle(note: Note, isChecked: Boolean) {
                favouriteAction(note)
            }
        })

        viewModel.favouritesList.observe(viewLifecycleOwner, Observer {
            it?.let {
                emptyState(it)
                adapter.submitList(it)
            }
        })

        adapter.setLongListener(object : NoteListAdapter.OnItemLongClickListener {
            override fun onItemLongClick(note: Note, view: View) {
                val dialog = NoteDialog(note)
                fragmentManager?.let { it1 -> dialog.show(it1, "NoteDialogFragment") }
                dialog.setNoteDialogClickListener(object : NoteDialog.NoteDialogListener {
                    override fun onNoteOptionClick(note: Note, position: Int) {
                        when (position) {
                            0 -> {
                                shareNote(note.title, note.body)
                            }
                            1 -> {
                                viewModel.moveToTrash(note)
                            }
                            2 -> {
                                copyToClipBoard(note.title, note.body)
                            }
                        }
                    }
                })
            }
        })



        setHasOptionsMenu(true)
        setupSwipeDelete()
    }

    private fun emptyState(it: List<Note>) {
        if (it.isEmpty()) {
            binding.emptyFavIcon.visibility = View.VISIBLE
            binding.emptyNoteText.visibility = View.VISIBLE
        } else {
            binding.emptyFavIcon.visibility = View.GONE
            binding.emptyNoteText.visibility = View.GONE
        }
    }

    private fun favouriteAction(note: Note) {
        viewModel.addRemoveFavourite(note)
    }


    private fun navigateToEditNote(note: Note) {
        findNavController().navigate(
            FavouritesFragmentDirections.actionFavouritesToNoteFragment(
                note,
                getString(R.string.edit_note)
            )
        )
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

                val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
                val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                val iconBottom = iconTop + icon.intrinsicHeight


                when {
                    dX > 0 -> { // Swiping to the right
                        val iconLeft = itemView.left + iconMargin
                        val iconRight = iconLeft + icon.intrinsicWidth
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                        background.setBounds(
                            itemView.left, itemView.top,
                            itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
                        )
                    }
                    dX < 0 -> { // Swiping to the left
                        val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                        val iconRight = itemView.right - iconMargin
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                        background.setBounds(
                            itemView.right + dX.toInt() - backgroundCornerOffset,
                            itemView.top, itemView.right, itemView.bottom
                        )
                    }
                    else -> // view is unSwiped
                        background.setBounds(0, 0, 0, 0)
                }
                background.draw(c)
                icon.draw(c)
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_date_created -> {
                viewModel.favouritesList.observe(viewLifecycleOwner, Observer {
                    adapter.submitList(it.sortedBy { note ->
                        note.dateCreated
                    })
                    binding.recyclerView.smoothScrollToPosition(0)
                })
            }
            R.id.sort_last_modified -> {
                viewModel.favouritesList.observe(viewLifecycleOwner, Observer {
                    adapter.submitList(it.sortedByDescending { note ->
                        note.dateLastUpdated
                    })
                    binding.recyclerView.smoothScrollToPosition(0)
                })
            }

            R.id.sort_name -> {
                viewModel.favouritesList.observe(viewLifecycleOwner, Observer {
                    adapter.submitList(it.sortedBy { note ->
                        note.title.toLowerCase()
                    })
                    binding.recyclerView.smoothScrollToPosition(0)
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

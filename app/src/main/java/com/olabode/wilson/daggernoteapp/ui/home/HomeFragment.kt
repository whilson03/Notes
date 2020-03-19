package com.olabode.wilson.daggernoteapp.ui.home

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
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
import com.olabode.wilson.daggernoteapp.databinding.FragmentHomeBinding
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.utils.NoteItemDecoration
import com.olabode.wilson.daggernoteapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import java.util.*
import javax.inject.Inject


class HomeFragment : DaggerFragment() {

    private var icon: Drawable? = null
    private var background: ColorDrawable? = null
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: NoteListAdapter
    @Inject
    lateinit var factory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        homeViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)

        icon = ContextCompat.getDrawable(
            Objects.requireNonNull<FragmentActivity>(activity), R.drawable.ic_delete
        )
        background = ColorDrawable(Color.RED)

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

        binding.fab.setOnClickListener { navigateToAddNewNote() }

        //observe list of note from the view model
        homeViewModel.notesList.observe(viewLifecycleOwner, Observer {
            it?.let {
                emptyState(it)
                adapter.submitList(it)
            }
        })


        setHasOptionsMenu(true)
        setupSwipeDelete()
        return binding.root
    }

    private fun emptyState(it: List<Note>) {
        if (it.isEmpty()) {
            binding.emptyNoteIcon.visibility = View.VISIBLE
            binding.emptyNoteText.visibility = View.VISIBLE
        } else {
            binding.emptyNoteIcon.visibility = View.GONE
            binding.emptyNoteText.visibility = View.GONE
        }
    }

    private fun favouriteAction(note: Note) {
        homeViewModel.addRemoveFavourite(note)
    }

    private fun navigateToAddNewNote() {
        this.findNavController().navigate(
            HomeFragmentDirections
                .actionNavHomeToNoteFragment(
                    null, getString(
                    R.string.add_note_title
                    )
                )
        )
    }


    private fun navigateToEditNote(note: Note) {
        findNavController().navigate(
            HomeFragmentDirections
                .actionNavHomeToNoteFragment(
                    note, getString(
                    R.string.edit_note
                    )
                )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_date_created -> {
                homeViewModel.notesList.observe(viewLifecycleOwner, Observer {
                    adapter.submitList(it.sortedBy { note ->
                        note.dateCreated
                    })
                    binding.recyclerView.smoothScrollToPosition(0)
                })
            }
            R.id.sort_last_modified -> {
                homeViewModel.notesList.observe(viewLifecycleOwner, Observer {
                    adapter.submitList(it.sortedByDescending { note ->
                        note.dateLastUpdated
                    })
                    binding.recyclerView.smoothScrollToPosition(0)
                })
            }

            R.id.sort_name -> {
                homeViewModel.notesList.observe(viewLifecycleOwner, Observer {
                    adapter.submitList(it.sortedBy { note ->
                        note.title.toLowerCase()
                    })
                    binding.recyclerView.smoothScrollToPosition(0)
                })
            }
        }
        return super.onOptionsItemSelected(item)
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
                        homeViewModel.moveToTrash(note)
                        Snackbar.make(
                            viewHolder.itemView,
                            getString(R.string.undo_snackbar_message),
                            Snackbar.LENGTH_SHORT
                        )
                            .setAction(getString(R.string.undo)) {
                                homeViewModel.undoMoveToTrash(note)
                            }.show()

                    }
                    ItemTouchHelper.RIGHT -> {
                        val note = adapter.getNoteAt(viewHolder.adapterPosition)
                        homeViewModel.moveToTrash(note)
                        Snackbar.make(
                            viewHolder.itemView,
                            getString(R.string.undo_snackbar_message),
                            Snackbar.LENGTH_SHORT
                        )
                            .setAction(getString(R.string.undo)) {
                                homeViewModel.undoMoveToTrash(note)
                            }.show()

                    }
                }
            }
        }).attachToRecyclerView(binding.recyclerView)
    }
}
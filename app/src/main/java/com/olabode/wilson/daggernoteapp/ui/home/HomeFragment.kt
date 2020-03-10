package com.olabode.wilson.daggernoteapp.ui.home

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.adapters.NoteListAdapter
import com.olabode.wilson.daggernoteapp.databinding.FragmentHomeBinding
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class HomeFragment : DaggerFragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    @Inject
    lateinit var factory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        homeViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)

        val adapter = NoteListAdapter(context!!)
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
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    private fun favouriteAction(note: Note) {
        homeViewModel.addRemoveFavourite(note)
    }

    private fun navigateToAddNewNote() {
        this.findNavController().navigate(
            HomeFragmentDirections.actionNavHomeToNoteFragment(
                null, getString(
                    R.string.add_note_title
                )
            )
        )
    }

    private fun navigateToEditNote(note: Note) {
        findNavController().navigate(
            HomeFragmentDirections.actionNavHomeToNoteFragment(
                note, getString(
                    R.string.edit_note
                )
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.trash_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }


}
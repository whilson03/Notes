package com.olabode.wilson.daggernoteapp.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.adapters.NoteListAdapter
import com.olabode.wilson.daggernoteapp.databinding.FavouritesFragmentBinding
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class FavouritesFragment : DaggerFragment() {

    companion object {
        fun newInstance() =
            FavouritesFragment()
    }

    @Inject
    lateinit var factory: ViewModelProviderFactory
    lateinit var binding: FavouritesFragmentBinding

    private lateinit var viewModel: FavouritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavouritesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(FavouritesViewModel::class.java)
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

        viewModel.favouritesList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
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

}

package com.olabode.wilson.daggernoteapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.adapters.callbacks.NoteDiffCallBack
import com.olabode.wilson.daggernoteapp.adapters.viewholders.NoteViewHolder
import com.olabode.wilson.daggernoteapp.databinding.FragmentHomeBinding
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import smartadapter.SmartRecyclerAdapter
import smartadapter.widget.DiffUtilExtension
import smartadapter.widget.DiffUtilExtensionBuilder
import javax.inject.Inject

class HomeFragment : DaggerFragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var diffUtilExtension: DiffUtilExtension

    @Inject
    lateinit var factory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        homeViewModel =
            ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
        binding.fab.setOnClickListener {
            this.findNavController().navigate(
                HomeFragmentDirections.actionNavHomeToNoteFragment(
                    null, getString(
                        R.string.add_note_title
                    )
                )
            )
        }

        val smartAdapter: SmartRecyclerAdapter = SmartRecyclerAdapter
            .items(listOf<Note>())
            .map(Note::class, NoteViewHolder::class)
            .into(binding.recyclerView)


        diffUtilExtension = DiffUtilExtensionBuilder().apply {
            smartRecyclerAdapter = smartAdapter
            diffPredicate = NoteDiffCallBack()
        }.build()


        homeViewModel.notesList.observe(viewLifecycleOwner, Observer {
            it?.let {
                smartAdapter.setItems(it.toMutableList())
            }
        })

        return binding.root
    }
}
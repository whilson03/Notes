package com.olabode.wilson.daggernoteapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.olabode.wilson.daggernoteapp.adapters.NoteListAdapter
import com.olabode.wilson.daggernoteapp.databinding.FragmentHomeBinding
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
        homeViewModel =
            ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
        val adapter = NoteListAdapter()
        binding.recyclerView.adapter = adapter


        homeViewModel.notesList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}
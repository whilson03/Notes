package com.olabode.wilson.daggernoteapp.ui.trash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
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
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(TrashViewModel::class.java)
//
//        val adapter = NoteListAdapter()
//        binding.recyclerView.adapter = adapter
//
//        viewModel.trashList.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                adapter.submitList(it)
//            }
//        })
    }

}

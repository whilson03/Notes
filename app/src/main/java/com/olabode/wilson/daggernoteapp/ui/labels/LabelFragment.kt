package com.olabode.wilson.daggernoteapp.ui.labels


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.adapters.LabelListAdapter
import com.olabode.wilson.daggernoteapp.databinding.FragmentLabelBinding
import com.olabode.wilson.daggernoteapp.models.Label
import com.olabode.wilson.daggernoteapp.ui.dialogs.EditAddLabel
import com.olabode.wilson.daggernoteapp.utils.showToast
import com.olabode.wilson.daggernoteapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class LabelFragment : DaggerFragment() {

    private lateinit var binding: FragmentLabelBinding
    private lateinit var viewModel: LabelViewModel
    private lateinit var adapter: LabelListAdapter

    @Inject
    lateinit var factory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        binding = FragmentLabelBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(LabelViewModel::class.java)
        adapter = LabelListAdapter(context!!)
        binding.labelRecycler.adapter = adapter

        viewModel.allLabels.observe(viewLifecycleOwner, Observer {
            binding.emptyLabelState.visibility = if (it.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
            adapter.submitList(it)
        })


        adapter.setDeleteClickListener(object : LabelListAdapter.OnItemDeleteClickListener {
            override fun onDeleteClicked(label: Label) {
                confirmDeleteDialog(label)
            }
        })




        binding.fab.setOnClickListener {
            val dialog = EditAddLabel(null)
            fragmentManager?.let { fm -> dialog.show(fm, "AddLabel") }
            dialog.setLabelDialogListener(object : EditAddLabel.LabelDialogListener {
                override fun onSubmitLabel(label: Label, isNewLabel: Boolean) {
                    if (isNewLabel) {
                        viewModel.insertLabel(label)

                    }
                }
            })
        }

        adapter.setOnItemClickListener(object : LabelListAdapter.OnItemClickListener {
            override fun onEditLabel(label: Label) {
                val dialog = EditAddLabel(label)
                fragmentManager?.let { fm -> dialog.show(fm, "AddLabel") }
                dialog.setLabelDialogListener(object : EditAddLabel.LabelDialogListener {
                    override fun onSubmitLabel(label: Label, isNewLabel: Boolean) {
                        if (!isNewLabel) {
                            viewModel.updateLabel(label)
                            context?.showToast("Updating")
                        }

                    }
                })
            }

        })

        return binding.root
    }

    private fun confirmDeleteDialog(label: Label) {
        MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.delete_label))
            .setMessage(getString(R.string.delete_label_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteLabel(label)
                context?.showToast(getString(R.string.deleted))

            }.setNegativeButton(
                getString(R.string.no)
            ) { _, _ ->

            }.show()
    }

}

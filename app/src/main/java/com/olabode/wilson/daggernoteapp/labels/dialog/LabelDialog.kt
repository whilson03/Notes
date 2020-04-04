package com.olabode.wilson.daggernoteapp.labels.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.olabode.wilson.daggernoteapp.adapters.LabelSelectionAdapter
import com.olabode.wilson.daggernoteapp.databinding.LabelDialogBinding
import com.olabode.wilson.daggernoteapp.models.Label

/**
 *   Created by OLABODE WILSON on 2020-04-02.
 */
class LabelDialog(
    private val labels: List<Label>
) : AppCompatDialogFragment() {

    private lateinit var binding: LabelDialogBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            retainInstance = true
            val builder = MaterialAlertDialogBuilder(it)
            val inflater = requireActivity().layoutInflater
            binding = LabelDialogBinding.inflate(inflater)


            val adapter = LabelSelectionAdapter()
            binding.labelRecycler.adapter = adapter

            adapter.submitList(labels)


            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(binding.root)
                .setCancelable(false)
                .create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }


}


package com.olabode.wilson.daggernoteapp.labels.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.SparseBooleanArray
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.olabode.wilson.daggernoteapp.adapters.LabelSelectionAdapter
import com.olabode.wilson.daggernoteapp.databinding.LabelDialogBinding
import com.olabode.wilson.daggernoteapp.models.Label

/**
 *   Created by OLABODE WILSON on 2020-04-02.
 */
class LabelDialog(private val checkItemsId: List<Label>? = null, private val labels: List<Label>) :
    AppCompatDialogFragment() {

    private lateinit var binding: LabelDialogBinding

    private lateinit var listener: LabelActionListener

    fun setOnLabelActionListener(listener: LabelActionListener) {
        this.listener = listener
    }

    interface LabelActionListener {
        fun onActionAddLabelToNote(label: Label)
        fun onActionRemoveLabelFromNote(label: Label)
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            retainInstance = true
            val builder = MaterialAlertDialogBuilder(it)
            val inflater = requireActivity().layoutInflater
            binding = LabelDialogBinding.inflate(inflater)

            val checkedIds = SparseBooleanArray()
            checkItemsId?.forEach { l ->
                checkedIds.put(l.labelId.toInt(), true)
            }


            val adapter = LabelSelectionAdapter(context!!, checkedIds)
            binding.labelRecycler.adapter = adapter

            adapter.submitList(labels)

            adapter.setOnItemClickListener(object : LabelSelectionAdapter.OnItemClickListener {
                override fun onCheckLabel(label: Label) {
                    listener.onActionAddLabelToNote(label)
                    Toast.makeText(context, "CHECKED", Toast.LENGTH_SHORT).show()
                }

                override fun onUnCheckLabel(label: Label) {
                    listener.onActionRemoveLabelFromNote(label)
                    Toast.makeText(context, "UnChecked", Toast.LENGTH_SHORT).show()
                }
            })

            builder.setView(binding.root)
                .setCancelable(false)
                .create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }


}


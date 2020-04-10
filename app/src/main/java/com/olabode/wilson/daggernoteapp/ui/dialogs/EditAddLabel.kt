package com.olabode.wilson.daggernoteapp.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.databinding.EditAddLabelBinding
import com.olabode.wilson.daggernoteapp.models.Label

/**
 *   Created by OLABODE WILSON on 2020-04-07.
 */

class EditAddLabel(val label: Label?) : AppCompatDialogFragment() {

    private lateinit var listener: LabelDialogListener
    private lateinit var binding: EditAddLabelBinding

    interface LabelDialogListener {
        fun onSubmitLabel(label: Label, isNewLabel: Boolean)
    }

    fun setLabelDialogListener(listener: LabelDialogListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            retainInstance = true
            val inflater = requireActivity().layoutInflater
            binding = EditAddLabelBinding.inflate(inflater)
            val builder = MaterialAlertDialogBuilder(it)
            builder.setView(binding.root)
            label?.let {
                binding.labelEditText.setText(label.title)
                binding.textView.text = getString(R.string.edit_label)
            }
            if (label == null) {
                binding.discard.text = getString(R.string.done_adding)
            }

            binding.saveLabel.setOnClickListener {
                if (label == null) {
                    if (isValidLabel()) {
                        val l = Label(title = binding.labelEditText.text.toString().trim())
                        listener.onSubmitLabel(l, true)
                        binding.labelEditText.setText("")
                    }
                } else {
                    if (isValidLabel()) {
                        val l = Label(
                            labelId = label.labelId,
                            title = binding.labelEditText.text.toString().trim()
                        )
                        listener.onSubmitLabel(l, false)
                        dismiss()
                    }
                }
            }

            binding.discard.setOnClickListener {
                dismiss()
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    private fun isValidLabel(): Boolean {
        return binding.labelEditText.text.toString().trim().isNotEmpty()
    }

}
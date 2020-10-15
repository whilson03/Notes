package com.olabode.wilson.daggernoteapp.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.util.SparseBooleanArray
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.olabode.wilson.daggernoteapp.adapters.LabelSelectionAdapter
import com.olabode.wilson.daggernoteapp.databinding.LabelDialogBinding
import com.olabode.wilson.daggernoteapp.models.Label
import com.olabode.wilson.daggernoteapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatDialogFragment
import javax.inject.Inject

/**
 *   Created by OLABODE WILSON on 2020-04-02.
 */
class LabelDialog :
    DaggerAppCompatDialogFragment() {

    private lateinit var binding: LabelDialogBinding

    private lateinit var listener: LabelActionListener

    private lateinit var checkItemsIdList: ArrayList<Label>

    private lateinit var adapter: LabelSelectionAdapter

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private lateinit var viewModel: LabelDialogViewModel

    private lateinit var labels: MutableList<Label>
    private val checkedIds = SparseBooleanArray()

    fun setOnLabelActionListener(listener: LabelActionListener) {
        this.listener = listener
    }

    interface LabelActionListener {
        fun onActionAddLabelToNote(label: Label)
        fun onActionRemoveLabelFromNote(label: Label)
    }

    companion object {

        private const val CHECKED_ID_KEY = "checkid"
        private const val LABELS_KEY = "labels"
        fun newInstance(
            checkItemsId: ArrayList<Label>? = null,
            labels: ArrayList<Label>
        ): LabelDialog {
            val args = Bundle()
            args.putParcelableArrayList(CHECKED_ID_KEY, checkItemsId)
            args.putParcelableArrayList(LABELS_KEY, labels)
            val fragment =
                LabelDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) { // not restart
            val args = arguments ?: throw IllegalArgumentException("Bundle args required")
            checkItemsIdList =
                args.getParcelableArrayList<Label>(CHECKED_ID_KEY) as ArrayList<Label>
            labels = args.getParcelableArrayList<Label>(LABELS_KEY) as ArrayList<Label>

            checkItemsIdList.forEach { l -> checkedIds.put(l.labelId.toInt(), true) }
        } else { // restart
            checkItemsIdList =
                savedInstanceState.getParcelableArrayList<Label>(CHECKED_ID_KEY) as ArrayList<Label>
            val args = arguments ?: throw IllegalArgumentException("Bundle args required")
            labels = args.getParcelableArrayList<Label>(LABELS_KEY) as ArrayList<Label>
            checkItemsIdList.forEach { l -> checkedIds.put(l.labelId.toInt(), true) }
        }
        viewModel = ViewModelProvider(this, factory).get(LabelDialogViewModel::class.java)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(CHECKED_ID_KEY, checkItemsIdList as ArrayList<out Label>)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.allLabels.observe(
            this,
            Observer {
                it?.let {
                    adapter.submitList(it)
                }
            }
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            retainInstance = true
            val builder = MaterialAlertDialogBuilder(it)
            val inflater = requireActivity().layoutInflater
            binding = LabelDialogBinding.inflate(inflater)

            adapter = LabelSelectionAdapter(requireContext(), checkedIds)
            binding.labelRecycler.adapter = adapter

            adapter.submitList(labels)

            adapter.setOnItemClickListener(
                object : LabelSelectionAdapter.OnItemClickListener {
                    override fun onCheckLabel(label: Label) {
                        listener.onActionAddLabelToNote(label)
                    }

                    override fun onUnCheckLabel(label: Label) {
                        listener.onActionRemoveLabelFromNote(label)
                    }
                }
            )

            binding.save.setOnClickListener {
                val title = binding.addLabelEditText.text.toString().trim()
                if (!TextUtils.isEmpty(title)) {
                    viewModel.saveLabel(title)
                    binding.addLabelEditText.setText("")
                }
            }

            builder.setView(binding.root)
                .setCancelable(false)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

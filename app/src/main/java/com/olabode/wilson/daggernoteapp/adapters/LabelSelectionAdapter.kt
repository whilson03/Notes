package com.olabode.wilson.daggernoteapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.olabode.wilson.daggernoteapp.adapters.callbacks.LabelDiffCallBack
import com.olabode.wilson.daggernoteapp.databinding.ItemSelectNoteLabelBinding
import com.olabode.wilson.daggernoteapp.models.Label

/**
 *   Created by OLABODE WILSON on 2020-04-02.
 */
class LabelSelectionAdapter : ListAdapter<Label,
        LabelSelectionAdapter.LabelSelectionAdapterViewHolder>(LabelDiffCallBack()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LabelSelectionAdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSelectNoteLabelBinding.inflate(
            layoutInflater, parent, false
        )
        return LabelSelectionAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LabelSelectionAdapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LabelSelectionAdapterViewHolder constructor(val binding: ItemSelectNoteLabelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Label) {
            binding.label = item
            binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->

            }
            binding.executePendingBindings()
        }
    }

}
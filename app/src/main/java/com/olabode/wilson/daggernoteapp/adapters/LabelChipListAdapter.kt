package com.olabode.wilson.daggernoteapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.olabode.wilson.daggernoteapp.adapters.callbacks.LabelDiffCallBack
import com.olabode.wilson.daggernoteapp.databinding.ItemLabelChipBinding
import com.olabode.wilson.daggernoteapp.models.Label

/**
 *   Created by OLABODE WILSON on 2020-04-02.
 */
class LabelChipListAdapter : ListAdapter<Label,
        LabelChipListAdapter.LabelChipViewHolder>(LabelDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelChipViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLabelChipBinding.inflate(
            layoutInflater, parent, false
        )
        return LabelChipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LabelChipViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    inner class LabelChipViewHolder constructor(val binding: ItemLabelChipBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Label) {
            binding.label = item
            binding.executePendingBindings()
        }
    }


}
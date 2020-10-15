package com.olabode.wilson.daggernoteapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.olabode.wilson.daggernoteapp.adapters.callbacks.LabelDiffCallBack
import com.olabode.wilson.daggernoteapp.databinding.ItemLabelBinding
import com.olabode.wilson.daggernoteapp.models.Label

/**
 *   Created by OLABODE WILSON on 2020-03-31.
 */

class LabelListAdapter(val context: Context) :
    ListAdapter<Label, LabelListAdapter.LabelViewHolder>(LabelDiffCallBack()) {

    private var clickListener: OnItemClickListener? = null
    private var deleteListener: OnItemDeleteClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    fun setDeleteClickListener(listener: OnItemDeleteClickListener) {
        this.deleteListener = listener
    }

    interface OnItemClickListener {
        fun onEditLabel(label: Label)
    }

    interface OnItemDeleteClickListener {
        fun onDeleteClicked(label: Label)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLabelBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return LabelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class LabelViewHolder constructor(val binding: ItemLabelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Label) {
            binding.label = item
            binding.edit.setOnClickListener {
                if (clickListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    clickListener!!.onEditLabel(getItem(adapterPosition))
                }
            }

            binding.deleteLabel.setOnClickListener {
                if (clickListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    deleteListener!!.onDeleteClicked(getItem(adapterPosition))
                }
            }
            binding.executePendingBindings()
        }
    }
}

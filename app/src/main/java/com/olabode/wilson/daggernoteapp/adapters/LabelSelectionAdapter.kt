package com.olabode.wilson.daggernoteapp.adapters

import android.content.Context
import android.util.SparseBooleanArray
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
class LabelSelectionAdapter(val context: Context, val itemStateArray: SparseBooleanArray) :
    ListAdapter<Label,
        LabelSelectionAdapter.LabelSelectionAdapterViewHolder>(LabelDiffCallBack()) {

    private var clickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    interface OnItemClickListener {
        fun onCheckLabel(label: Label)
        fun onUnCheckLabel(label: Label)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LabelSelectionAdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSelectNoteLabelBinding
            .inflate(layoutInflater, parent, false)
        return LabelSelectionAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LabelSelectionAdapterViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class LabelSelectionAdapterViewHolder constructor(val binding: ItemSelectNoteLabelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Label, position: Int) {
            binding.label = item
            binding.checkbox.isChecked =
                itemStateArray.get(getItem(position).labelId.toInt(), false)
            binding.checkbox.setOnClickListener {

                if (clickListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    // store use Label IDS as keys instead of the adapter positions
                    if (!itemStateArray.get(getItem(adapterPosition).labelId.toInt(), false)) {
                        itemStateArray.put(getItem(adapterPosition).labelId.toInt(), true)
                        binding.checkbox.isChecked = true
                        clickListener!!.onCheckLabel(getItem(adapterPosition))
                    } else {
                        itemStateArray.put(getItem(adapterPosition).labelId.toInt(), false)
                        binding.checkbox.isChecked = false
                        clickListener!!.onUnCheckLabel(getItem(adapterPosition))
                    }
                }
            }
            binding.executePendingBindings()
        }
    }
}

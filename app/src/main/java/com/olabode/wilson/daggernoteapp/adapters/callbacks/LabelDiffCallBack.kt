package com.olabode.wilson.daggernoteapp.adapters.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.olabode.wilson.daggernoteapp.models.Label

/**
 *   Created by OLABODE WILSON on 2020-03-31.
 */
class LabelDiffCallBack : DiffUtil.ItemCallback<Label>() {
    override fun areItemsTheSame(oldItem: Label, newItem: Label): Boolean {
        return oldItem.labelId == newItem.labelId
    }

    override fun areContentsTheSame(oldItem: Label, newItem: Label): Boolean {
        return oldItem == newItem
    }
}

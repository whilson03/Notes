package com.olabode.wilson.daggernoteapp.adapters.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.olabode.wilson.daggernoteapp.adapters.DataItem

class DrawerDiffCallBack : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

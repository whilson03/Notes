package com.olabode.wilson.daggernoteapp.adapters.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.olabode.wilson.daggernoteapp.models.DrawerItem

class DrawerDiffCallBack : DiffUtil.ItemCallback<DrawerItem>() {
    override fun areItemsTheSame(oldItem: DrawerItem, newItem: DrawerItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DrawerItem, newItem: DrawerItem): Boolean {
        return oldItem == newItem
    }

}

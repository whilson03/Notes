package com.olabode.wilson.daggernoteapp.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.olabode.wilson.daggernoteapp.adapters.callbacks.DrawerDiffCallBack

import com.olabode.wilson.daggernoteapp.databinding.DrawerItemBinding
import com.olabode.wilson.daggernoteapp.databinding.NavHeaderMainBinding
import com.olabode.wilson.daggernoteapp.models.DrawerItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 *   Created by OLABODE WILSON on 5/1/20.
 */


private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1


class DrawerAdapter(val clickListener: DrawerClickListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(DrawerDiffCallBack()) {


    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<DrawerItem>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.DrawerItems(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> DrawerViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DrawerViewHolder -> {
                val dataItem = getItem(position) as DataItem.DrawerItems
                holder.bind(dataItem.drawerItem, clickListener)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.DrawerItems -> ITEM_VIEW_TYPE_ITEM
        }
    }


    class DrawerViewHolder constructor(val binding: DrawerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DrawerItem, clickListener: DrawerClickListener) {
            binding.drawerItem = item
            binding.title.text = item.title
            binding.icon.setImageResource(item.iconDrawable)
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): DrawerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DrawerItemBinding.inflate(
                    layoutInflater
                    , parent, false
                )
                return DrawerViewHolder(binding)
            }
        }
    }


    class HeaderViewHolder constructor(val binding: NavHeaderMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NavHeaderMainBinding.inflate(
                    layoutInflater
                    , parent, false
                )
                return HeaderViewHolder(binding)
            }

        }
    }

}


class DrawerClickListener(val clickListener: (drawerItem: DrawerItem) -> Unit) {
    fun onClick(drawerItem: DrawerItem) = clickListener(drawerItem)
}


sealed class DataItem {
    data class DrawerItems(val drawerItem: DrawerItem) : DataItem() {
        override val id = drawerItem.id.toLong()
    }

    object Header : DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}



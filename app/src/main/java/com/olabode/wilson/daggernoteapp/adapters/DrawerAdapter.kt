package com.olabode.wilson.daggernoteapp.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.olabode.wilson.daggernoteapp.R
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


class DrawerAdapter(val clickListener: OnDrawerItemClickListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(DrawerDiffCallBack()) {


    interface OnDrawerItemClickListener {
        fun onItemClicked(drawerItem: DrawerItem)
    }

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
            ITEM_VIEW_TYPE_ITEM -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DrawerItemBinding.inflate(
                    layoutInflater
                    , parent, false
                )
                return DrawerViewHolder(binding)
            }
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DrawerViewHolder -> {
                val dataItem = getItem(position) as DataItem.DrawerItems
                holder.bind(dataItem.drawerItem, clickListener)
                holder.binding.root.isSelected = dataItem.drawerItem.isSelected
                holder.itemView.setBackgroundResource(if (dataItem.drawerItem.isSelected) R.color.ripple_color else android.R.color.transparent)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.DrawerItems -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun setSelected(position: Int) {
        adapterScope.launch {
            for (i in 1 until currentList.size) {
                (currentList[i] as DataItem.DrawerItems).drawerItem.isSelected = i == position
            }
            withContext(Dispatchers.Main) {
                notifyDataSetChanged()
            }
        }


    }


    inner class DrawerViewHolder constructor(val binding: DrawerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DrawerItem, clickListener: OnDrawerItemClickListener) {
            binding.title.text = item.title
            binding.icon.setImageResource(item.iconDrawable)

            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    clickListener.onItemClicked(item)
                    setSelected(adapterPosition)
                }
            }

            binding.executePendingBindings()
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

sealed class DataItem {
    data class DrawerItems(val drawerItem: DrawerItem) : DataItem() {
        override val id = drawerItem.id.toLong()
    }

    object Header : DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}



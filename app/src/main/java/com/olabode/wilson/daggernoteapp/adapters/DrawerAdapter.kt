package com.olabode.wilson.daggernoteapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.olabode.wilson.daggernoteapp.adapters.callbacks.DrawerDiffCallBack
import com.olabode.wilson.daggernoteapp.databinding.DrawerItemBinding
import com.olabode.wilson.daggernoteapp.models.DrawerItem

/**
 *   Created by OLABODE WILSON on 5/1/20.
 */
class DrawerAdapter :
    ListAdapter<DrawerItem, DrawerAdapter.DrawerViewHolder>(DrawerDiffCallBack()) {

    private var clickListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onItemClicked(drawerItem: DrawerItem)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DrawerItemBinding.inflate(
            layoutInflater
            , parent, false
        )
        return DrawerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DrawerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DrawerViewHolder constructor(val binding: DrawerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DrawerItem) {
            binding.title.text = item.title
            binding.icon.setImageResource(item.iconDrawable)

            binding.root.setOnClickListener { view ->
                if (clickListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    clickListener!!.onItemClicked(getItem(adapterPosition))

                }

                binding.executePendingBindings()
            }
        }

    }
}

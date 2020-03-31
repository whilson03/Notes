package com.olabode.wilson.daggernoteapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.olabode.wilson.daggernoteapp.R
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

    fun getNoteAt(position: Int): Label {
        return getItem(position)
    }

    interface OnItemClickListener {
        fun onEditLabel(
            Label: Label,
            editText: EditText,
            editIcon: ImageView,
            deleteImageView: ImageView
        )
    }

    interface OnItemDeleteClickListener {
        fun onDeleteClicked(Label: Label)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLabelBinding.inflate(
            layoutInflater
            , parent, false
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
            binding.imageView?.let {
                it.setImageResource(R.drawable.ic_label)
                it.isEnabled = false
            }

            binding.edit.setOnClickListener {
                val position = adapterPosition
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    clickListener!!.onEditLabel(
                        getItem(position),
                        binding.title,
                        binding.edit, binding.imageView
                    )
                }
            }

            binding.imageView.setOnClickListener {
                val position = adapterPosition
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    deleteListener!!.onDeleteClicked(getItem(position))
                }
            }
            binding.executePendingBindings()
        }
    }


}


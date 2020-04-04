package com.olabode.wilson.daggernoteapp.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
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

    private var currentEditingPosition: Int = -1
    private var clickListener: OnItemClickListener? = null
    private var deleteListener: OnItemDeleteClickListener? = null
    private var labelHolder: LabelViewHolder? = null


    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    fun setDeleteClickListener(listener: OnItemDeleteClickListener) {
        this.deleteListener = listener
    }

    fun getLabelAt(position: Int): Label {
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
        if (currentEditingPosition == position) {
            labelHolder = holder
            labelHolder!!.bind(getItem(position))
            isInEditMode()
        } else {
            isDefaultMode(holder)
            holder.bind(getItem(position))
        }
    }

    override fun onViewRecycled(holder: LabelViewHolder) {
        super.onViewRecycled(holder)
        if (currentEditingPosition == holder.adapterPosition) {
            isDefaultMode(holder)
            labelHolder = null
        }
    }


    private fun isInEditMode() {
        labelHolder!!.let {
            it.binding.imageView.setImageResource(R.drawable.ic_delete)
            it.binding.edit.setImageResource(R.drawable.ic_check)
            it.binding.title.isEnabled = true
            it.binding.mainLayout.setBackgroundColor(Color.TRANSPARENT)
            it.binding.textHolder.visibility = View.GONE
            it.binding.title.visibility = View.VISIBLE
        }
    }

    private fun isDefaultMode(viewHolder: LabelViewHolder) {
        viewHolder.binding.imageView.setImageResource(R.drawable.ic_label)
        viewHolder.binding.edit.setImageResource(R.drawable.ic_mode_edit)
        viewHolder.binding.title.isEnabled = false
        viewHolder.binding.textHolder.visibility = View.VISIBLE
        viewHolder.binding.title.visibility = View.INVISIBLE
    }



    inner class LabelViewHolder constructor(val binding: ItemLabelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Label) {
            binding.label = item

            binding.mainLayout.setOnClickListener {
                if (currentEditingPosition == adapterPosition) {
                    if (labelHolder != null) {
                        isInEditMode()
                    }
                } else {
                    currentEditingPosition = adapterPosition
                    if (labelHolder != null) {
                        isDefaultMode(labelHolder!!)
                    }
                    labelHolder = this
                    isInEditMode()
                    binding.edit.setOnClickListener {
                        if (clickListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                            if (currentEditingPosition == adapterPosition) {
                                isDefaultMode(labelHolder!!)
                                clickListener!!.onEditLabel(
                                    getLabelAt(adapterPosition),
                                    binding.title,
                                    binding.edit,
                                    binding.imageView
                                )
                            }
                        }
                    }


                    binding.imageView.setOnClickListener {
                        if (deleteListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                            if (currentEditingPosition == adapterPosition) {
                                deleteListener!!.onDeleteClicked(getItem(adapterPosition))
//                            currentEditingPosition  = -1
                            }
                        }
                    }
                }


            }

            binding.executePendingBindings()
        }
    }


}


package com.olabode.wilson.daggernoteapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.olabode.wilson.daggernoteapp.adapters.callbacks.NoteDiffCallBack
import com.olabode.wilson.daggernoteapp.databinding.ItemNoteBinding
import com.olabode.wilson.daggernoteapp.models.Note

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */
class NoteListAdapter(val context: Context) :
    ListAdapter<Note, NoteListAdapter.NoteViewHolder>(NoteDiffCallBack()) {

    private var clickListener: OnItemClickListener? = null
    private var toggleListener: OnToggleListener? = null
    private var longListener: OnItemLongClickListener? = null


    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    fun setOnToggleListener(togglelistener: OnToggleListener) {
        this.toggleListener = togglelistener
    }

    fun setLongListener(listener: OnItemLongClickListener) {
        this.longListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteBinding.inflate(
            layoutInflater
            , parent, false
        )
        return NoteViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!)
    }

    fun getNoteAt(position: Int): Note {
        return getItem(position)
    }

    /**
     * interfaces
     */
    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    interface OnToggleListener {
        fun onItemToggle(note: Note, isChecked: Boolean)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(note: Note)
    }


    inner class NoteViewHolder constructor(val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Note) {
            binding.note = item
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    clickListener!!.onItemClick(getItem(position))
                }
            }

            if (item.isTrashItem == 1) {
                binding.favButton.visibility = View.GONE
            } else {
                binding.favButton.visibility = View.VISIBLE
            }

            binding.favButton.isChecked = item.isFavourite == 1



            binding.favButton.setOnClickListener {
                if (getNoteAt(adapterPosition).isFavourite == 0) {
                    Toast.makeText(context, "Added To Favourite", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Removed From Favourite", Toast.LENGTH_SHORT).show()
                }
            }

            binding.favButton.setOnCheckedChangeListener { _, isChecked ->
                // get the current note item position that is being toggled
                val position = adapterPosition
                if (isChecked) {
                    if (toggleListener != null && position != RecyclerView.NO_POSITION) {
                        toggleListener!!.onItemToggle(getItem(position), true)
                    }
                } else {
                    if (toggleListener != null && position != RecyclerView.NO_POSITION) {
                        toggleListener!!.onItemToggle(getItem(position), false)
                    }
                }
            }

            // get the position that was long clicked
            // get the position that was long clicked
            binding.root.setOnLongClickListener {
                val position = adapterPosition
                if (longListener != null && position != RecyclerView.NO_POSITION) {
                    longListener!!.onItemLongClick(getItem(position))
                    return@setOnLongClickListener true
                }
                false
            }
            binding.executePendingBindings()
        }


    }
}




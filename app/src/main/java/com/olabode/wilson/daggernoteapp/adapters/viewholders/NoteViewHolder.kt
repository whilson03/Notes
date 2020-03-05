package com.olabode.wilson.daggernoteapp.adapters.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.models.Note
import smartadapter.viewholder.SmartViewHolder


/**
 *   Created by OLABODE WILSON on 2020-03-01.
 */

class NoteViewHolder constructor(parent: ViewGroup) :
    SmartViewHolder<Note>(
        LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
    ) {

    private val title: TextView = itemView.findViewById(R.id.text_view_title)
    private val desc: TextView = itemView.findViewById(R.id.text_view_description)
    private val addFavourite: ImageView = itemView.findViewById(R.id.favButton)
    private val date: TextView = itemView.findViewById(R.id.note_date)

    override fun bind(item: Note) {
        title.text = item.title
        desc.text = item.body
        date.text = item.dateCreated.toString()
        if (item.isFavourite == 1) {
            addFavourite.setImageResource(R.drawable.ic_star_filled)
        } else {
            addFavourite.setImageResource(R.drawable.ic_star_border)
        }
    }

}
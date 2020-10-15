package com.olabode.wilson.daggernoteapp.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */

@Parcelize

@Entity(tableName = "notes_table", indices = [Index("noteId")])
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "noteId")
    val noteId: Long = 0,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "body")
    var body: String,

    @ColumnInfo(name = "favourite")
    var isFavourite: Boolean = false,

    @ColumnInfo(name = "trash")
    var isTrashItem: Boolean = false,

    @ColumnInfo(name = "created_date")
    var dateCreated: Date,

    @ColumnInfo(name = "date_last_modified")
    var dateLastUpdated: Date,

    @ColumnInfo(name = "date_trashed")
    var trashedDate: Date? = null

) : Parcelable

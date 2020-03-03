package com.olabode.wilson.daggernoteapp.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */

@Parcelize
@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "body")
    var body: String,

    @ColumnInfo(name = "favourite")
    var isFavourite: Int = 0,

    @ColumnInfo(name = "trash")
    var isTrashItem: Int = 0,

    @ColumnInfo(name = "created_date")
    var dateCreated: Date,

    @ColumnInfo(name = "date_last_modified")
    var dateLastUpdated: Date

) : Parcelable
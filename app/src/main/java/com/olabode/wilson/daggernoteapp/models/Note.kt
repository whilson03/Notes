package com.olabode.wilson.daggernoteapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */

@Parcelize
data class Note(
    var id: Int = 0,

    var title: String,
    var body: String,
    var isFavourite: Boolean,
    var isTrashItem: Boolean

) : Parcelable
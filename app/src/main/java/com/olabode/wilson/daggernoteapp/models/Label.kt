package com.olabode.wilson.daggernoteapp.models

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

/**
 *   Created by OLABODE WILSON on 2020-03-29.
 */


@Parcelize
@Entity
data class Label(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "labelId")
    val labelId: Long,
    @ColumnInfo(name = "title")
    var title: String
) : Parcelable


@Entity(primaryKeys = ["labelId", "id"])
data class NotesAndLabelCrossRef(
    val labelId: Long,
    val id: Long
)


data class NotesWithLabel(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "id",
        entityColumn = "labelId",
        associateBy = Junction(NotesAndLabelCrossRef::class)
    )
    val labels: List<Label>
)


data class LabelsWithNote(
    @Embedded val label: Label,
    @Relation(
        parentColumn = "labelId",
        entityColumn = "id",
        associateBy = Junction(NotesAndLabelCrossRef::class)
    )
    val notes: List<Note>
)
package com.olabode.wilson.daggernoteapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.olabode.wilson.daggernoteapp.data.converters.Converters
import com.olabode.wilson.daggernoteapp.models.Label
import com.olabode.wilson.daggernoteapp.models.Note
import com.olabode.wilson.daggernoteapp.models.NotesAndLabelCrossRef

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */

@Database(
    entities = [
        Note::class,
        Label::class,
        NotesAndLabelCrossRef::class
    ], version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun noteDao(): NotesDao

    abstract fun labelDao(): LabelDao

}
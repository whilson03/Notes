package com.olabode.wilson.daggernoteapp.repository.label

import androidx.lifecycle.LiveData
import com.olabode.wilson.daggernoteapp.data.LabelDao
import com.olabode.wilson.daggernoteapp.models.Label
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 *   Created by OLABODE WILSON on 2020-03-30.
 */

@Singleton
class LabelRepository @Inject constructor(private val labelDao: LabelDao) : LabelRepo {

    override fun getAllLabels(): LiveData<List<Label>> {
        return labelDao.getAllLabels()
    }

    override suspend fun insertLabel(label: Label) {
        withContext(Dispatchers.IO) {
            if (labelDao.checkLabelExist(label.title.toLowerCase()) == null) {
                labelDao.insertLabel(label)
            }
        }
    }

    override suspend fun updateLabel(label: Label) {
        withContext(Dispatchers.IO) {
            labelDao.updateLabel(label)
        }
    }

    override suspend fun deleteLabel(label: Label) {
        withContext(Dispatchers.IO) {
            labelDao.deleteLabel(label)
        }
    }
}
package com.olabode.wilson.daggernoteapp.labels

import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.models.Label
import com.olabode.wilson.daggernoteapp.repository.label.LabelRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *   Created by OLABODE WILSON on 2020-03-31.
 */
class LabelViewModel @Inject constructor(private val labelRepository: LabelRepository) :
    ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    val allLabels = labelRepository.getAllLabels()


    fun insertLabel(label: Label) {
        uiScope.launch {
            labelRepository.insertLabel(label)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun updateLabel(label: Label) {
        uiScope.launch {
            labelRepository.updateLabel(label)
        }
    }

    fun deleteLabel(label: Label) {
        uiScope.launch {
            labelRepository.deleteLabel(label)
        }

    }

}
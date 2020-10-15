package com.olabode.wilson.daggernoteapp.ui.dialogs

import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.models.Label
import com.olabode.wilson.daggernoteapp.repository.label.LabelRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *   Created by OLABODE WILSON on 5/19/20.
 */
class LabelDialogViewModel @Inject
constructor(private val labelRepository: LabelRepository) : ViewModel() {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    val allLabels = labelRepository.getAllLabels()

    fun saveLabel(title: String) {
        uiScope.launch {
            labelRepository.insertLabel(Label(title = title))
        }
    }
}

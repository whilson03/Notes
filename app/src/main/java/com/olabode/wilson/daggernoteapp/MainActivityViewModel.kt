package com.olabode.wilson.daggernoteapp

import androidx.lifecycle.ViewModel
import com.olabode.wilson.daggernoteapp.repository.label.LabelRepository
import javax.inject.Inject

/**
 *   Created by OLABODE WILSON on 4/11/20.
 */
class MainActivityViewModel @Inject constructor(private val labelRepository: LabelRepository) :
    ViewModel() {
    val allLabels = labelRepository.getAllLabels()
}
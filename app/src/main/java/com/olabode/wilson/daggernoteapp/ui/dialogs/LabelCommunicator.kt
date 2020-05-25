package com.olabode.wilson.daggernoteapp.ui.dialogs

import com.olabode.wilson.daggernoteapp.models.Label

/**
 *   Created by OLABODE WILSON on 5/18/20.
 */
interface LabelCommunicator {
    fun onNewLabel(labelList: List<Label>)
}
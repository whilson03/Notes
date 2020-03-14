package com.olabode.wilson.daggernoteapp.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 *   Created by OLABODE WILSON on 2020-03-14.
 */


object Util {

    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("EEEE, dd MMMM yyyy, hh:mm:ss a")
        return formatter.format(date)
    }


}

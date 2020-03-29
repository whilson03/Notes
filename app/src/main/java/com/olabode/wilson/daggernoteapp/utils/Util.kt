package com.olabode.wilson.daggernoteapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.olabode.wilson.daggernoteapp.R
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


    /**
     * store the mode for recycler view grid or list
     */
    fun setGridMode(context: Context, isGrid: Boolean) {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(context.getString(R.string.SHARED_PREF_VIEW_MODE_RV), isGrid)
        editor.apply()
    }


    private fun isGridModeEnabled(context: Context): Boolean {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(context.getString(R.string.SHARED_PREF_VIEW_MODE_RV), false)
    }


    /**
     * Return a span count value for the {RecyclerView} , to switch from list mode to Grid mode
     */
    fun getViewModeSpanCount(context: Context): Int {
        return when (isGridModeEnabled(context)) {
            true -> {
                2
            }
            else -> {
                1
            }

        }
    }


    enum class SORT {
        NAME, DATE_CREATED,
        DATE_LAST_MODIFIED,
        DEFAULT,
        DATE_ADDED_TO_TRASH_RECENT,
        DATE_ADDED_TO_TRASH_RECENT_OLDER
    }

}

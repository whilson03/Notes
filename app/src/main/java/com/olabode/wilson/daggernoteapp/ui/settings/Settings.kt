package com.olabode.wilson.daggernoteapp.ui.settings


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import com.olabode.wilson.daggernoteapp.R

/**
 * A simple [Fragment] subclass.
 */
class Settings : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_main, rootKey)
    }
}


private fun rate() {

}


private fun sendFeedback() {

}


private fun disclaimer() {

}
package com.olabode.wilson.daggernoteapp.labels


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.olabode.wilson.daggernoteapp.R

/**
 * A simple [Fragment] subclass.
 */
class LabelFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_label, container, false)
    }


}

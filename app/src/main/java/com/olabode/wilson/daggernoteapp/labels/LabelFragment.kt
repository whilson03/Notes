package com.olabode.wilson.daggernoteapp.labels


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.olabode.wilson.daggernoteapp.adapters.LabelListAdapter
import com.olabode.wilson.daggernoteapp.databinding.FragmentLabelBinding
import com.olabode.wilson.daggernoteapp.models.Label
import com.olabode.wilson.daggernoteapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class LabelFragment : DaggerFragment() {

    private lateinit var binding: FragmentLabelBinding
    private lateinit var viewModel: LabelViewModel
    private lateinit var adapter: LabelListAdapter

    @Inject
    lateinit var factory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLabelBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(LabelViewModel::class.java)
        adapter = LabelListAdapter(context!!)
        binding.labelRecycler.adapter = adapter

        viewModel.allLabels.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.save.setOnClickListener {
            val labelTitle = binding.labelText.text.toString().trim()
            if (labelTitle.isNotEmpty()) {
                viewModel.insertLabel(Label(title = labelTitle))
                binding.labelText.text?.clear()
            } else {
                binding.labelText.error = "Field Cannot Be Blank"
            }

        }

        adapter.setDeleteClickListener(object : LabelListAdapter.OnItemDeleteClickListener {
            override fun onDeleteClicked(Label: Label) {

                Toast.makeText(context!!, "Delete", Toast.LENGTH_SHORT)
                    .show()
                //viewModel.deleteLabel(Label)
            }
        })


        adapter.setOnItemClickListener(object : LabelListAdapter.OnItemClickListener {
            override fun onEditLabel(
                Label: Label, editText: EditText, editIcon: ImageView,
                deleteImageView: ImageView
            ) {
                Toast.makeText(context!!, editText.text, Toast.LENGTH_SHORT)
                    .show()


//                if (!editText.isEnabled) {
//                    editText.isEnabled = true
//                    editText.requestFocus()
//                } else {
//                    if (editText.text.toString().trim().isNotEmpty()) {
//                        viewModel.updateLabel(Label)
//                    } else {
//                        Toast.makeText(context!!, "Field Cannot Be Blank", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
            }
        })

        return binding.root
    }


}

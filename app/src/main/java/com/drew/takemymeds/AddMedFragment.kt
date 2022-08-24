package com.drew.takemymeds

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.drew.takemymeds.databinding.FragmentAddMedBinding
import com.drew.takemymeds.databinding.FragmentHomeBinding
import com.drew.takemymeds.viewmodels.MedicationViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddMedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddMedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentAddMedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MedicationViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val addMedBinding = FragmentAddMedBinding.inflate(inflater, container, false)
        _binding = addMedBinding
        return addMedBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
        R.array.medication_types,
        R.layout.list_item)
        (binding.medicationTypeLabel.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.setScheduleButton.setOnClickListener {
            val action = AddMedFragmentDirections.actionAddMedFragmentToSetScheduleFragment()
            if (isEntryValid()) {
                viewModel.setName(binding.medicationName.text.toString())
                viewModel.setAilmentName(binding.ailmentNameTv.text.toString())
                viewModel.setStock(binding.quantityTv.text.toString().toDouble())
                findNavController().navigate(action)
            } else {
                binding.medNameLabel.error = viewModel.setError(binding.medicationName.text.toString())
                binding.medicationTypeLabel.error = viewModel.setError(binding.spinnerMedType.text.toString())
                binding.ailmentNameLabel.error = viewModel.setError(binding.ailmentNameTv.text.toString())
                binding.quantityLabelTv.error = viewModel.setError(binding.quantityTv.text.toString())
            }
        }

        binding.spinnerMedType.setOnItemClickListener { adapterView, view, position, id ->
            viewModel.setType(adapter.getItem(position).toString())
        }
    }

    private fun isEntryValid() : Boolean {
        return viewModel.isEntryValid(
            binding.medicationName.text.toString(),
            binding.spinnerMedType.text.toString(),
            binding.ailmentNameTv.text.toString(),
            binding.quantityTv.text.toString()
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddMedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddMedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
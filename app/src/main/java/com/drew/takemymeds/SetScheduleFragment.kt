package com.drew.takemymeds

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drew.takemymeds.adapters.ScheduleListAdapter
import com.drew.takemymeds.databinding.FragmentSetScheduleBinding
import com.drew.takemymeds.viewmodels.MedicationViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.lang.System.out
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.time.Duration.Companion.days

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SetScheduleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SetScheduleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentSetScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerviewAdapter: ScheduleListAdapter

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
        val setScheduleBinding = FragmentSetScheduleBinding.inflate(inflater, container, false)
        _binding = setScheduleBinding
        return setScheduleBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.number_of_you_take_meds_per_day,
            R.layout.list_item)
        (binding.frequencyLabel.editText as? AutoCompleteTextView)?.setAdapter(adapter)


        binding.spinnerMedFrequency.setOnItemClickListener { adapterView, view, position, id ->
            var positionSelected = position + 1
            Log.i("Size selected", position.toString())
            Log.i("List", viewModel.sampleTimes(positionSelected).toString())
            viewModel.setFrequency(positionSelected)
            viewModel.sampleTimes(positionSelected)
            viewModel.setMedicationTime(viewModel.sampleTimes(positionSelected))
//            Log.i("VIEWMODELLIST", viewModel.getMedication().toString())

           recyclerviewAdapter.submitList(viewModel.getMedicationTimes())
        }

        recyclerView = binding.timeRecyclerView
        recyclerviewAdapter = ScheduleListAdapter(requireContext(),
            viewModel.type.value.toString(), onTimeClickLister = { localTimes, position ->
            var currentList = recyclerviewAdapter.currentList
            currentList[position] = localTimes
            viewModel.setMedicationTime(currentList)

        }, onDosageClickLister = { dosages, position ->
            var dosageList = viewModel.dosage.value?.toMutableList()

                if (position < dosageList?.size!!) {
                    dosageList[position] = dosages
                    viewModel.setDosage(dosageList)
                }
        })

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recyclerviewAdapter
        //recyclerviewAdapter.submitList(list)

        binding.startDateTextview.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show((context as AppCompatActivity).supportFragmentManager, null)

            datePicker.addOnPositiveButtonClickListener {
                val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                utc.timeInMillis = it
                //val format = SimpleDateFormat("yyyy-MM-dd")
                val format = SimpleDateFormat.getDateInstance()
                val formatted: String = format.format(utc.time)


                viewModel.setStartDate(LocalDate.of(utc.time.year, utc.time.month, utc.time.day))
                binding.startDateTextview.text = formatted
            }
        }

        binding.endDateTextview.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show((context as AppCompatActivity).supportFragmentManager, null)

            datePicker.addOnPositiveButtonClickListener {
                val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                utc.timeInMillis = it
//                val format = SimpleDateFormat("yyyy-MM-dd")
                val format = SimpleDateFormat.getDateInstance()
                val formatted: String = format.format(utc.time)
                
                viewModel.setEndDate(LocalDate.of(utc.time.year, utc.time.month, utc.time.day))
                binding.endDateTextview.text = formatted
            }
        }

    }

//    private fun isEntryValid() : Boolean {
//        return viewModel.isEntryValid(
//            binding.startDateTextview.text.toString(),
//            binding.endDateTextview.text.toString(),
//            binding.spinnerMedFrequency.text.toString(),
//            null
//        )
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SetScheduleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SetScheduleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}
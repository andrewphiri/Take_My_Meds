package com.drew.takemymeds.adapters

import android.content.Context
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drew.takemymeds.databinding.TimeItemListBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.Int



class ScheduleListAdapter(var context: Context,
                          var typeOfMed: String,
                          private val onTimeClickLister: (localTimes: LocalTime, position: Int) -> Unit,
                          private val onDosageClickLister: (dosages: Double, position: Int) -> Unit): ListAdapter<LocalTime,ScheduleListAdapter.ScheduleViewHolder>(DiffCallback) {
    private var hour = 12
    private var minute = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = TimeItemListBinding.inflate(layoutInflater, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {


        val times = getItem(position)
        holder.bind(times, typeOfMed = typeOfMed)

        val isSystem24Hour = is24HourFormat(context)

        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H


        holder.timeSelectedTextView.setOnClickListener {

            if (holder.timeSelectedTextView.text.isNotEmpty()) {
                val split = holder.timeSelectedTextView.text.split(":")
                hour = split[0].toInt()

                minute = if (split[1] == "00") {
                    0
                } else {
                    split[1].toDouble().toInt()
                }
            }
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setHour(hour)
                .setMinute(minute)
                .setTitleText("Select Time")
                .build()
            picker.addOnPositiveButtonClickListener {
                hour = picker.hour
                minute = picker.minute
                holder.timeSelectedTextView.text =
                    String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        hour,
                        minute
                    )
            }
            onTimeClickLister(times, position)
           picker.show((context as AppCompatActivity).supportFragmentManager, null)
        }

        holder.dosageTextView.addTextChangedListener {
            if (it?.isNotEmpty() == true) {
                var text = holder.dosageTextView.text.toString()
                var convertToDouble = text.toDoubleOrNull()
                if (convertToDouble != null) {
                    onDosageClickLister(convertToDouble, position)
                }
            }
        }
    }

    class ScheduleViewHolder(private var binding: TimeItemListBinding): RecyclerView.ViewHolder(binding.root){
        val timeSelectedTextView = binding.timeSelectedTextview
        val dosageTextView = binding.dosagePerTimeTv
        val typeMed = binding.typeMedTextView

        fun bind(localTime: LocalTime, typeOfMed: String){
                binding.timeSelectedTextview.text = localTime.format(
                    DateTimeFormatter.ofPattern("HH:mm "))
            typeMed.text = typeOfMed
        }
    }
    companion object DiffCallback: DiffUtil.ItemCallback<LocalTime>(){
        override fun areItemsTheSame(oldItem: LocalTime, newItem: LocalTime): Boolean {
            return oldItem.hour == newItem.hour && oldItem.minute == newItem.minute
        }

        override fun areContentsTheSame(oldItem: LocalTime, newItem: LocalTime): Boolean {
            return oldItem.nano == newItem.nano
        }
    }
}
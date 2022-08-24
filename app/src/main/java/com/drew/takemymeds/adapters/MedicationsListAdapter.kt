package com.drew.takemymeds.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drew.takemymeds.databinding.MedItemBinding
import com.drew.takemymeds.model.MyMeds
import java.time.format.DateTimeFormatter

class MedicationsListAdapter (
    private val context: Context,
    private val itemClickListener: (MyMeds) -> Unit)
    : ListAdapter<MyMeds, MedicationsListAdapter.MedicationViewHolder>(DiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        val view = MedItemBinding.inflate(layoutInflater, parent, false)
        return MedicationViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        val medication = getItem(position)
        holder.bind(medication)
        val layoutManager = LinearLayoutManager(context)

        val adapter = NestedItemListAdapter()
        holder.recyclerView.adapter = adapter
        holder.recyclerView.layoutManager = layoutManager

        adapter.submitList(medication.myMeds)

        holder.itemView.setOnClickListener {
            itemClickListener(medication)
        }
    }

    class MedicationViewHolder(private var binding: MedItemBinding)
        :RecyclerView.ViewHolder(binding.root) {
            val  recyclerView = binding.nestedListRecyclerview
            fun bind(meds: MyMeds){
                binding.timeLabelTextView.text = meds.startTime[adapterPosition].format(
                    DateTimeFormatter.ofPattern("HH:mm ")
                )
            }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<MyMeds>() {
        override fun areItemsTheSame(oldItem: MyMeds, newItem: MyMeds): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MyMeds, newItem: MyMeds): Boolean {
            return oldItem.startDate == newItem.startDate
        }


    }
}
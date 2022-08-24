package com.drew.takemymeds.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drew.takemymeds.databinding.NestedListItemBinding
import com.drew.takemymeds.model.Medication

class NestedItemListAdapter: ListAdapter<Medication, NestedItemListAdapter.NestedViewHolder> (DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NestedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = NestedListItemBinding.inflate(layoutInflater, parent, false)
        return NestedViewHolder(view)
    }

    override fun onBindViewHolder(holder: NestedViewHolder, position: Int) {
        val medication = getItem(position)
        holder.bind(medication)
    }

    class NestedViewHolder(private var binding: NestedListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

            fun bind(medication: Medication) {
                binding.medLabelTextView.text = medication.name

            }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Medication>() {
        override fun areItemsTheSame(oldItem: Medication, newItem: Medication): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Medication, newItem: Medication): Boolean {
            return oldItem.name == newItem.name
        }

    }
}
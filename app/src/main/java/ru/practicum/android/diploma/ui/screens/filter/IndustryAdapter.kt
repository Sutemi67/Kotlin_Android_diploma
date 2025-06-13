package ru.practicum.android.diploma.ui.screens.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.network.models.Industry

class IndustryAdapter(
    private val onIndustrySelected: (Industry) -> Unit
) : ListAdapter<Industry, IndustryAdapter.IndustryViewHolder>(IndustryDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IndustryViewHolder {
        val binding = ItemIndustryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IndustryViewHolder(binding, onIndustrySelected)
    }

    override fun onBindViewHolder(
        holder: IndustryViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class IndustryViewHolder(
        private val binding: ItemIndustryBinding,
        private val onIndustrySelected: (Industry) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(industry: Industry) {
            binding.apply {
                industryName.text = industry.name
                radioButton.setOnClickListener { onIndustrySelected(industry) }
                root.setOnClickListener {
                    radioButton.isChecked = !radioButton.isChecked
                    onIndustrySelected(industry)
                }
            }
        }
    }

    private class IndustryDiffCallback : DiffUtil.ItemCallback<Industry>() {
        override fun areItemsTheSame(oldItem: Industry, newItem: Industry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Industry, newItem: Industry): Boolean {
            return oldItem == newItem
        }
    }
}

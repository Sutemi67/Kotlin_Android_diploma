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

    private var selectedItemId: String? = null
    private var selectedItemPosition: Int? = null

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
        val item = getItem(position)
        holder.bind(
            item,
            selectedItemId == item.id
        ) {
            if (selectedItemPosition != position) {
                selectItem(position, item.id)
            }
        }
    }

    fun clearSelectedItem() {
        val oldPosition = selectedItemPosition
        selectedItemId = null
        selectedItemPosition = null
        oldPosition?.let {
            notifyItemChanged(it)
            notifyDataSetChanged()
        }
    }

    private fun selectItem(position: Int, id: String) {
        val oldPosition = selectedItemPosition
        selectedItemPosition = position
        selectedItemId = id
        oldPosition?.let { notifyItemChanged(it) }
        notifyItemChanged(position)
    }

    class IndustryViewHolder(
        private val binding: ItemIndustryBinding,
        private val onIndustrySelected: (Industry) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            industry: Industry,
            isSelected: Boolean,
            selectItemCallback: () -> Unit
        ) {
            binding.apply {
                industryName.text = industry.name
                radioButton.isChecked = isSelected

                radioButton.setOnClickListener {
                    if (!isSelected) {
                        selectItemCallback()
                        onIndustrySelected(industry)
                    }
                }

                root.setOnClickListener {
                    if (!isSelected) {
                        selectItemCallback()
                        onIndustrySelected(industry)
                    }
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

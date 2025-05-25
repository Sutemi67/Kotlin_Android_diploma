package ru.practicum.android.diploma.ui.screens.mainScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.network.models.VacancyDetails

class VacancyAdapter : ListAdapter<VacancyDetails, VacancyAdapter.VacancyViewHolder>(VacancyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {

        val binding = ItemVacancyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VacancyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class VacancyViewHolder(
        private val binding: ItemVacancyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vacancyDetails: VacancyDetails) {
            binding.apply {
                jobTitle.text = vacancyDetails.name
                companyName.text = vacancyDetails.employer.name
                location.text = vacancyDetails.area.name

                salary.text = when {
                    vacancyDetails.salary?.from != null && vacancyDetails.salary.to != null ->
                        "от ${vacancyDetails.salary.from} до ${vacancyDetails.salary.to} ${vacancyDetails.salary.currency}"
                    vacancyDetails.salary?.from != null ->
                        "от ${vacancyDetails.salary.from} ${vacancyDetails.salary.currency}"
                    vacancyDetails.salary?.to != null ->
                        "до ${vacancyDetails.salary.to} ${vacancyDetails.salary.currency}"
                    else -> "Зарплата не указана"
                }
            }
        }
    }

    private class VacancyDiffCallback : DiffUtil.ItemCallback<VacancyDetails>() {
        override fun areItemsTheSame(oldItem: VacancyDetails, newItem: VacancyDetails): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VacancyDetails, newItem: VacancyDetails): Boolean {
            return oldItem == newItem
        }
    }
}

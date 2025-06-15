package ru.practicum.android.diploma.ui.screens.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.domain.network.models.FilterSettings

class FilterFragment : Fragment() {

    private val viewModel by viewModel<FilterViewModel>(ownerProducer = { requireActivity() })
    private var _binding: FragmentFilterBinding? = null
    private val binding: FragmentFilterBinding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBindings()
        allFieldsCheck()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBindings() {
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.area.setOnClickListener { }
        binding.workingArea.setOnClickListener {
            val direction = FilterFragmentDirections.actionFilterFragmentToWorkAreaFragment()
            findNavController().navigate(direction)
        }
        binding.industryText.setOnClickListener {
            val direction = FilterFragmentDirections.actionFilterFragmentToWorkAreaFragment()
            findNavController().navigate(direction)
        }
        binding.checkboxFrame.setOnClickListener {
            viewModel.setOnlyWithSalary(binding.checkboxFrame.isChecked)
        }
        binding.clearButton.setOnClickListener {
            allClear()
        }
        binding.clearIndustry.setOnClickListener { binding.salaryInput.text?.clear() }

        binding.salaryInput.addTextChangedListener(
            onTextChanged = { text: CharSequence?, _, _, _ ->
                viewModel.setSalary(text?.toString() ?: "")
                binding.clearIndustry.isVisible = !text.isNullOrEmpty()
                allFieldsCheck()
            }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (binding.salaryInput.text.toString() != state.salary) {
                    binding.salaryInput.setText(state.salary)
                }
                binding.checkboxFrame.isChecked = state.onlyWithSalary
                binding.industryText.setText(state.workArea)
                if (state.workArea.isNotEmpty()) {
                    binding.workAreaIcon.setImageResource(R.drawable.close_24px)
                    binding.workAreaIcon.setOnClickListener {
                        viewModel.setIndustry("")
                        binding.workAreaIcon.setImageResource(R.drawable.outline_arrow_forward_ios_24)
                    }
                } else {
                    binding.workAreaIcon.setImageResource(R.drawable.outline_arrow_forward_ios_24)
                }
                allFieldsCheck()
            }
        }

        binding.applyButton.setOnClickListener {
            val state = viewModel.uiState.value
            val filterSettings = FilterSettings(
                selectedIndustry = state.selectedIndustry,
                salary = state.salary,
                onlyWithSalary = state.onlyWithSalary
            )
            sendFilterAndNavigateBack(filterSettings)
            findNavController().popBackStack()
        }
    }

    private fun FragmentFilterBinding.checkFields(): Boolean =
        binding.industryText.text.isNullOrEmpty() &&
            binding.areaText.text.isNullOrEmpty() &&
            binding.salaryInput.text.isNullOrEmpty() &&
            !binding.checkboxFrame.isChecked

    private fun allFieldsCheck() {
        if (binding.checkFields()) {
            isButtonsGroupVisible(false)
        } else {
            isButtonsGroupVisible(true)
        }
    }

    private fun isButtonsGroupVisible(state: Boolean) {
        when (state) {
            true -> {
                binding.apply {
                    clearButton.isVisible = true
                    applyButton.isVisible = true
                }
            }

            false -> {
                binding.apply {
                    clearButton.isVisible = false
                    applyButton.isVisible = false
                }
            }
        }
    }

    private fun allClear() {
        viewModel.resetFilters()
        binding.areaText.setText("")
        binding.industryText.setText("")
        binding.salaryInput.text = null
        binding.checkboxFrame.isChecked = false
        allFieldsCheck()
        val clearedFilter = FilterSettings(
            selectedIndustry = null,
            salary = "",
            onlyWithSalary = false
        )
        sendFilterAndNavigateBack(clearedFilter)
    }

    private fun sendFilterAndNavigateBack(filterSettings: FilterSettings) {
        findNavController().previousBackStackEntry
            ?.savedStateHandle
            ?.set("filter_settings", filterSettings)
    }
}

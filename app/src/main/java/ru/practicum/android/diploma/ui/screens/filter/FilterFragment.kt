package ru.practicum.android.diploma.ui.screens.filter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.domain.network.models.FilterSettings

class FilterFragment : Fragment() {

    private val viewModel by viewModel<FilterViewModel>(ownerProducer = { requireActivity() })
    private var _binding: FragmentFilterBinding? = null
    private val binding: FragmentFilterBinding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

    override fun onResume() {
        super.onResume()
        setupIndustryField()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupBindings() {
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.area.setOnClickListener { }
        binding.workingArea.setOnClickListener {
            val direction = FilterFragmentDirections.actionFilterFragmentToWorkAreaFragment()
            findNavController().navigate(direction)
        }
//        binding.salaryCheckBoxLinearLayout.setOnClickListener {
//            binding.checkboxFrame.isChecked = !binding.checkboxFrame.isChecked
//            allFieldsCheck()
//        }
        binding.checkboxFrame.setOnClickListener {
            viewModel.setOnlyWithSalary(binding.checkboxFrame.isChecked)
        }
        binding.clearButton.setOnClickListener {
            allClear()
        }
        binding.salaryInput.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val drawableEnd = binding.salaryInput.compoundDrawablesRelative[2]
                if (drawableEnd != null) {
                    val touchableWidth = drawableEnd.intrinsicWidth + binding.salaryInput.paddingEnd
                    val isClickOnDrawable = event.rawX >= binding.salaryInput.right - touchableWidth
                    if (isClickOnDrawable) {
                        binding.salaryInput.text?.clear()
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            } else {
                false
            }
        }

        binding.salaryInput.addTextChangedListener(
            onTextChanged = { text: CharSequence?, _, _, _ ->
                viewModel.setSalary(text?.toString() ?: "")
                if (!text.isNullOrEmpty()) {
                    binding.salaryInput.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        null,
                        null,
                        imageChooser(text),
                        null
                    )
                }
                allFieldsCheck()
            },
            afterTextChanged = { _: Editable? -> }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.salary.collect { salary ->
                if (binding.salaryInput.text.toString() != salary) {
                    binding.salaryInput.setText(salary)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.onlyWithSalary.collect { onlyWithSalary ->
                binding.checkboxFrame.isChecked = onlyWithSalary
            }
        }

        binding.applyButton.setOnClickListener {
            val selectedIndustry = viewModel.selectedIndustry.value
            val filterSettings = FilterSettings(
                selectedIndustry = selectedIndustry,
                salary = viewModel.salary.value,
                onlyWithSalary = viewModel.onlyWithSalary.value
            )
            sendFilterAndNavigateBack(filterSettings)
            findNavController().popBackStack()

        }
    }

    private fun setupIndustryField() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.workArea.collect { data ->
                binding.industryText.setText(data)
                Log.d("area1", "Значение $data установлено")
                if (!binding.industryText.text.isNullOrEmpty()) {
                    binding.workAreaIcon.setImageResource(R.drawable.close_24px)
                    binding.workAreaIcon.setOnClickListener {
                        viewModel.setIndustry("")
                        binding.workAreaIcon.setImageResource(R.drawable.outline_arrow_forward_ios_24)
                    }
                    allFieldsCheck()
                }
            }
        }
    }

    private fun imageChooser(text: CharSequence?): Drawable? {
        return if (text.isNullOrBlank()) {
            null
        } else {
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.close_24px
            )
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

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
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding

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
        setupWorkAreaField()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupBindings() {
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.area.setOnClickListener { }
        binding.workingArea.setOnClickListener {
            val direction = FilterFragmentDirections.actionFilterFragmentToWorkAreaFragment()
            findNavController().navigate(direction)
        }
        binding.clearButton.isVisible = true
        binding.applyButton.isVisible = true

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
                binding.salaryInput.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    imageChooser(text),
                    null
                )
            },
            afterTextChanged = { _: Editable? -> }
        )
    }

    private fun setupWorkAreaField() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.workArea.collect { data ->
                binding.workingAreaText.text = data
                Log.d("area", "Значение $data установлено")
                if (binding.workingAreaText.text != "Отрасль") {
                    binding.workAreaIcon.setImageResource(R.drawable.close_24px)
                    binding.workingAreaText.setTextColor(resources.getColor(R.color.black_universal))
                    binding.workAreaIcon.setOnClickListener {
                        viewModel.setWorkingArea("Отрасль")
                        binding.workAreaIcon.setImageResource(R.drawable.outline_arrow_forward_ios_24)
                        binding.workingAreaText.setTextColor(resources.getColor(R.color.gray))
                    }
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
}

package ru.practicum.android.diploma.ui.screens.filter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding

class FilterFragment : Fragment() {
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
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupBindings() {
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.area.setOnClickListener { }
        binding.workingArea.setOnClickListener { }
        binding.clearButton.isVisible = true
        binding.applyButton.isVisible = true

        binding.salaryInput.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val drawableEnd = binding.salaryInput.compoundDrawablesRelative[2]
                if (drawableEnd != null) {
                    val touchableWidth = drawableEnd.intrinsicWidth + binding.salaryInput.paddingEnd
                    if (event.rawX >= (binding.salaryInput.right - touchableWidth)) {
                        binding.salaryInput.text?.clear()
                        return@setOnTouchListener true
                    }
                }
            }
            false
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

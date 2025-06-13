package ru.practicum.android.diploma.ui.screens.filter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentIndustryBinding

class IndustryFragment : Fragment() {

    private val viewModel by viewModel<FilterViewModel>(ownerProducer = { requireActivity() })
    private var _binding: FragmentIndustryBinding? = null
    private val binding: FragmentIndustryBinding get() = requireNotNull(_binding)

    private val adapter = IndustryAdapter { industry ->
        viewModel.onSelectIndustry(industry)
        binding.applyButton.isVisible = true
        binding.industryFilterField.setText(industry.name)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupToolbar()
        observeIndustries()
        setupFilterField()
        setupClickListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupFilterField() {
        binding.industryFilterField.addTextChangedListener(
            onTextChanged = { text: CharSequence?, _, _, _ ->
                viewModel.filterList(text)
                if (!text.isNullOrEmpty()) {
                    binding.industryFilterField.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        null,
                        null,
                        imageChooser(text),
                        null
                    )
                    setClearIcon()
                }
            },
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setClearIcon() {
        binding.industryFilterField.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val drawableEnd = binding.industryFilterField.compoundDrawablesRelative[2]
                if (drawableEnd != null) {
                    val touchableWidth = drawableEnd.intrinsicWidth + binding.industryFilterField.paddingEnd
                    val isClickOnDrawable = event.rawX >= binding.industryFilterField.right - touchableWidth
                    if (isClickOnDrawable) {
                        binding.industryFilterField.text?.clear()
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

    private fun setupClickListeners() {
        binding.applyButton.setOnClickListener {
            viewModel.setIndustry(binding.industryFilterField.text.toString())
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {
        binding.industriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@IndustryFragment.adapter
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeIndustries() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                adapter.submitList(state.industries)
                binding.errorImage.isVisible = state.isError
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.resetList()
        Log.d("list", "onDestroyView has done, list has reset")
    }
}

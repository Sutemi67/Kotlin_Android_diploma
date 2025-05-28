package ru.practicum.android.diploma.ui.screens.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentMainBinding
import ru.practicum.android.diploma.util.debounce
import ru.practicum.android.diploma.util.Resource

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = requireNotNull(_binding)
    private val adapter: VacancyAdapter = VacancyAdapter()
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()

        binding.buttonCleanSearch.setOnClickListener {
            binding.searchView.setText("")
            val inputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.buttonCleanSearch.windowToken, 0)
            //   adapter.notifyDataSetChanged()
            binding.errorMessage.isVisible = false
            binding.imageStart.isVisible = true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MainFragment.adapter
        }
    }

    private fun setupSearchView() {
        val searchDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.search_24px)
        val debouncedSearch = debounce(
            delayMillis = 1000L,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = true
        ) { query: String ->
            if (query.isBlank()) {
                binding.recyclerView.isVisible = false
                binding.errorMessage.isVisible = false
                binding.imageStart.isVisible = true
                adapter.submitList(emptyList())
            } else {
                viewModel.searchVacancies(query)
            }
        }

        binding.searchView.addTextChangedListener(
            onTextChanged = { p0: CharSequence?, _, _, _ ->
                debouncedSearch(p0?.toString() ?: "")
                if (binding.searchView.hasFocus() && binding.searchView.text.isEmpty()) {
                    binding.searchView.setCompoundDrawablesWithIntrinsicBounds(null, null, searchDrawable, null)
                    binding.buttonCleanSearch.isVisible = false
                    binding.imageStart.isVisible = true
                } else {
                    binding.searchView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                    binding.buttonCleanSearch.isVisible = true
                }
            },
            afterTextChanged = { _: Editable? ->
                binding.errorMessage.isVisible = false
            }
        )
    }

    private fun observeViewModel() {
        viewModel.searchState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {
                    binding.recyclerView.isVisible = true
                    binding.errorMessage.isVisible = false
                    binding.imageStart.isVisible = false
                    adapter.submitList(state.data)
                }
                is Resource.Error -> {
                    binding.recyclerView.isVisible = false
                    binding.errorMessage.isVisible = true
                    binding.imageStart.isVisible = true
                    binding.errorText.text = state.message
                    adapter.submitList(emptyList())
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            if (isLoading) {
                binding.imageStart.isVisible = false
            }
        }
    }

    private fun showMessage(text: String, additionalMessage: String, drawable: Int) =
        with(binding) {
            imageStart.isVisible = false
            progressBar.isVisible = false
            recyclerView.isVisible = false
            imageView.setImageResource(drawable)
            if (text.isNotEmpty()) {
                errorMessage.isVisible = true
                adapter.notifyDataSetChanged()
                errorText.text = text
            } else {
                errorMessage.isVisible = false
            }
        }
}

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentMainBinding
import ru.practicum.android.diploma.domain.OnItemClickListener
import ru.practicum.android.diploma.domain.network.models.VacancyDetails
import ru.practicum.android.diploma.util.debounce

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = requireNotNull(_binding)
    private var adapter: VacancyAdapter? = null
    private val viewModel by viewModel<MainViewModel>()
    private var isClickAllowed = true

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
            adapter?.submitList(emptyList())
            val inputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.buttonCleanSearch.windowToken, 0)
            binding.errorMessage.isVisible = false
            binding.imageStart.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val onItemClickListener = OnItemClickListener<VacancyDetails> { vacancy ->
            if (clickDebounce()) {
                val action = MainFragmentDirections.actionHomeFragmentToDetailsFragment(vacancy.id)
                findNavController().navigate(action)
            }
        }

        binding.recyclerView.apply {
            this@MainFragment.adapter = VacancyAdapter(onItemClickListener)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MainFragment.adapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

//                    if (!viewModel.isLoading.value!! &&
//                        visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
//                        firstVisibleItemPosition >= 0
//                    ) {
//                        viewModel.loadMoreItems()
//                    }
                }
            })
        }
    }

    private fun setupSearchView() {
        val searchDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.search_24px)
        val debouncedSearch = debounce(
            delayMillis = CLICK_DEBOUNCE_DELAY,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = true
        ) { query: String ->
            if (query.isBlank()) {
                binding.recyclerView.isVisible = false
                binding.errorMessage.isVisible = false
                binding.imageStart.isVisible = true
                adapter?.submitList(emptyList())
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
                is UiState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.imageStart.isVisible = false
                }

                is UiState.Content -> {
                    binding.recyclerView.isVisible = true
                    binding.errorMessage.isVisible = false
                    binding.imageStart.isVisible = false
                    binding.progressBar.isVisible = false
                    binding.infoSearch.text = "Найдено ${state.vacancies.size} вакансий"
                    adapter?.submitList(state.vacancies)
                }

                is UiState.NotFound -> {
                    showMessage(getString(R.string.empty_search), "1", R.drawable.image_kat)
                }

                is UiState.Error -> {
                    showMessage(getString(R.string.no_internet), "1", R.drawable.image_skull)
                }

                is UiState.Idle -> {
                    binding.recyclerView.isVisible = false
                    binding.errorMessage.isVisible = false
                    binding.imageStart.isVisible = true
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
                adapter?.submitList(emptyList())
                errorText.text = text
            } else {
                errorMessage.isVisible = false
            }
        }

    private fun clickDebounce(): Boolean {
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return true
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}

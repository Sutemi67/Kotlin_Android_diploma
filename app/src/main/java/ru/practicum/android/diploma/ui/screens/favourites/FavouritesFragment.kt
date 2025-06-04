package ru.practicum.android.diploma.ui.screens.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentFavouritesBinding
import ru.practicum.android.diploma.domain.OnItemClickListener
import ru.practicum.android.diploma.domain.network.models.VacancyDetails
import ru.practicum.android.diploma.ui.screens.main.VacancyAdapter

class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding: FragmentFavouritesBinding get() = requireNotNull(_binding)
    private val viewModel by viewModel<FavouritesViewModel>()
    private var adapter: VacancyAdapter? = null
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        viewModel.getFavoriteVacancy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val onItemClickListener = OnItemClickListener<VacancyDetails> { vacancy ->
            if (clickDebounce()) {
                val action = FavouritesFragmentDirections.actionFavouriteFragmentToDetailsFragment(vacancy.id)
                findNavController().navigate(action)
            }
        }

        binding.recyclerView.apply {
            this@FavouritesFragment.adapter = VacancyAdapter(onItemClickListener)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavouritesFragment.adapter

        }
    }

    private fun observeViewModel() {
        viewModel.favouriteState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiStateFavourites.Content -> {
                    adapter?.submitList(state.vacancy)
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.emptyList.visibility = View.GONE

                }

                is UiStateFavourites.Empty -> {
                    binding.recyclerView.visibility = View.GONE
                    binding.emptyList.visibility = View.VISIBLE

                }

                is UiStateFavourites.Error -> {
                    binding.recyclerView.visibility = View.GONE
                    binding.emptyList.visibility = View.GONE
                    binding.errorList.visibility = View.VISIBLE
                }
            }
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

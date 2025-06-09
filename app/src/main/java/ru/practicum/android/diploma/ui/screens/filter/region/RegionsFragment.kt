package ru.practicum.android.diploma.ui.screens.filter.region

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.bundle.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ru.practicum.android.diploma.databinding.FragmentRegionBinding

class RegionsFragment : Fragment() {

    private var _binding: FragmentRegionBinding? = null
    private val binding: FragmentRegionBinding get() = requireNotNull(_binding)

    private val viewModel: AreasViewModel by viewModels { /* ViewModelProvider.Factory */ }
    private lateinit var adapter: AreaAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: android.os.Bundle?
    ): View? {
        _binding = FragmentRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val country = /* получить из аргументов */
            adapter = AreaAdapter(emptyList()) { region ->
                // обработка выбора региона
            }

        binding.recyclerView.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.regions.collect { list ->
                adapter.submitList(list)
            }
        }
        viewModel.loadRegions(country)
    }
}

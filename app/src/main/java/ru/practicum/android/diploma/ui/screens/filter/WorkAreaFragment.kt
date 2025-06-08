package ru.practicum.android.diploma.ui.screens.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentWorkAreaBinding

class WorkAreaFragment : Fragment() {

    private val viewModel by viewModel<FilterViewModel>(ownerProducer = { requireActivity() })
    private var _binding: FragmentWorkAreaBinding? = null
    private val binding: FragmentWorkAreaBinding get() = requireNotNull(_binding)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWorkAreaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.sampleText.setOnClickListener {
            viewModel.setWorkingArea("Установлено!")
            binding.sampleText.text = "Успешно!"
        }
    }
}

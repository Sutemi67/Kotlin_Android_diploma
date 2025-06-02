package ru.practicum.android.diploma.ui.screens.vacancy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentDetailsBinding
import ru.practicum.android.diploma.domain.network.models.VacancyDetails

class VacancyDetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding get() = requireNotNull(_binding)
    private val viewModel by viewModel<VacancyDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailsBinding.bind(view)

        val args = arguments?.let { VacancyDetailsFragmentArgs.fromBundle(it) }
        val vacancyId = args?.vacancyId
        viewModel.getVacancyDetails(vacancyId.toString())
        Log.i("Log1", "id = $vacancyId")

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_share -> {
                    // код для "Поделиться"
                    true
                }

                R.id.action_favorite -> {
                    //   viewModel.onFavoriteClicked()
                    true
                }

                else -> false
            }
        }

        viewModel.vacancyDetails.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiStateVacancy.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.content.isVisible = false
                    binding.errorMessage.isVisible = false
                }

                is UiStateVacancy.Content -> {
                    showVacancy(state.vacancy)
                }

                is UiStateVacancy.Error -> {
                    showMessage(getString(R.string.vacancy_not_found_deleted), "", R.drawable.sponge_bob)
                }

                is UiStateVacancy.ErrorService -> {
                    showMessage(getString(R.string.error_service), "", R.drawable.kat_error_service)
                }
            }
        }
    }

    private fun showVacancy(vacancy: VacancyDetails) {
        binding.progressBar.isVisible = false
        binding.content.isVisible = true
        binding.errorMessage.isVisible = false
        binding.jobTitle.text = vacancy.name
        binding.salary.text = vacancy.salary?.let {
            when {
                it.from != null && it.to != null -> "от ${it.from} до ${it.to} ${it.currency}"
                it.from != null -> "от ${it.from} ${it.currency}"
                it.to != null -> "до ${it.to} ${it.currency}"
                else -> getString(R.string.salary_is_not_specified)
            }
        } ?: getString(R.string.salary_is_not_specified)

        binding.companyName.text = vacancy.employer.name
        binding.area.text = vacancy.area.name

        Glide.with(this)
            .load(vacancy.employer.logoUrls?.original)
            .placeholder(R.drawable.empty_image)
            .fitCenter()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.indent_12dp)))
            .into(binding.companyLogo)

        binding.experience.text = vacancy.experience.name

        binding.schedule.text = buildString {
            append(vacancy.schedule.name)
            append(" , ")
            append(vacancy.employment.name)
            append(" ")
        }

        binding.description.text = HtmlCompat.fromHtml(
            vacancy.description ?: "",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun showMessage(text: String, additionalMessage: String, drawable: Int) =
        with(binding) {
            progressBar.isVisible = false
            content.isVisible = false
            errorMessage.isVisible = true
            imageView.setImageResource(drawable)
            errorText.text = text
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

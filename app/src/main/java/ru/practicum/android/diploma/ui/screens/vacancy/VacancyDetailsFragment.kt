package ru.practicum.android.diploma.ui.screens.vacancy

import android.os.Bundle
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
import ru.practicum.android.diploma.domain.network.models.Salary
import ru.practicum.android.diploma.domain.network.models.VacancyDetails

class VacancyDetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding get() = requireNotNull(_binding)
    private val viewModel by viewModel<VacancyDetailsViewModel>()
    private var vacancyOnScreen: VacancyDetails? = null

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

        setupToolbar()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_share -> {
                    viewModel.shareVacancy(vacancyOnScreen?.id ?: "")
                    true
                }

                R.id.action_favorite -> {
                    vacancyOnScreen?.let { viewModel.toggleFavorite(it) }
                    true
                }

                else -> false
            }
        }
    }

    private fun observeViewModel() {
        val args = arguments?.let { VacancyDetailsFragmentArgs.fromBundle(it) }
        val vacancyId = args?.vacancyId
        viewModel.getVacancyDetails(vacancyId.toString())
        viewModel.vacancyDetails.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiStateVacancy.Loading -> showLoading()
                is UiStateVacancy.Content -> {
                    showVacancy(state.vacancy)
                    vacancyOnScreen = state.vacancy
                }

                is UiStateVacancy.Error -> {
                    showMessage(getString(R.string.vacancy_not_found_deleted), "", R.drawable.sponge_bob)
                }

                is UiStateVacancy.ErrorService -> {
                    showMessage(getString(R.string.error_service), "", R.drawable.kat_error_service)
                }
            }
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            binding.toolbar.menu.findItem(R.id.action_favorite)?.setIcon(
                if (isFavorite) R.drawable.favorites_on__24px else R.drawable.favorites_off__24px
            )
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.content.isVisible = false
        binding.errorMessage.isVisible = false
    }

    private fun showVacancy(vacancy: VacancyDetails) {
        showContent()
        updateBasicInfo(vacancy)
        updateCompanyInfo(vacancy)
        updateJobDetails(vacancy)
        updateDescription(vacancy)
    }

    private fun showContent() {
        binding.progressBar.isVisible = false
        binding.content.isVisible = true
        binding.errorMessage.isVisible = false
    }

    private fun updateBasicInfo(vacancy: VacancyDetails) {
        binding.jobTitle.text = vacancy.name
        binding.salary.text = formatSalary(vacancy.salary)
    }

    private fun formatSalary(salary: Salary?): String {
        return salary?.let {
            when {
                it.from != null && it.to != null -> getString(
                    R.string.salary_range,
                    it.from.toString(),
                    it.to.toString(),
                    it.currency
                )

                it.from != null -> getString(
                    R.string.salary_from,
                    it.from.toString(),
                    it.currency
                )

                it.to != null -> getString(
                    R.string.salary_to,
                    it.to.toString(),
                    it.currency
                )

                else -> getString(R.string.salary_not_specified)
            }
        } ?: getString(R.string.salary_not_specified)
    }

    private fun updateCompanyInfo(vacancy: VacancyDetails) {
        binding.companyName.text = vacancy.employer.name
        binding.area.text = vacancy.area.name
        loadCompanyLogo(vacancy.employer.logoUrls?.original)
    }

    private fun loadCompanyLogo(logoUrl: String?) {
        Glide.with(this)
            .load(logoUrl)
            .placeholder(R.drawable.empty_image)
            .fitCenter()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.indent_12dp)))
            .into(binding.companyLogo)
    }

    private fun updateJobDetails(vacancy: VacancyDetails) {
        binding.experience.text = vacancy.experience.name
        binding.schedule.text = buildString {
            append(vacancy.schedule.name)
            append(" , ")
            append(vacancy.employment.name)
            append(" ")
        }
    }

    private fun updateDescription(vacancy: VacancyDetails) {
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

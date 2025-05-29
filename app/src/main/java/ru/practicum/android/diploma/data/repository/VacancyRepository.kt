package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.VacancyRepositoryInterface
import ru.practicum.android.diploma.domain.network.api.HhApi
import ru.practicum.android.diploma.domain.network.models.AllVacancyRequest
import ru.practicum.android.diploma.domain.network.models.AllVacancyResponse
import ru.practicum.android.diploma.domain.network.models.VacancyDetails
import ru.practicum.android.diploma.util.Resource

class VacancyRepository(
    private val networkClient: NetworkClient
) : VacancyRepositoryInterface {

    override fun searchVacancies(query: String): Flow<Resource<List<VacancyDetails>>> = flow {
        val response = networkClient.doSearchRequest(AllVacancyRequest(query))
        when (response.resultCode) {
            ERROR_NO_CONNECTION -> emit(Resource.Error("Ошибка"))
            SUCCESS -> {
                with(response as AllVacancyResponse) {
                    val data = items.mapNotNull {
                        VacancyDetails(
                            id = it.id,
                            name = it.name,
                            salary = it.salary,
                            area = it.area,
                            employer = it.employer,
                            snippet = it.snippet,
                            alternateUrl = it.alternateUrl
                        )
                    }
                    emit(Resource.Success(data, data.size))
                }
            }
        }
    }

//    override suspend fun getVacancyDetails(
//        id: String
//    ): VacancyDetails {
//        return api.getVacancyDetails(token, id)

    companion object {
        private const val ERROR_NO_CONNECTION = -1
        private const val ERROR_INVALID_DTO = 400
        private const val ERROR_IO_EXCEPTION = 500
        private const val SUCCESS = 200
    }

}

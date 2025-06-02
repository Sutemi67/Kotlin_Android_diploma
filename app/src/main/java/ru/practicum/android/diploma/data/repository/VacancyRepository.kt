package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.VacancyRequest
import ru.practicum.android.diploma.data.dto.VacancyResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.VacancyRepositoryInterface
import ru.practicum.android.diploma.data.dto.AllVacancyRequest
import ru.practicum.android.diploma.data.dto.AllVacancyResponse
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
                    val data = items.map {
                        VacancyDetails(
                            id = it.id,
                            name = it.name,
                            salary = it.salary,
                            area = it.area,
                            employer = it.employer,
                            experience = it.experience,
                            snippet = it.snippet,
                            alternateUrl = it.alternateUrl,
                            schedule = it.schedule,
                            employment = it.employment,
                            description = it.description,
                        )
                    }
                    emit(Resource.Success(data = data, itemsCount = response.found))
                }
            }
        }
    }

    override suspend fun getVacancyDetails(id: String): Flow<Resource<VacancyDetails>> = flow {
        val response = networkClient.doSearchRequest(VacancyRequest(id))
        when (response.resultCode) {
            ERROR_NO_CONNECTION -> emit(Resource.Error("Проверьте подключение к интернету"))
            SUCCESS -> {
                with(response as VacancyResponse) {
                    emit(
                        Resource.Success(
                           data = VacancyDetails(
                                id = id,
                                name = name,
                                salary = salary,
                                area = area,
                                employer = employer,
                                experience = experience,
                                snippet = snippet,
                                alternateUrl = alternateUrl,
                                schedule = schedule,
                                employment = employment,
                                description = description,
                            ), itemsCount = response.resultCode
                        )
                    )
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))

            }
        }
    }

    companion object {
        private const val ERROR_NO_CONNECTION = -1
        private const val ERROR_INVALID_DTO = 400
        private const val ERROR_IO_EXCEPTION = 500
        private const val SUCCESS = 200
    }

}

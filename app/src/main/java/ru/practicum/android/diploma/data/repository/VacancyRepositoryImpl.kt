package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.converters.VacancyDbConvertor
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.data.dto.AllVacancyRequest
import ru.practicum.android.diploma.data.dto.AllVacancyResponse
import ru.practicum.android.diploma.data.dto.VacancyRequest
import ru.practicum.android.diploma.data.dto.VacancyResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.VacancyRepository
import ru.practicum.android.diploma.domain.network.models.VacancyDetails
import ru.practicum.android.diploma.util.Resource

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient,
    private val db: AppDatabase,
    private val convertor: VacancyDbConvertor,
) : VacancyRepository {

    override fun searchVacancy(query: String, page: Int): Flow<Triple<List<VacancyDetails>?, String?, String?>> = flow {
        val response = networkClient.doSearchRequest(AllVacancyRequest(query, page))
        when (response.resultCode) {
            ERROR_NO_CONNECTION -> emit(Triple(null, R.string.error.toString(), null))
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
                            alternateUrl = it.alternateUrl,
                            schedule = it.schedule,
                            employment = it.employment,
                            description = it.description,
                        )
                    }
                    emit(Triple(data, null, response.found.toString()))
                }
            }
        }
    }

    override suspend fun getVacancyDetails(id: String): Flow<Resource<VacancyDetails>> = flow {
        val response = networkClient.getVacancyDetails(VacancyRequest(id))
        when (response.resultCode) {
            ERROR_NO_CONNECTION -> emit(Resource.Error(R.string.check_your_internet_connection.toString()))
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
                                alternateUrl = alternateUrl,
                                schedule = schedule,
                                employment = employment,
                                description = description,
                            ),
                            itemsCount = response.resultCode
                        )
                    )
                }
            }

            else -> {
                emit(Resource.Error(R.string.error_service.toString()))
            }
        }
    }

    override suspend fun addToFavorites(vacancy: VacancyEntity) {
        db.vacancyDao().addVacancy(vacancy)
    }

    override suspend fun removeFromFavorites(vacancyId: Int) {
        db.vacancyDao().deleteVacancy(vacancyId)
    }

    override suspend fun getFavoriteVacancy(vacancyId: Int): VacancyEntity? {
        return db.vacancyDao().getVacancyById(vacancyId)
    }

    override fun getAllFavoriteVacancy(): Flow<List<VacancyDetails>> = flow {
        val vacancyEntity = db.vacancyDao().getAllVacancy()
        emit(convertFromVacancyEntity(vacancyEntity))
    }

    private fun convertFromVacancyEntity(vacancy: List<VacancyEntity>): List<VacancyDetails> {
        return vacancy.map { vacancy -> convertor.mapToDomain(vacancy) }
    }

    companion object {
        private const val ERROR_NO_CONNECTION = -1
        private const val SUCCESS = 200
    }

}

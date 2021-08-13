//package de.timbo.coinOracle.usecases
//
//import kotlinx.coroutines.async
//import kotlinx.coroutines.withContext
//
//class MultifetchUseCase {
//
//
//    class GetAppointmentsUseCase : BaseUseCase() {
//
//        private val customizer by inject<Customizer>()
//        private val appointmentRepository by inject<AppointmentRepository>()
//
//        suspend fun call(articleId: Long, ids: List<Int>): UseCaseResult<List<Appointment>> {
//            val appointments = appointmentRepository.getAppointmentsByIds(articleId, ids, customizer.code).map { entity -> Appointment(entity) }
//            return when (appointments.size) {
//                ids.size -> UseCaseResult.Success(appointments)
//                else -> UseCaseResult.Failure()
//            }
//        }
//    }
//
//
//    // REPO
//
//    package de.bkkdachverband.healthforme.repositories
//
//    import de.bkkdachverband.healthforme.api.ResponseEvaluator
//    import de.bkkdachverband.healthforme.api.model.request.AppointmentRequestDto
//    import de.bkkdachverband.healthforme.database.dao.AppointmentDao
//    import de.bkkdachverband.healthforme.database.entities.AppointmentEntity
//    import kotlinx.coroutines.async
//    import kotlinx.coroutines.awaitAll
//    import kotlinx.coroutines.withContext
//    import org.koin.core.component.inject
//
//    class AppointmentRepository : BaseRepository() {
//
//        private val appointmentDao by inject<AppointmentDao>()
//
//        suspend fun getAppointmentById(id: Int, clientCode: String) = apiCall { api.getAppointmentById(AppointmentRequestDto(id.toString(), clientCode)) }
//
//        suspend fun getAppointmentsByIds(articleId: Long, ids: List<Int>, clientCode: String): List<AppointmentEntity> {
//            return withContext(repositoryScope.coroutineContext) {
//                val appointmentEntities = ids.map { id ->
//                    async { getAppointmentById(id, clientCode) }
//                }.awaitAll().mapNotNull { apiResult ->
//                    if (apiResult is ResponseEvaluator.Result.Success) apiResult.response.body()?.let { appointmentDto -> AppointmentEntity(articleId.toInt(), appointmentDto) }
//                    else null
//                }
//                return@withContext if (appointmentEntities.isNotEmpty()) updateLocalAppointments(articleId, appointmentEntities)
//                else getAppointmentsByIdsFromDb(ids)
//            }
//        }
//
//        private suspend fun updateLocalAppointments(articleId: Long, appointmentEntities: List<AppointmentEntity>): List<AppointmentEntity> {
//            if (appointmentEntities.isEmpty()) return emptyList()
//            deleteAllAppointmentsByArticleId(articleId)
//            saveAppointments(appointmentEntities)
//            val ids = appointmentEntities.map { entity -> entity.id }
//            return getAppointmentsByIdsFromDb(ids)
//        }
//
//        suspend fun saveAppointments(appointments: List<AppointmentEntity>) {
//            appointmentDao.insertOrUpdate(appointments)
//        }
//
//        private suspend fun getAppointmentsByIdsFromDb(ids: List<Int>): List<AppointmentEntity> {
//            return appointmentDao.getAppointmentsByIds(ids)
//        }
//
//        private suspend fun deleteAllAppointmentsByArticleId(articleId: Long) {
//            appointmentDao.deleteAllApointmentsByArticleId(articleId.toInt())
//        }
//    }
//
//
//    // REPOSITORY
//    private suspend fun updateLocalAssistance(articleId: Long, assistanceDto: AssistanceDto): Assistance? {
//        val assistanceEntity = AssistanceEntity(articleId, assistanceDto)
//        updateAssistance(assistanceEntity)
//        assistanceDto.challenges?.map { challengeDto -> ChallengeEntity(challengeDto, assistanceDto.id.toLong()) }?.let { entities -> updateChallenges(entities) }
//        return withContext(repositoryScope.coroutineContext) {
//            val challengeEntities = async { challengeDao.getChallengesByAssistanceId(assistanceEntity.id) }.await()
//            val assistance = async { assistanceDao.getAssistanceById(assistanceEntity.id)?.let { entity -> Assistance(entity) } }.await()
//            return@withContext when {
//                challengeEntities.isNullOrEmpty() && assistance == null -> null
//                else -> getAssistanceByIdFromDb(assistanceEntity.id)
//            }
//        }
//    }
//
//}
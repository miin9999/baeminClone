package aop.fastcampus.part06.chapter01.data.repository.user

import aop.fastcampus.part06.chapter01.data.entity.LocationLatLngEntity
import kotlinx.coroutines.withContext

interface UserRepository {

    suspend fun getUserLocation(): LocationLatLngEntity?

    suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity)
}
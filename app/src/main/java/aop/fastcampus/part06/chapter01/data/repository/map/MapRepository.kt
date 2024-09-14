package aop.fastcampus.part06.chapter01.data.repository.map

import aop.fastcampus.part06.chapter01.data.entity.LocationLatLngEntity
import aop.fastcampus.part06.chapter01.data.response.address.AddressInfo

interface MapRepository {

    suspend fun getReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity
    ):AddressInfo?

}
package aop.fastcampus.part06.chapter01.data.repository.map

import android.util.Log
import aop.fastcampus.part06.chapter01.data.entity.LocationLatLngEntity
import aop.fastcampus.part06.chapter01.data.network.MapApiService
import aop.fastcampus.part06.chapter01.data.response.address.AddressInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient

class DefaultMapRepository(
    private val mapApiService: MapApiService,
    private val ioDispatcher: CoroutineDispatcher,
): MapRepository {


    override suspend fun getReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity
    ): AddressInfo? = withContext(ioDispatcher){
        val response = mapApiService.getReverseGeoCode(
            lat = locationLatLngEntity.latitude,
            lon = locationLatLngEntity.longitude
        )
        if(response.isSuccessful){
            response.body()?.addressInfo
        }else{
            null
        }
    }
}
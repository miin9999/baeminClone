package aop.fastcampus.part06.chapter01.screen.main.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import aop.fastcampus.part06.chapter01.R
import aop.fastcampus.part06.chapter01.data.entity.LocationLatLngEntity
import aop.fastcampus.part06.chapter01.data.entity.MapSearchInfoEntity
import aop.fastcampus.part06.chapter01.data.repository.map.MapRepository
import aop.fastcampus.part06.chapter01.data.repository.user.UserRepository
import aop.fastcampus.part06.chapter01.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Suppress("UNREACHABLE_CODE", "UNUSED_EXPRESSION")
class HomeViewModel(
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository
) :BaseViewModel(){

    companion object{
        const val MY_LOCATION_KEY = "MyLocation"
    }

    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    fun loadReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity)
    = viewModelScope.launch {
        homeStateLiveData.value = HomeState.Loading
        val userLocation = userRepository.getUserLocation()
        Log.d("userLocation",userLocation.toString())

        val currentLocation = userLocation ?: locationLatLngEntity // null일 경우 locationLatLngEntity가 현재 내 위치가 됨
        Log.d("currentLocation",currentLocation.toString())

        val addressInfo = mapRepository.getReverseGeoInformation(currentLocation)
        addressInfo?.let{ info->
            Log.d("HomeState","여기 지나가면 success가 됨")
            homeStateLiveData.value = HomeState.Success(
                mapSearchInfoEntity = info.toSearchInfoEntity(locationLatLngEntity),
                isLocationSame = currentLocation == locationLatLngEntity
            )

        }?:kotlin.run{

            homeStateLiveData.value = HomeState.Error(
                R.string.can_not_load_address_info
            )
        }
    }


    fun getMapSearchInfo(): MapSearchInfoEntity? {
        when (val data = homeStateLiveData.value) {
            is HomeState.Success -> {
                return data.mapSearchInfoEntity
            }

            else -> {}
        }
        return null
    }

}
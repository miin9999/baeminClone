package aop.fastcampus.part06.chapter01.screen.main.home.restaurant

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import aop.fastcampus.part06.chapter01.data.entity.LocationLatLngEntity
import aop.fastcampus.part06.chapter01.data.repository.restaurant.RestaurantRepository
import aop.fastcampus.part06.chapter01.model.restaurant.RestaurantModel
import aop.fastcampus.part06.chapter01.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantListViewModel(
    private val restaurantCategory: RestaurantCategory,
    private var locationLatLng: LocationLatLngEntity,
    private val restaurantRepository: RestaurantRepository
):BaseViewModel() {

    val restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()


    override fun fetchData(): Job = viewModelScope.launch {

        Log.d("restaurantCategory",restaurantCategory.toString())
        Log.d("locationLatLng",locationLatLng.toString())


        val restaurantList = restaurantRepository.getList(restaurantCategory,locationLatLng)

        restaurantListLiveData.value = restaurantList.map{
            RestaurantModel(
                id = it.id,
                restaurantInfoId = it.restaurantInfoId,
                restaurantCategory = it.restaurantCategory,
                restaurantTitle = it.restaurantTitle,
                restaurantImageUrl = it.restaurantImageUrl,
                grade = it.grade,
                reviewCount = it.reviewCount,
                deliveryTimeRange = it.deliveryTimeRange,
                deliveryTipRange = it.deliveryTipRange
            )
        }
    }

    fun setLocationLatLng(locationLatLng: LocationLatLngEntity) {
        this.locationLatLng = locationLatLng
        fetchData()


    }

}
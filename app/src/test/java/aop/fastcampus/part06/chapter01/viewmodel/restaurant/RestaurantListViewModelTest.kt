package aop.fastcampus.part06.chapter01.viewmodel.restaurant

import aop.fastcampus.part06.chapter01.data.entity.LocationLatLngEntity
import aop.fastcampus.part06.chapter01.data.repository.restaurant.RestaurantRepository
import aop.fastcampus.part06.chapter01.model.restaurant.RestaurantModel
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.RestaurantCategory
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.RestaurantListViewModel
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.RestaurantOrder
import aop.fastcampus.part06.chapter01.viewmodel.ViewModelTest
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

internal class RestaurantListViewModelTest: ViewModelTest() {

    /**
     * [RestaurantCategory]
     * [LocationLatLngEntity]
     */

    private var restaurantCategory = RestaurantCategory.ALL

    private var locationLatLngEntity : LocationLatLngEntity = LocationLatLngEntity(0.0,0.0)

    private val restaurantRepository by inject<RestaurantRepository>()

    private val restaurantListViewModel by inject<RestaurantListViewModel>{
        parametersOf(
            restaurantCategory,
            locationLatLngEntity
        )
    }

    @Test
    fun `test load restaurant list category ALL`(): Unit = runBlocking{
        val testObservable = restaurantListViewModel.restaurantListLiveData.test()

        restaurantListViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                restaurantRepository.getList(restaurantCategory, locationLatLngEntity).map{
                    RestaurantModel(
                        id = it.id,
                        restaurantInfoId = it.restaurantInfoId,
                        restaurantCategory = it.restaurantCategory,
                        restaurantTitle = it.restaurantTitle,
                        restaurantImageUrl = it.restaurantImageUrl,
                        grade = it.grade,
                        reviewCount = it.reviewCount,
                        deliveryTimeRange = it.deliveryTimeRange,
                        deliveryTipRange = it.deliveryTipRange,
                        restaurantTelNumber = it.restaurantTelNumber
                    )
                }

            )
        )

    }

    @Test
    fun `test load restaurant list category Excepted`(): Unit = runBlocking {
        restaurantCategory = RestaurantCategory.CAFE_DESSERT
        val testObservable = restaurantListViewModel.restaurantListLiveData.test()
        restaurantListViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                listOf()
            )
        )
    }

    @Test
    fun `test load restaurant list order by fast delivery time`(): Unit = runBlocking {

        val testObservable = restaurantListViewModel.restaurantListLiveData.test()

        restaurantListViewModel.setRestaurantOrder(RestaurantOrder.FAST_DELIVERY)

        testObservable.assertValueSequence(
            listOf(
                restaurantRepository.getList(restaurantCategory,locationLatLngEntity)
                    .sortedBy { it.deliveryTimeRange.first }
                    .map{RestaurantModel(
                        id = it.id,
                        restaurantInfoId = it.restaurantInfoId,
                        restaurantCategory = it.restaurantCategory,
                        restaurantTitle = it.restaurantTitle,
                        restaurantImageUrl = it.restaurantImageUrl,
                        grade = it.grade,
                        reviewCount = it.reviewCount,
                        deliveryTimeRange = it.deliveryTimeRange,
                        deliveryTipRange = it.deliveryTipRange,
                        restaurantTelNumber = it.restaurantTelNumber
                    )}
            )
        )


    }





}
package aop.fastcampus.part06.chapter01.screen.main.home.restaurant.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import aop.fastcampus.part06.chapter01.data.entity.RestaurantEntity
import aop.fastcampus.part06.chapter01.data.entity.RestaurantFoodEntity
import aop.fastcampus.part06.chapter01.data.repository.restaurant.food.RestaurantFoodRepository
import aop.fastcampus.part06.chapter01.data.repository.user.UserRepository
import aop.fastcampus.part06.chapter01.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantDetailViewModel(
    private val restaurantEntity: RestaurantEntity,
    private val restaurantFoodRepository: RestaurantFoodRepository,
    private val userRepository: UserRepository
):BaseViewModel() {

    val restaurantDetailStateLiveData = MutableLiveData<RestaurantDetailState>(RestaurantDetailState.Uninitialized)


    override fun fetchData(): Job = viewModelScope.launch{
        restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity
        )
        restaurantDetailStateLiveData.value = RestaurantDetailState.Loading
        val foods = restaurantFoodRepository.getFoods(
            restaurantId = restaurantEntity.restaurantInfoId,
            restaurantTitle= restaurantEntity.restaurantTitle
        )

        val foodMenuListInBasket = restaurantFoodRepository.getAllFoodMenuListInBasket()

        val isLiked = userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle) != null
        restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity,
            restaurantFoodList = foods,
            foodMenuListInBasket = foodMenuListInBasket,
            isLiked = isLiked
        )



    }

    fun getRestaurantTelNumber(): String? {
        return when(val data = restaurantDetailStateLiveData.value){
            is RestaurantDetailState.Success->{
                data.restaurantEntity.restaurantTelNumber
            }
            else -> null
        }

    }

    fun getRestaurantInfo(): RestaurantEntity?{
        return when(val data = restaurantDetailStateLiveData.value){
            is RestaurantDetailState.Success->{
                data.restaurantEntity
            }
            else -> null
        }
    }

    fun toggleLikedRestaurant() = viewModelScope.launch{
        when(val data = restaurantDetailStateLiveData.value){
            is RestaurantDetailState.Success->{
                userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle)?.let{
                    userRepository.deleteUserLikedRestaurant(it.restaurantTitle)
                    restaurantDetailStateLiveData.value = data.copy(
                        isLiked = false
                    )
                }?: kotlin.run{
                    userRepository.insertUserLikedRestaurant(restaurantEntity)
                    restaurantDetailStateLiveData.value = data.copy(
                        isLiked = true
                    )
                }
            }
            else->{}
        }


    }

    fun notifyFoodMenuListInBasket(foodMenu: RestaurantFoodEntity) = viewModelScope.launch{

        when(val data = restaurantDetailStateLiveData.value){
            is RestaurantDetailState.Success->{
                restaurantDetailStateLiveData.value = data.copy(
                    foodMenuListInBasket = data.foodMenuListInBasket?.toMutableList()?.apply{
                        add(foodMenu)
                    }
                )
            }
            else -> Unit
        }
    }

    fun notifyClearNeedAlertInBasket(clearNeed: Boolean, afterAction: () -> Unit) {
        when(val data = restaurantDetailStateLiveData.value){
            is RestaurantDetailState.Success->{
                restaurantDetailStateLiveData.value = data.copy(
                    isClearNeedInBasketAndAction = Pair(clearNeed,afterAction)

                )
            }
            else -> Unit
        }
    }

    fun notifyClearBasket() = viewModelScope.launch{
        when(val data = restaurantDetailStateLiveData.value){
            is RestaurantDetailState.Success->{
                restaurantDetailStateLiveData.value = data.copy(
                    foodMenuListInBasket = listOf(),
                    isClearNeedInBasketAndAction = Pair(false,{})

                )
            }
            else -> Unit
        }
    }
}
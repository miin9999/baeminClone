package aop.fastcampus.part06.chapter01.screen.main.home.restaurant.detail.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import aop.fastcampus.part06.chapter01.data.entity.RestaurantFoodEntity
import aop.fastcampus.part06.chapter01.data.repository.restaurant.food.RestaurantFoodRepository
import aop.fastcampus.part06.chapter01.model.restaurant.food.FoodModel
import aop.fastcampus.part06.chapter01.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantMenuListViewModel(
    private val restaurantId: Long,
    private val foodEntityList: List<RestaurantFoodEntity>,
    private val restaurantFoodRepository: RestaurantFoodRepository
):BaseViewModel(){

    val restaurantFoodListLiveData = MutableLiveData<List<FoodModel>>()

    val menuBasketLiveData = MutableLiveData<RestaurantFoodEntity>()

    val isClearNeedInBasketLiveData = MutableLiveData<Pair<Boolean,()->Unit>>()

    override fun fetchData(): Job = viewModelScope.launch{
        restaurantFoodListLiveData.value = foodEntityList.map{
            FoodModel(
                id = it.hashCode().toLong(),
                title = it.title,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl,
                restaurantId = restaurantId,
                foodId = it.id,
                restaurantTitle = it.restaurantTitle

            )
        }
    }

    fun insertMenuInBasket(foodModel: FoodModel) = viewModelScope.launch {
        // 현재 식당에서 사용되고 있는 장바구니 음식리스트
        val restaurantMenuListInBasket = restaurantFoodRepository.getFoodMenuListInBasket(restaurantId)
        val foodMenuEntity = foodModel.toEntity(restaurantMenuListInBasket.size)
        val anotherRestaurantMenuListInBasket =
            restaurantFoodRepository.getAllFoodMenuListInBasket().filter{it.restaurantId != restaurantId}
        if(anotherRestaurantMenuListInBasket.isNotEmpty()){
            // 다른 식당의 메뉴가 들어가 있다면 지울 거임->
            isClearNeedInBasketLiveData.value = Pair(true,{ clearMenuAndInsertNewMenuInBasket(foodMenuEntity)})
        } else{
            restaurantFoodRepository.insertFoodMenuInBasket(foodMenuEntity)
            menuBasketLiveData.value = foodMenuEntity
        }
    }

    private fun clearMenuAndInsertNewMenuInBasket(foodMenuEntity: RestaurantFoodEntity) = viewModelScope.launch {

        restaurantFoodRepository.clearFoodMenuListInBasket()
        restaurantFoodRepository.insertFoodMenuInBasket(foodMenuEntity)
        menuBasketLiveData.value = foodMenuEntity


    }

}
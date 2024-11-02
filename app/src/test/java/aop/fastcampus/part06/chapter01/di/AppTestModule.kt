package aop.fastcampus.part06.chapter01.di

import aop.fastcampus.part06.chapter01.data.TestOrderRepository
import aop.fastcampus.part06.chapter01.data.TestRestaurantFoodRepository
import aop.fastcampus.part06.chapter01.data.TestRestaurantRepository
import aop.fastcampus.part06.chapter01.data.entity.LocationLatLngEntity
import aop.fastcampus.part06.chapter01.data.repository.order.OrderRepository
import aop.fastcampus.part06.chapter01.data.repository.restaurant.RestaurantRepository
import aop.fastcampus.part06.chapter01.data.repository.restaurant.food.RestaurantFoodRepository
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.RestaurantCategory
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.RestaurantListViewModel
import aop.fastcampus.part06.chapter01.screen.order.OrderMenuListViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module{
    viewModel{
        (restaurantCategory: RestaurantCategory,locationLatLng: LocationLatLngEntity)->
        RestaurantListViewModel(restaurantCategory,locationLatLng,get())
    }

    viewModel { (firebaseAuth: FirebaseAuth)-> OrderMenuListViewModel(get(),get(),firebaseAuth) }

    single<RestaurantRepository>{TestRestaurantRepository()}

    single<RestaurantFoodRepository>{TestRestaurantFoodRepository()}

    single<OrderRepository>{TestOrderRepository()}




}
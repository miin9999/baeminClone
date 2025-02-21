package aop.fastcampus.part06.chapter01.di

import aop.fastcampus.part06.chapter01.Part6Chapter01Application
import aop.fastcampus.part06.chapter01.data.entity.LocationLatLngEntity
import aop.fastcampus.part06.chapter01.data.entity.MapSearchInfoEntity
import aop.fastcampus.part06.chapter01.data.entity.RestaurantEntity
import aop.fastcampus.part06.chapter01.data.entity.RestaurantFoodEntity
import aop.fastcampus.part06.chapter01.data.preference.AppPreferenceManager
import aop.fastcampus.part06.chapter01.data.repository.map.DefaultMapRepository
import aop.fastcampus.part06.chapter01.data.repository.map.MapRepository
import aop.fastcampus.part06.chapter01.data.repository.order.DefaultOrderRepository
import aop.fastcampus.part06.chapter01.data.repository.order.OrderRepository
import aop.fastcampus.part06.chapter01.data.repository.restaurant.DefaultRestaurantRepository
import aop.fastcampus.part06.chapter01.data.repository.restaurant.RestaurantRepository
import aop.fastcampus.part06.chapter01.data.repository.restaurant.food.DefaultRestaurantFoodRepository
import aop.fastcampus.part06.chapter01.data.repository.restaurant.food.RestaurantFoodRepository
import aop.fastcampus.part06.chapter01.data.repository.restaurant.review.DefaultRestaurantReviewRepository
import aop.fastcampus.part06.chapter01.data.repository.restaurant.review.RestaurantReviewRepository
import aop.fastcampus.part06.chapter01.data.repository.user.DefaultUserRepository
import aop.fastcampus.part06.chapter01.data.repository.user.UserRepository
import aop.fastcampus.part06.chapter01.screen.main.home.HomeViewModel
import aop.fastcampus.part06.chapter01.screen.main.my.MyViewModel
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.RestaurantCategory
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.RestaurantListViewModel
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.detail.menu.RestaurantMenuListViewModel
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.detail.review.RestaurantReviewListViewModel
import aop.fastcampus.part06.chapter01.screen.main.like.RestaurantLikeListViewModel
import aop.fastcampus.part06.chapter01.screen.mylocation.MyLocationViewModel
import aop.fastcampus.part06.chapter01.screen.order.OrderMenuListViewModel
import aop.fastcampus.part06.chapter01.util.event.MenuChangeEventBus
import aop.fastcampus.part06.chapter01.util.provider.DefaultResourcesProvider
import aop.fastcampus.part06.chapter01.util.provider.ResourcesProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val appModule = module {


    viewModel{HomeViewModel(get(),get(),get())}
    viewModel{ MyViewModel(get(),get(),get()) }
    viewModel{(restaurantCategory:RestaurantCategory,locationLatLng:LocationLatLngEntity)->
        RestaurantListViewModel(restaurantCategory,locationLatLng,get())}


    viewModel{(mapSearchInfoEntity:MapSearchInfoEntity) -> MyLocationViewModel(mapSearchInfoEntity,get(),get())}
    viewModel{(restaurantEntity: RestaurantEntity) -> RestaurantDetailViewModel(restaurantEntity,get(),get())}
    viewModel{ (restaurantId: Long, restaurantFoodList: List<RestaurantFoodEntity>)->
        RestaurantMenuListViewModel(restaurantId,restaurantFoodList,get()) }

    viewModel{ (restaurantTitle: String) -> RestaurantReviewListViewModel(restaurantTitle,get()) }
    viewModel{ (firebaseAuth: FirebaseAuth)->OrderMenuListViewModel(get(),get(),firebaseAuth)}

    viewModel{ RestaurantLikeListViewModel(get())}



    single<RestaurantRepository>{ DefaultRestaurantRepository(get(),get(),get()) }
    single<MapRepository>{DefaultMapRepository(get(),get())}
    single<UserRepository>{DefaultUserRepository(get(),get(),get())}
    single<RestaurantFoodRepository>{DefaultRestaurantFoodRepository(get(),get(),get())}
    single<RestaurantReviewRepository>{DefaultRestaurantReviewRepository(get(),get())}
    single<OrderRepository>{ DefaultOrderRepository(get(),get())}



    single{ provideGsonConvertFactory() }
    single{ buildOkHttpClient() }

    single(named("map")){ provideMapRetrofit(get(),get()) }
    single(named("food")){ provideFoodRetrofit(get(),get()) }



    single{ provideMapApiService(get(qualifier=named("map"))) }
    single{ provideFoodApiService(get(qualifier= named("food")))}


    single{ (Part6Chapter01Application.appContext?.let{
        provideDB(it)
    }?:throw IllegalStateException("Application context is not available")) }

    single{ provideLocationDao(get()) }
    single{ provideRestaurantDao(get()) }
    single{ provideFoodMenuBasketDao(get())}


    single<ResourcesProvider> {
        Part6Chapter01Application.appContext?.let { DefaultResourcesProvider(it) }
            ?: throw IllegalStateException("Application context is not available")
    }

    single {
        Part6Chapter01Application.appContext?.let { AppPreferenceManager(it) }
            ?: throw IllegalStateException("Application context is not available")
    }

    single{Dispatchers.IO}
    single{Dispatchers.Main}

    single{ MenuChangeEventBus() }

    single{ Firebase.firestore}
    single{ FirebaseAuth.getInstance()}
    single{ FirebaseStorage.getInstance() }
}
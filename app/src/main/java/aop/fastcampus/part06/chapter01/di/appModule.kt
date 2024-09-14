package aop.fastcampus.part06.chapter01.di

import aop.fastcampus.part06.chapter01.Part6Chapter01Application
import aop.fastcampus.part06.chapter01.data.entity.LocationLatLngEntity
import aop.fastcampus.part06.chapter01.data.entity.MapSearchInfoEntity
import aop.fastcampus.part06.chapter01.data.repository.map.DefaultMapRepository
import aop.fastcampus.part06.chapter01.data.repository.map.MapRepository
import aop.fastcampus.part06.chapter01.data.repository.restaurant.DefaultRestaurantRepository
import aop.fastcampus.part06.chapter01.data.repository.restaurant.RestaurantRepository
import aop.fastcampus.part06.chapter01.data.repository.user.DefaultUserRepository
import aop.fastcampus.part06.chapter01.data.repository.user.UserRepository
import aop.fastcampus.part06.chapter01.screen.main.home.HomeViewModel
import aop.fastcampus.part06.chapter01.screen.main.home.MyViewModel
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.RestaurantCategory
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.RestaurantListViewModel
import aop.fastcampus.part06.chapter01.screen.mylocation.MyLocationViewModel
import aop.fastcampus.part06.chapter01.util.provider.DefaultResourcesProvider
import aop.fastcampus.part06.chapter01.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {


    viewModel{HomeViewModel(get(),get())}
    viewModel{MyViewModel()}
    viewModel{(restaurantCategory:RestaurantCategory,locationLatLng:LocationLatLngEntity)->
        RestaurantListViewModel(restaurantCategory,locationLatLng,get())}
    viewModel{(mapSearchInfoEntity:MapSearchInfoEntity) -> MyLocationViewModel(mapSearchInfoEntity,get(),get())}


    single<RestaurantRepository>{ DefaultRestaurantRepository(get(),get(),get()) }
    single<MapRepository>{DefaultMapRepository(get(),get())}
    single<UserRepository>{DefaultUserRepository(get(),get())}

    single{ provideGsonConvertFactory() }
    single{ buildOkHttpClient() }

    single{ provideMapRetrofit(get(),get()) }
    single{ provideMapApiService(get()) }

    single{ (Part6Chapter01Application.appContext?.let{
        provideDB(it)
    }?:throw IllegalStateException("Application context is not available")) }

    single{ provideLocationDao(get()) }

    single<ResourcesProvider> {
        Part6Chapter01Application.appContext?.let { DefaultResourcesProvider(it) }
            ?: throw IllegalStateException("Application context is not available")
    }

    single{Dispatchers.IO}
    single{Dispatchers.Main}
}
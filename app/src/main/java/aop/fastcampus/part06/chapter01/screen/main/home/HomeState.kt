package aop.fastcampus.part06.chapter01.screen.main.home

import androidx.annotation.StringRes
import aop.fastcampus.part06.chapter01.data.entity.LocationLatLngEntity
import aop.fastcampus.part06.chapter01.data.entity.MapSearchInfoEntity

sealed class HomeState {

    object Uninitialized: HomeState()

    object Loading: HomeState()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity,
        val isLocationSame: Boolean

    ): HomeState()

    data class Error(
        @StringRes val messageId: Int
    ): HomeState()

}

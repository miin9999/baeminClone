package aop.fastcampus.part06.chapter01.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MapSearchInfoEntity (
    val fullAddress: String,
    val name: String,
    val locationLatLng: LocationLatLngEntity,
): Parcelable
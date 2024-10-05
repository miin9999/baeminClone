package aop.fastcampus.part06.chapter01.data.entity

import android.os.Parcelable
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.RestaurantCategory
import aop.fastcampus.part06.chapter01.util.converter.RoomTypeConverters
import kotlinx.parcelize.Parcelize


@Parcelize
@androidx.room.Entity
@TypeConverters(RoomTypeConverters::class)
data class RestaurantEntity (
    override val id: Long,
    val restaurantInfoId: Long,
    val restaurantCategory:RestaurantCategory,
    @PrimaryKey val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange:Pair<Int,Int>,
    val deliveryTipRange:Pair<Int,Int>,
    val restaurantTelNumber: String?
):Entity, Parcelable
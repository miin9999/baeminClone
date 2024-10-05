package aop.fastcampus.part06.chapter01.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import aop.fastcampus.part06.chapter01.data.db.dao.FoodMenuBasketDao
import aop.fastcampus.part06.chapter01.data.db.dao.LocationDao
import aop.fastcampus.part06.chapter01.data.db.dao.RestaurantDao
import aop.fastcampus.part06.chapter01.data.entity.LocationLatLngEntity
import aop.fastcampus.part06.chapter01.data.entity.RestaurantEntity
import aop.fastcampus.part06.chapter01.data.entity.RestaurantFoodEntity


@Database(
    entities =[LocationLatLngEntity::class,RestaurantEntity::class, RestaurantFoodEntity::class],
    version = 3,
    exportSchema = false
)
abstract class ApplicationDatabase: RoomDatabase(){

    companion object{
        const val DB_NAME = "ApplicationDataBase.db"

    }

    // 이 함수를 직접적으로 오버라이드한다기보다는 이걸 이용해서 koin으로 넣는 것임
    abstract fun LocationDao():LocationDao

    abstract fun RestaurantDao():RestaurantDao

    abstract fun FoodMenuBasketDao():FoodMenuBasketDao


}
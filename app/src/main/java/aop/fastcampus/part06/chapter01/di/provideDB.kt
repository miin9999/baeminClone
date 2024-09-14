package aop.fastcampus.part06.chapter01.di

import android.content.Context
import androidx.room.Room
import aop.fastcampus.part06.chapter01.data.db.ApplicationDatabase
import aop.fastcampus.part06.chapter01.data.db.dao.LocationDao

fun provideDB(context: Context):ApplicationDatabase =
        Room.databaseBuilder(context, ApplicationDatabase::class.java,ApplicationDatabase.DB_NAME).build()

fun provideLocationDao(database: ApplicationDatabase) = database.LocationDao()
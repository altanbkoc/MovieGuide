package com.altankoc.themoviesapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.altankoc.themoviesapp.data.local.dao.MovieDao
import com.altankoc.themoviesapp.data.local.entity.MovieEntity


@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}
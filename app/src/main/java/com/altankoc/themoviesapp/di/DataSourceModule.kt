package com.altankoc.themoviesapp.di

import com.altankoc.themoviesapp.data.datasource.localdatasource.LocalDataSource
import com.altankoc.themoviesapp.data.datasource.localdatasource.LocalDataSourceImpl
import com.altankoc.themoviesapp.data.datasource.remotedatasource.RemoteDataSource
import com.altankoc.themoviesapp.data.datasource.remotedatasource.RemoteDataSourceImpl
import com.altankoc.themoviesapp.data.local.dao.MovieDao
import com.altankoc.themoviesapp.data.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {


    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource {
        return RemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(movieDao: MovieDao): LocalDataSource {
        return LocalDataSourceImpl(movieDao)
    }

}
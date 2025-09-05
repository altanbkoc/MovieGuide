package com.altankoc.themoviesapp.di

import com.altankoc.themoviesapp.data.datasource.localdatasource.LocalDataSource
import com.altankoc.themoviesapp.data.datasource.remotedatasource.RemoteDataSource
import com.altankoc.themoviesapp.data.repository.MovieRepositoryImpl
import com.altankoc.themoviesapp.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Provides
    @Singleton
    fun provideMovieRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): MovieRepository = MovieRepositoryImpl(
        remoteDataSource = remoteDataSource,
        localDataSource = localDataSource
    )
}
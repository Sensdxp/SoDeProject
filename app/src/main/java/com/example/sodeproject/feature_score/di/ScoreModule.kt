package com.example.sodeproject.feature_score.di


import com.example.sodeproject.feature_score.data.ScoreRepository
import com.example.sodeproject.feature_score.data.ScoreRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ScoreModule {

    @Provides
    @Singleton
    fun providesScoreRepositoryImpl(): ScoreRepository{
        return ScoreRepositoryImpl()
    }
}

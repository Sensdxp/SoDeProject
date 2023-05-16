package com.example.sodeproject.feature_shop.di

import com.example.sodeproject.feature_shop.data.ShopRepository
import com.example.sodeproject.feature_shop.data.ShopRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ShopModule {
    @Provides
    @Singleton
    fun providesShopRepositoryImpl(): ShopRepository{
        return ShopRepositoryImpl()
    }
}
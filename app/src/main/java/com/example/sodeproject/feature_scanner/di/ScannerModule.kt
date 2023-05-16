package com.example.sodeproject.feature_scanner.di

import com.example.sodeproject.feature_scanner.data.ScannerRepository
import com.example.sodeproject.feature_scanner.data.ScannerRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ScannerModule {
    @Provides
    @Singleton
    fun providesScannerRepositoryImpl(): ScannerRepository{
        return ScannerRepositoryImpl()
    }
}
/*
@Provides
    @Singleton
    fun providesShopRepositoryImpl(): ShopRepository{
        return ShopRepositoryImpl()
    }
 */
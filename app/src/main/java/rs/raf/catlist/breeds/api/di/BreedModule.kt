package rs.raf.catlist.breeds.api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import rs.raf.catlist.breeds.api.BreedApi
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BreedModule {
    @Provides
    @Singleton
    fun provideBreedsApi(@Named("breed") retrofit: Retrofit): BreedApi = retrofit.create()
}
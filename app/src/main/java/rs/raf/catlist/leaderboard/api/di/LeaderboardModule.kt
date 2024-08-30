package rs.raf.catlist.leaderboard.api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import rs.raf.catlist.leaderboard.api.LeaderboardApi
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LeaderboardModule {

    @Provides
    @Singleton
    fun provideLeaderBoardApi(@Named("leaderBoard") retrofit: Retrofit): LeaderboardApi = retrofit.create()
}
package com.mezhendosina.sgo.di

import android.content.Context
import androidx.room.Room
import com.mezhendosina.sgo.data.emoji.AppDatabase
import com.mezhendosina.sgo.data.emoji.EmojiDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room
            .databaseBuilder(context, AppDatabase::class.java, "app-database")
            .build()

    @Singleton
    @Provides
    fun provideEmojiDao(appDatabase: AppDatabase): EmojiDao = appDatabase.getEmoji()
}

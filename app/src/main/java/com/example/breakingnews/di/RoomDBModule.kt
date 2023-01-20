package com.example.breakingnews.di

import android.content.Context
import androidx.room.Room
import com.example.breakingnews.data.local.ArticleDao
import com.example.breakingnews.data.local.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomDBModule {
    @Provides
    @Singleton
    fun provideLocalDatabase(
        @ApplicationContext context: Context,
    ): ArticleDatabase =
        Room.databaseBuilder(
            context,
            ArticleDatabase::class.java,
            "article_database"
        ).allowMainThreadQueries().build()

    @Provides
    @Singleton
    fun provideNoteDao(articleDatabase: ArticleDatabase): ArticleDao = articleDatabase.articleDao()
}
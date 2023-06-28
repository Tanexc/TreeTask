package ru.tanexc.tree.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.tanexc.tree.data.local.database.MainDatabase
import ru.tanexc.tree.data.repository.NodeRepositoryImpl
import ru.tanexc.tree.domain.repository.NodeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context,
    ): MainDatabase = Room.databaseBuilder(
        context = appContext,
        klass = MainDatabase::class.java,
        name = "TreeDatabase",
    )   .createFromAsset("database/main.db")
        .build()

    @Provides
    @Singleton
    fun provideNodeRepository(
        db: MainDatabase
    ): NodeRepository = NodeRepositoryImpl(db.nodeDao)

}
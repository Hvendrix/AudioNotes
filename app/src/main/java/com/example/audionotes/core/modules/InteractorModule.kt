package com.example.audionotes.core.modules

import com.example.audionotes.core.domain.interactors.MainInteractor
import com.example.audionotes.core.domain.repository.NotesRepository
import com.example.audionotes.home.domain.interactors.HomeInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object InteractorModule {

    @Provides
    @Singleton
    fun provideHomeInteractor(
        notesRepository: NotesRepository
    ): HomeInteractor = HomeInteractor(notesRepository)


    @Provides
    @Singleton
    fun provideMainInteractor(
        notesRepository: NotesRepository
    ): MainInteractor = MainInteractor(notesRepository)
}
package com.example.audionotes.core.modules

import android.content.Context
import androidx.room.Room
import com.example.audionotes.core.data.database.AppDatabase
import com.example.audionotes.core.data.database.NotesDao
import com.example.audionotes.core.data.repository.NoteLocalDataSource
import com.example.audionotes.core.data.repository.NoteLocalDataSourceImpl
import com.example.audionotes.core.data.repository.NotesRepositoryImpl
import com.example.audionotes.core.domain.repository.NotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: AppDatabase) = database.notesDao()

    private const val DATABASE_NAME = "audio_notes_app"


    @Singleton
    @Provides
    fun provideNoteLocalDataSource(
        notesDao: NotesDao
    ): NoteLocalDataSource = NoteLocalDataSourceImpl(notesDao)

    @Singleton
    @Provides
    fun provideNotesRepository(
        noteLocalDataSource: NoteLocalDataSource
    ) : NotesRepository = NotesRepositoryImpl(noteLocalDataSource)
}


package com.pawlowski.imageupload.di

import com.pawlowski.imageupload.FirebaseStoragePhotoUploader
import com.pawlowski.imageupload.IPhotoUploader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageUploadModule {
    @Singleton
    @Provides
    internal fun photoUploader(firebaseStoragePhotoUploader: FirebaseStoragePhotoUploader): IPhotoUploader = firebaseStoragePhotoUploader
}
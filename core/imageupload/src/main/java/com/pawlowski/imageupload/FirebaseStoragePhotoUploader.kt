package com.pawlowski.imageupload

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.google.firebase.storage.FirebaseStorage
import com.pawlowski.utils.Resource
import com.pawlowski.utils.UiText
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject



internal class FirebaseStoragePhotoUploader @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val contentResolver: ContentResolver,
) : IPhotoUploader {
    override suspend fun uploadNewImage(uri: Uri, userUid: String): Resource<String>
    {
        return try {
            val reducedImage = reduceImageSize(uri)
            val result = firebaseStorage.reference.child("profile_images").child(userUid).child(UUID.randomUUID().toString()).putBytes(reducedImage).await()
            val downloadUrl = result.storage.downloadUrl.await()
            Resource.Success(downloadUrl.toString())
        }
        catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(UiText.NonTranslatable(e.localizedMessage?:"Error with uploading image"))
        }
    }

    private fun reduceImageSize(uri: Uri): ByteArray
    {
        val bitmap = getBitmapFromUri(uri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    private fun getBitmapFromUri(selectedPhotoUri: Uri): Bitmap {
        return when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                contentResolver,
                selectedPhotoUri
            )
            else -> {
                val source = ImageDecoder.createSource(contentResolver, selectedPhotoUri)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }
}
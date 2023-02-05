package com.pawlowski.imageupload

import android.net.Uri
import com.pawlowski.utils.Resource

interface IPhotoUploader {
    suspend fun uploadNewImage(uri: Uri, userUid: String): Resource<String>
}
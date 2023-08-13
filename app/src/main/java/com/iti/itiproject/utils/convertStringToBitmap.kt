package com.iti.itiproject.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

fun convertStringToBitmap(encodedImage: String): Bitmap? {
    val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

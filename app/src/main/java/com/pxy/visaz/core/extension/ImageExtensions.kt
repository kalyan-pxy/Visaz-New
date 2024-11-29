package com.pxy.visaz.core.extension

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.pxy.visaz.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

fun ImageView.loadImage(url: String?) {
    Glide.with(context).load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(R.drawable.image1)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun ImageView.loadImageFromFilePath(filePath: String) {
    try {
        // Convert file path to Bitmap
        val bitmap = BitmapFactory.decodeFile(filePath)
        if (bitmap == null) {
            loadDefaultImage()
            return
        }
        // Set the bitmap to the ImageView
        setImageBitmap(bitmap)
    } catch (_: Exception) {
        loadDefaultImage()
    }
}

fun ImageView.loadDefaultImage() {
    setImageResource(R.drawable.ic_image_placeholder)
}

fun copyFileToAppStorage(context: Context, sourcePath: String, fileName: String): String? {
    try {
        val inputStream = FileInputStream(File(sourcePath))
        val outputFile = File(context.filesDir, "${fileName}.jpg") // Save in internal storage
        val outputStream = FileOutputStream(outputFile)

        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }

        inputStream.close()
        outputStream.close()

        return outputFile.path
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

fun String.toBase64(): String? {
    return try {
        val file = File(this)
        val bytes = file.readBytes() // Read the file as a byte array
        Base64.encodeToString(bytes, Base64.DEFAULT) // Convert to Base64 string
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun String.base64ToImage(imageView: ImageView) {
    try {
        // Decode Base64 string to byte array
        val decodedBytes = Base64.decode(this, Base64.DEFAULT)

        // Convert byte array to Bitmap
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

        // Set the Bitmap to ImageView
        imageView.setImageBitmap(bitmap)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun ImageView.setImageUri(uri: String?) {
    if (uri == null) {
        setImageResource(R.drawable.ic_image_placeholder)
    } else {
        loadImageFromFilePath(uri)
    }
}
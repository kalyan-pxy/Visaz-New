package com.pxy.visaz.core.extension

import android.content.Context
import android.graphics.BitmapFactory
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
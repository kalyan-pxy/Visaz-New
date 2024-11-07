package com.pxy.visaz.core.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.pxy.visaz.R

fun ImageView.loadImage(url: String?) {
    Glide.with(context).load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(R.drawable.image1)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}
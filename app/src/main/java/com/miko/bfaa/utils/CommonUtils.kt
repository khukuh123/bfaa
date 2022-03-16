package com.miko.bfaa.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ImageView.setImageFromString(image: String) {
    val resId = with(this.context) {
        resources.getIdentifier(image, "drawable", packageName)
    }
    Glide.with(this).load(ContextCompat.getDrawable(this.context, resId)).into(this)
}

fun Int.formatShorter(): String {
    val result: Float = this.toFloat() / 1000.toFloat()
    val decimalFormat = DecimalFormat("#.#").apply {
        roundingMode = RoundingMode.FLOOR
        decimalFormatSymbols = DecimalFormatSymbols(Locale.getDefault()).apply {
            decimalSeparator = '.'
        }
    }
    return when {
        result >= 1.0 -> {
            "${decimalFormat.format(result)}K"
        }
        else -> {
            "$this"
        }
    }
}
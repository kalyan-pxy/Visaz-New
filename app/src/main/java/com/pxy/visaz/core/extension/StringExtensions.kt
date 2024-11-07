package com.pxy.visaz.core.extension

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.TypefaceSpan
import android.view.View
import android.widget.TextView
import java.text.NumberFormat
import java.util.Locale


fun TextView.highlightTextMatches(
    fullText: String,
    searchText: String,
    typeface: Typeface? = null,
    function: () -> Unit
) {
    val spannableText = SpannableString(fullText)
    val startIndex = spannableText.indexOf(searchText, ignoreCase = true)
    if (startIndex >= 0) {
        val endIndex = startIndex + searchText.length
        if (endIndex <= spannableText.length) {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    function.invoke()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false // Disable underline for clickable span
                    // Set the custom text color
                    ds.typeface = typeface     // Set the custom typeface
                }
            }
            spannableText.setSpan(
                clickableSpan,
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            typeface?.let {
                spannableText.setSpan(
                    TypefaceSpan(it),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }
    text = spannableText
    movementMethod = LinkMovementMethod.getInstance()
}

fun Double.formatPrice(): String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    return numberFormat.format(this)
}

@file:Suppress("NOTHING_TO_INLINE")

package co.sodalabs.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable

inline fun Drawable.determineSelfCenterBound() {
    val hw = intrinsicWidth.toFloat() / 2f
    val hh = intrinsicHeight.toFloat() / 2f
    this.setBounds(-(hw.toFloorInt()), -(hh.toFloorInt()), hw.toCeilInt(), hh.toCeilInt())
}

inline fun Drawable.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(
        this.bounds.width(), this.bounds.height(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    this.draw(canvas)
    return bitmap
}
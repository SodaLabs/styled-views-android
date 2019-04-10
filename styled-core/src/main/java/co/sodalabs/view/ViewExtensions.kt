@file:Suppress("NOTHING_TO_INLINE")

package co.sodalabs.view

import android.support.v4.view.ViewCompat
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.AbsSeekBar
import android.widget.ProgressBar
import android.widget.SeekBar

/**
 * A backport for accessing hidden function "isInScrollingContainerCompat".
 */
inline fun View.isInScrollingContainerCompat(): Boolean {
    return try {
        val clazz = Class.forName("android.view.View")
        val method = clazz.getDeclaredMethod("isInScrollingContainer")
        method.isAccessible = true
        method.invoke(this) as? Boolean ?: false
    } catch (err: Throwable) {
        var p: ViewParent? = parent
        while (p != null && p is ViewGroup) {
            if (p.shouldDelayChildPressedState()) {
                return true
            }
            p = p.parent
        }
        false
    }
}

inline fun View.isLayoutRtl(): Boolean {
    return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
}

/**
 * A backport for accessing hidden function "onStartTrackingTouch" which dispatches
 * the track callback.
 */
inline fun SeekBar.onStartTrackingTouch() {
    try {
        val clazz = Class.forName("android.widget.SeekBar")
        val method = clazz.getDeclaredMethod("onStartTrackingTouch")
        method.isAccessible = true
        method.invoke(this)
    } catch (err: Throwable) {
        err.printStackTrace()
    }
}

/**
 * A backport for accessing hidden function "onStopTrackingTouch" which dispatches
 * the track callback.
 */
inline fun SeekBar.onStopTrackingTouch() {
    try {
        val clazz = Class.forName("android.widget.SeekBar")
        val method = clazz.getDeclaredMethod("onStopTrackingTouch")
        method.isAccessible = true
        method.invoke(this)
    } catch (ignored: Throwable) {
        // No-op
    }
}

/**
 * A backport for accessing hidden field "mIsDragging" of a [SeekBar].
 */
inline fun AbsSeekBar.isDragging(): Boolean {
    return try {
        val clazz = Class.forName("android.widget.AbsSeekBar")
        val value = clazz.getDeclaredField("mIsDragging")
        value.isAccessible = true
        value.getBoolean(this)
    } catch (ignored: Throwable) {
        false
    }
}

/**
 * A backport for accessing hidden field "mMirrorForRtl" of a [SeekBar].
 */
inline fun ProgressBar.isMirrorForRtl(): Boolean {
    return try {
        val clazz = Class.forName("android.widget.ProgressBar")
        val value = clazz.getDeclaredField("mMirrorForRtl")
        value.isAccessible = true
        value.getBoolean(this)
    } catch (ignored: Throwable) {
        false
    }
}
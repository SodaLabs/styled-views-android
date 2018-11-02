@file:Suppress("NOTHING_TO_INLINE")

package co.sodalabs.view

inline fun Float.toFloorInt(): Int {
    return Math.floor(this.toDouble()).toInt()
}

inline fun Float.toCeilInt(): Int {
    return Math.ceil(this.toDouble()).toInt()
}

inline fun Float.xOneByte(): Int {
    return (this * 255).toInt()
}
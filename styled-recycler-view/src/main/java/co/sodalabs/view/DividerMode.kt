package co.sodalabs.view

import android.support.annotation.IntDef

object DividerMode {

    @IntDef(flag = true, value = [SHOW_DIVIDER_NONE, SHOW_DIVIDER_BEGINNING, SHOW_DIVIDER_MIDDLE, SHOW_DIVIDER_END])
    @Retention(AnnotationRetention.SOURCE)
    annotation class Type

    /**
     * Don't show any dividers.
     */
    const val SHOW_DIVIDER_NONE = 0
    /**
     * Show a divider at the beginning of the group.
     */
    const val SHOW_DIVIDER_BEGINNING = 1
    /**
     * Show dividers between each item in the group.
     */
    const val SHOW_DIVIDER_MIDDLE = 2
    /**
     * Show a divider at the end of the group.
     */
    const val SHOW_DIVIDER_END = 4
}
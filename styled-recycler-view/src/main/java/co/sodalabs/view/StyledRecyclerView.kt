package co.sodalabs.view

import android.content.Context
import android.content.res.TypedArray
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import co.sodalabs.R

/**
 * A custom [RecyclerView] supporting the style attribute [R.styleable.StyledRecyclerView].
 *
 * @see [R.attr.rvDividerDrawable] The drawable reference.
 * @see [R.attr.rvDividerMode] There are [DividerMode.SHOW_DIVIDER_BEGINNING],
 * [DividerMode.SHOW_DIVIDER_MIDDLE], [DividerMode.SHOW_DIVIDER_END] and [DividerMode.SHOW_DIVIDER_NONE].
 */
class StyledRecyclerView : RecyclerView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.StyledRecyclerView, 0, 0)
        decorateView(a, getOrientation(attrs))
        a.recycle()
    }

    private fun decorateView(
        typedArray: TypedArray,
        orientation: Int
    ) {
        // Divider and the mode
        if (typedArray.hasValue(R.styleable.StyledRecyclerView_rvDividerDrawable)) {
            val dividerDrawable = typedArray.getDrawable(R.styleable.StyledRecyclerView_rvDividerDrawable)
            val dividerMode = typedArray.getInt(
                R.styleable.StyledRecyclerView_rvDividerMode,
                DividerMode.SHOW_DIVIDER_BEGINNING.or(
                    DividerMode.SHOW_DIVIDER_MIDDLE).or(
                    DividerMode.SHOW_DIVIDER_END))

            val decoration = DrawableDividerDecoration(
                dividerDrawable = dividerDrawable,
                dividerMode = dividerMode)
            addItemDecoration(decoration)
        }

        // Preview list item
        if (isInEditMode) {
            layoutManager = LinearLayoutManager(context, orientation, false)
        }
    }

    private fun getOrientation(attrs: AttributeSet?): Int {
        val b = context.theme.obtainStyledAttributes(
            attrs,
            intArrayOf(android.R.attr.orientation), 0, 0)
        val orientation = b.getInt(0, RecyclerView.VERTICAL)
        b.recycle()
        return orientation
    }
}
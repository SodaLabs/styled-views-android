package co.sodalabs.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatSeekBar
import android.util.AttributeSet
import android.view.MotionEvent
import co.sodalabs.view.slider.R
import kotlin.math.roundToInt

/**
 * The base slider view
 *
 * @see [R.attr.slThumbDrawable] The thumb drawable.
 * @see [R.attr.slTrackBackgroundDrawable] The track (a.k.a progress, but only the background part) drawable.
 * @see [R.attr.slMarkerDrawableMiddle] The marker (tick) drawable in the middle.
 * @see [R.attr.slMarkerDrawableStart] The marker (tick) drawable at the start.
 * @see [R.attr.slMarkerDrawableEnd] The marker (tick) drawable at the end.
 * @see [R.attr.slMarkerNum] The amount of markers on the track. The markers are distributed evenly spaced.
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class StyledBaseSliderView : AppCompatSeekBar {

    // Thumb
    protected var thumbDrawable: Drawable? = null
        set(value) {
            field = value
            onThumbDrawableChanged()
        }
    /**
     * The starting x of the thumb (align with the center of the thumb)
     */
    protected var thumbStartX: Float = 0f
    /**
     * The ending x of the thumb (align with the center of the thumb)
     */
    protected var thumbEndX: Float = 0f

    // Track
    protected var trackForegroundDrawable: Drawable? = null
        set(value) {
            ensureTrackDrawableBoundary(value)
            field = value
        }

    protected var trackBackgroundDrawable: Drawable?
        set(value) {
            ensureTrackDrawableBoundary(value)
            // The setter will take care of the boundary (by forcing layout)
            progressDrawable = value
        }
        get() {
            return progressDrawable
        }

    val tmpBound = RectF()

    // FIXME: Expose the XML configuration for this property.
    protected val scaledDragSlop by lazy { context.resources.getDimensionPixelSize(R.dimen.default_touch_drag_slop) }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        // By default determinate
        isIndeterminate = false

        // Force original thumb null
        thumb = null

        initProperties(attrs)
    }

    private fun initProperties(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.StyledSliderView, 0, 0)

        thumbDrawable = ContextCompat.getDrawable(context, R.drawable.default_slider_thumb)
        // Override the track drawable
        trackForegroundDrawable = ContextCompat.getDrawable(context, R.drawable.default_slider_foreground_track)
        trackBackgroundDrawable = ContextCompat.getDrawable(context, R.drawable.default_slider_background_track)

        for (i in 0 until typedArray.indexCount) {
            when (typedArray.getIndex(i)) {
                R.styleable.StyledSliderView_slThumbDrawable -> {
                    thumbDrawable = typedArray.getCompatDrawable(context, R.styleable.StyledSliderView_slThumbDrawable)
                }
                R.styleable.StyledSliderView_slTrackForegroundDrawable -> {
                    trackForegroundDrawable = typedArray.getCompatDrawable(context, R.styleable.StyledSliderView_slTrackForegroundDrawable)
                }
                R.styleable.StyledSliderView_slTrackBackgroundDrawable -> {
                    trackBackgroundDrawable = typedArray.getCompatDrawable(context, R.styleable.StyledSliderView_slTrackBackgroundDrawable)
                }
            }
        }

        typedArray.recycle()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed) {
            onThumbDrawableChanged()

            // Manually update foreground track boundary
            ensureTrackDrawableBoundary(trackForegroundDrawable)
        }
    }

    // Touch //////////////////////////////////////////////////////////////////

    protected var touchDownX: Float = -1f
    protected var touchX: Float = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return false

        val action = event.actionMasked
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (isInScrollingContainerCompat()) {
                    touchDownX = event.x
                } else {
                    isPressed = true
                    onStartTrackingTouch()
                    trackTouchEvent(event)
                    attemptClaimDrag()

                    // The thumb position might have changed and need to repaint.
                    invalidate()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDragging()) {
                    trackTouchEvent(event)
                } else {
                    val x = event.x
                    if (Math.abs(x - touchDownX) > scaledDragSlop) {
                        isPressed = true
                        onStartTrackingTouch()
                        trackTouchEvent(event)
                        attemptClaimDrag()
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                if (isDragging()) {
                    trackTouchEvent(event)
                    onStopTrackingTouch()
                    isPressed = false
                } else {
                    // Touch up when we never crossed the touch slop threshold should
                    // be interpreted as a tap-seek to that location.
                    onStartTrackingTouch()
                    trackTouchEvent(event)
                    onStopTrackingTouch()
                }
                // ProgressBar doesn't know to repaint the thumb drawable
                // in its inactive state when the touch stops (because the
                // value has not apparently changed)
                invalidate()
            }
            MotionEvent.ACTION_CANCEL -> {
                if (isDragging()) {
                    onStopTrackingTouch()
                    isPressed = false
                }
                invalidate() // see above explanation
            }
        }

        return true
    }

    protected open fun trackTouchEvent(event: MotionEvent) {
        val available = thumbEndX - thumbStartX
        val x = event.x.toInt()
        val newProgress = if (isLayoutRtl() && isMirrorForRtl()) {
            when {
                x < thumbStartX -> max.toFloat()
                x > thumbEndX -> 0f
                else -> (thumbEndX - x) / available
            }
        } else {
            when {
                x < thumbStartX -> 0f
                x > thumbEndX -> max.toFloat()
                else -> {
                    val percentage = (x - thumbStartX) / available
                    percentage * max
                }
            }
        }

        //     .       .       .
        // |-------+-------+--------|
        //    ^

        // setHotspot(x, event.y.toInt())
        progress = newProgress.roundToInt()
        touchX = event.x
    }

    /**
     * Tries to claim the user's drag motion, and requests disallowing any
     * ancestors from stealing events in the drag.
     */
    private fun attemptClaimDrag() {
        parent?.apply {
            requestDisallowInterceptTouchEvent(true)
        }
    }

    // Rendering //////////////////////////////////////////////////////////////

    /**
     * The moment for setting [thumbStartX] and [thumbEndX]. It could get triggered
     * by setting thumb drawable or layout change.
     */
    protected open fun onThumbDrawableChanged() {
        if (width == 0 || height == 0) return

        thumbStartX = paddingLeft.toFloat()
        thumbEndX = (width - paddingRight).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        drawTrack(canvas)
        drawThumb(canvas)
    }

    private fun ensureTrackDrawableBoundary(drawable: Drawable?) {
        val validViewWidth = width - paddingLeft - paddingRight
        val validViewHeight = height - paddingTop - paddingBottom
        if (validViewWidth <= 0 && validViewHeight <= 0) return

        drawable?.let {
            if (it.bounds.width() != validViewWidth ||
                it.bounds.height() != validViewHeight) {
                it.setBounds(0, 0, validViewWidth, validViewHeight)
            }
        }
    }

    /**
     * Called in [onDraw] and is to draw the foreground/background track.
     */
    protected open fun drawTrack(canvas: Canvas) {
        val paddingLeft = paddingLeft.toFloat()
        val paddingRight = paddingTop.toFloat()

        // Background track
        canvas.runSafely {
            translate(paddingLeft, paddingRight)
            trackBackgroundDrawable?.draw(canvas)
        }

        ensureTrackDrawableBoundary(trackForegroundDrawable)

        // Foreground track
        canvas.runSafely {
            translate(paddingLeft, paddingRight)

            val progressFloat = progress.toFloat() / max.toFloat()
            val drawableBound = trackForegroundDrawable?.bounds
            val clipWidth = (drawableBound?.width()?.toFloat() ?: 0f) * progressFloat
            val clipHeight = drawableBound?.height()?.toFloat() ?: 0f
            tmpBound.set(0f, 0f, clipWidth, clipHeight)
            clipRect(tmpBound)

            trackForegroundDrawable?.draw(canvas)
        }
    }

    /**
     * Called in [onDraw] and is to draw the thumb.
     */
    protected open fun drawThumb(canvas: Canvas) {
        // DO NOTHING
    }

    // override fun drawableHotspotChanged(x: Float, y: Float) {
    //     super.drawableHotspotChanged(x, y)
    // }
}
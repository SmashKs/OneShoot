/*
 * Copyright (C) 2018 The Smash Ks Open Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package smash.ks.com.oneshoot.widgets.customize.selectable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color.WHITE
import android.graphics.Paint
import android.graphics.Paint.Style.FILL
import android.graphics.Paint.Style.STROKE
import android.graphics.PointF
import android.graphics.PorterDuff.Mode.DST_OUT
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.widget.FrameLayout
import smash.ks.com.oneshoot.widgets.customize.selectable.SelectableAreaView.AnglePoint.LB
import smash.ks.com.oneshoot.widgets.customize.selectable.SelectableAreaView.AnglePoint.LT
import smash.ks.com.oneshoot.widgets.customize.selectable.SelectableAreaView.AnglePoint.RB
import smash.ks.com.oneshoot.widgets.customize.selectable.SelectableAreaView.AnglePoint.RT

/**
 * A customize view for selecting a rectangle area. It has four angles which be able to
 * let us control and adjust the size and the position of the rectangle.
 */
class SelectableAreaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    companion object {
        const val DEFAULT_GAP = 300f
        const val DEFAULT_STROKE_WIDTH = 2f
        const val DEFAULT_STROKE_HALF_WIDTH = 1f
        const val DEFAULT_ANGLE_WIDTH = 15f
        const val DEFAULT_TOUCH_RANGE = DEFAULT_ANGLE_WIDTH * 3
        const val DEFAULT_POSITION = 50f

        private const val DIRECT_LEFT = 0b0001
        private const val DIRECT_RIGHT = 0b0010
        private const val DIRECT_TOP = 0b0100
        private const val DIRECT_BOTTOM = 0b1000

        private const val FLOAT_ZERO = 0f

        private const val OUTER_ARGB_ALPHA = 150
        private const val TOUCHABLE_RANGE_TIMES = 4

        private const val SHIFT = 8

        private const val BLACK = 0xff
    }

    var selectedAreaCallback: ((x: Int, y: Int, width: Int, height: Int) -> Unit)? = null
        set(value) {
            val x = fourAngles[0].coordination.x.toInt()
            val y = fourAngles[0].coordination.y.toInt()
            val width = fourAngles[1].coordination.x.toInt() - x
            val height = fourAngles[1].coordination.y.toInt() - y

            value?.invoke(x, y, width, height)
            field = value
        }
    private var isTouchAngle = false
    private var isTouchInside = false
    private val paintRect by lazy {
        Paint().apply {
            setARGB(OUTER_ARGB_ALPHA, BLACK, BLACK, BLACK)
            isAntiAlias = true
            style = STROKE
            strokeWidth = DEFAULT_STROKE_WIDTH
        }
    }
    private val paintOuterRect by lazy {
        Paint().apply {
            setARGB(OUTER_ARGB_ALPHA, 0, 0, 0)
            isAntiAlias = true
            style = FILL
        }
    }
    private val paintInnerRect by lazy {
        Paint().apply {
            color = WHITE  // This is interacted area so any color is fine.
            isAntiAlias = true
            style = FILL
            xfermode = PorterDuffXfermode(DST_OUT)
        }
    }
    private val paintAngles by lazy {
        Paint().apply {
            setARGB(OUTER_ARGB_ALPHA, BLACK, BLACK, BLACK)
            isAntiAlias = true
        }
    }
    private val leftTopPoint by lazy {
        LT(false,
           PointF(DEFAULT_POSITION, DEFAULT_POSITION),
           DIRECT_LEFT or DIRECT_TOP)
    }
    private val rightBottomPoint by lazy {
        RB(false,
           PointF(DEFAULT_GAP + DEFAULT_POSITION, DEFAULT_GAP + DEFAULT_POSITION),
           DIRECT_RIGHT or DIRECT_BOTTOM)
    }
    private val leftBottomPoint by lazy {
        LB(false,
           PointF(DEFAULT_POSITION, DEFAULT_GAP + DEFAULT_POSITION),
           DIRECT_LEFT or DIRECT_BOTTOM)
    }
    private val rightTopPoint by lazy {
        RT(false,
           PointF(DEFAULT_GAP + DEFAULT_POSITION, DEFAULT_POSITION),
           DIRECT_RIGHT or DIRECT_TOP)
    }
    private val fourAngles by lazy { listOf(leftTopPoint, rightBottomPoint, leftBottomPoint, rightTopPoint) }
    /** This canvas rect object. */
    private val wholeRectangle by lazy { RectF(FLOAT_ZERO, FLOAT_ZERO, width.toFloat(), height.toFloat()) }
    /** Each angles' offset when touching the screen inside the rectangle. */
    private val listOffset by lazy { listOf(PointF(), PointF(), PointF(), PointF()) }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            ACTION_DOWN -> {
                // Four angles' area.
                fourAngles.forEach searching@{
                    val rangeLeft = it.coordination.x - DEFAULT_TOUCH_RANGE * TOUCHABLE_RANGE_TIMES
                    val rangeRight = it.coordination.x + DEFAULT_TOUCH_RANGE
                    val rangeTop = it.coordination.y - DEFAULT_TOUCH_RANGE
                    val rangeBottom = it.coordination.y + DEFAULT_TOUCH_RANGE

                    if (event.x in rangeLeft..rangeRight && event.y in rangeTop..rangeBottom) {
                        it.isSelected = true
                        isTouchAngle = true
                        return@searching
                    }
                }
                // Inside the rectangle excepting the overlapping of the four angles' area.
                if (!isTouchAngle &&
                    event.x in leftTopPoint.coordination.x..rightBottomPoint.coordination.x &&
                    event.y in leftTopPoint.coordination.y..rightBottomPoint.coordination.y) {
                    // Keeping the each offsets for each angles.
                    listOffset.zip(fourAngles).forEach { (offset, angles) ->
                        offset.x = event.x - angles.coordination.x
                        offset.y = event.y - angles.coordination.y
                    }

                    isTouchInside = true
                }
            }
            ACTION_MOVE -> {
                when {
                    isTouchAngle -> {
                        fourAngles.find(AnglePoint::isSelected)?.apply {
                            // Change the touching rectangle angle.
                            coordination.x = event.x
                            coordination.y = event.y
                            // Change others related rectangle angle.
                            fourAngles.forEach {
                                when (direction and it.direction) {
                                    DIRECT_LEFT, DIRECT_RIGHT -> it.coordination.x = event.x
                                    DIRECT_TOP, DIRECT_BOTTOM -> it.coordination.y = event.y
                                }
                            }
                        }
                    }
                    isTouchInside -> {
                        // Recalculate for the each angles position.
                        fourAngles.zip(listOffset).forEach { (angle, offset) ->
                            angle.coordination.x = event.x - offset.x
                            angle.coordination.y = event.y - offset.y
                        }
                    }
                }
                invalidate()
            }
            ACTION_UP -> {
                if (isTouchAngle || isTouchInside) {
                    val x = fourAngles[0].coordination.x.toInt()
                    val y = fourAngles[0].coordination.y.toInt()
                    val width = fourAngles[1].coordination.x.toInt() - x
                    val height = fourAngles[1].coordination.y.toInt() - y

                    selectedAreaCallback?.invoke(x, y, width, height)
                }
                fourAngles.find(AnglePoint::isSelected)?.isSelected = false
                isTouchAngle = false
                isTouchInside = false
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.apply {
            // Create a new canvas for doing interaction layer.
            saveLayer(wholeRectangle, null).apply {
                // All background rectangle (DST layer).
                drawRect(wholeRectangle, paintOuterRect)
                // Inner transport of selection area rectangle (SRC layer).
                drawRect(leftTopPoint.coordination.x,
                         leftTopPoint.coordination.y,
                         rightBottomPoint.coordination.x,
                         rightBottomPoint.coordination.y,
                         paintInnerRect)
            }.apply(::restoreToCount)
            // Selection Area Rectangle.
            drawRect(leftTopPoint.coordination.x,
                     leftTopPoint.coordination.y,
                     rightBottomPoint.coordination.x,
                     rightBottomPoint.coordination.y,
                     paintRect)
            // Four Angles
            fourAngles.forEach {
                // Create a new canvas for doing interaction layer.
                saveLayer(wholeRectangle, null).apply {
                    when (it) {
                        is LT -> {
                            // All background rectangle (DST layer).
                            drawRect(it.coordination.x + DEFAULT_STROKE_HALF_WIDTH,
                                     it.coordination.y + DEFAULT_STROKE_HALF_WIDTH,
                                     it.coordination.x + DEFAULT_STROKE_HALF_WIDTH + DEFAULT_TOUCH_RANGE,
                                     it.coordination.y + DEFAULT_STROKE_HALF_WIDTH + DEFAULT_TOUCH_RANGE,
                                     paintAngles)
                            // Inner transport of selection area rectangle (SRC layer).
                            drawRect(it.coordination.x + SHIFT,
                                     it.coordination.y + SHIFT,
                                     it.coordination.x + DEFAULT_TOUCH_RANGE + SHIFT,
                                     it.coordination.y + DEFAULT_TOUCH_RANGE + SHIFT,
                                     paintInnerRect)
                        }
                        is RB -> {
                            // All background rectangle (DST layer).
                            drawRect(it.coordination.x - DEFAULT_STROKE_HALF_WIDTH - DEFAULT_TOUCH_RANGE,
                                     it.coordination.y - DEFAULT_STROKE_HALF_WIDTH - DEFAULT_TOUCH_RANGE,
                                     it.coordination.x - DEFAULT_STROKE_HALF_WIDTH,
                                     it.coordination.y - DEFAULT_STROKE_HALF_WIDTH,
                                     paintAngles)
                            // Inner transport of selection area rectangle (SRC layer).
                            drawRect(it.coordination.x - DEFAULT_TOUCH_RANGE - SHIFT,
                                     it.coordination.y - DEFAULT_TOUCH_RANGE - SHIFT,
                                     it.coordination.x - SHIFT,
                                     it.coordination.y - SHIFT,
                                     paintInnerRect)
                        }
                        is LB -> {
                            // All background rectangle (DST layer).
                            drawRect(it.coordination.x + DEFAULT_STROKE_HALF_WIDTH,
                                     it.coordination.y - DEFAULT_STROKE_HALF_WIDTH - DEFAULT_TOUCH_RANGE,
                                     it.coordination.x + DEFAULT_STROKE_HALF_WIDTH + DEFAULT_TOUCH_RANGE,
                                     it.coordination.y - DEFAULT_STROKE_HALF_WIDTH,
                                     paintAngles)
                            // Inner transport of selection area rectangle (SRC layer).
                            drawRect(it.coordination.x + SHIFT,
                                     it.coordination.y - DEFAULT_TOUCH_RANGE - SHIFT,
                                     it.coordination.x + DEFAULT_TOUCH_RANGE + SHIFT,
                                     it.coordination.y - SHIFT,
                                     paintInnerRect)
                        }
                        is RT -> {
                            // All background rectangle (DST layer).
                            drawRect(it.coordination.x - DEFAULT_STROKE_HALF_WIDTH - DEFAULT_TOUCH_RANGE,
                                     it.coordination.y + DEFAULT_STROKE_HALF_WIDTH,
                                     it.coordination.x - DEFAULT_STROKE_HALF_WIDTH,
                                     it.coordination.y + DEFAULT_STROKE_HALF_WIDTH + DEFAULT_TOUCH_RANGE,
                                     paintAngles)
                            // Inner transport of selection area rectangle (SRC layer).
                            drawRect(it.coordination.x - DEFAULT_STROKE_HALF_WIDTH - DEFAULT_TOUCH_RANGE - SHIFT,
                                     it.coordination.y + DEFAULT_STROKE_HALF_WIDTH + SHIFT,
                                     it.coordination.x - DEFAULT_STROKE_HALF_WIDTH - SHIFT,
                                     it.coordination.y + DEFAULT_STROKE_HALF_WIDTH + DEFAULT_TOUCH_RANGE + SHIFT,
                                     paintInnerRect)
                        }
                    }
                }.apply(::restoreToCount)
            }
        }
    }

    /**
     * The angle point's properties of a rectangle.
     *
     * @property isSelected check myself is clicked or selected.
     * @property coordination the coordination.
     * @property direction the point's position. Ref: [DIRECT_LEFT], [DIRECT_RIGHT], [DIRECT_TOP], [DIRECT_BOTTOM].
     *                     Using bit to keep the position is easy to check by code.
     */
    sealed class AnglePoint(
        var isSelected: Boolean,
        var coordination: PointF,
        var direction: Int
    ) {
        /**
         * The left top point of a rectangle.
         */
        class LT(
            isSelected: Boolean,
            coordination: PointF,
            direction: Int
        ) : AnglePoint(isSelected, coordination, direction)

        /**
         * The left bottom point of a rectangle.
         */
        class LB(
            isSelected: Boolean,
            coordination: PointF, direction: Int
        ) : AnglePoint(isSelected, coordination, direction)

        /**
         * The right top point of a rectangle.
         */
        class RT(
            isSelected: Boolean,
            coordination: PointF, direction: Int
        ) : AnglePoint(isSelected, coordination, direction)

        /**
         * The right bottom point of a rectangle.
         */
        class RB(
            isSelected: Boolean,
            coordination: PointF, direction: Int
        ) : AnglePoint(isSelected, coordination, direction)
    }
}

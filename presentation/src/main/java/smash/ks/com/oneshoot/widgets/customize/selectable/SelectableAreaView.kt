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
import android.graphics.Color.CYAN
import android.graphics.Color.GREEN
import android.graphics.Color.RED
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

class SelectableAreaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    companion object {
        const val DEFAULT_GAP = 300f
        const val DEFAULT_STROKE_WIDTH = 2f
        const val DEFAULT_ANGLE_WIDTH = 15f
        const val DEFAULT_TOUCH_RANGE = DEFAULT_ANGLE_WIDTH * 3
        const val DEFAULT_POSITION = 50f

        private const val DIRECT_LEFT = 0b0001
        private const val DIRECT_RIGHT = 0b0010
        private const val DIRECT_TOP = 0b0100
        private const val DIRECT_BOTTOM = 0b1000
    }

    var selectedAreaCallback: ((x: Int, y: Int, width: Int, height: Int) -> Unit)? = null
    private var isTouchAngle = false
    private var isTouchInside = false
    private val paintRect by lazy {
        Paint().apply {
            color = RED
            isAntiAlias = true
            style = STROKE
            strokeWidth = DEFAULT_STROKE_WIDTH
        }
    }
    private val paintOuterRect by lazy {
        Paint().apply {
            setARGB(150, 0, 0, 0)
            isAntiAlias = true
            style = FILL
        }
    }
    private val paintInnerRect by lazy {
        Paint().apply {
            color = CYAN  // This is interacted area so any color is fine.
            isAntiAlias = true
            style = FILL
            xfermode = PorterDuffXfermode(DST_OUT)
        }
    }
    private val paintAngles by lazy {
        Paint().apply {
            color = GREEN
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
    private val wholeRectangle by lazy { RectF(0f, 0f, width.toFloat(), height.toFloat()) }
    /** Each angles' offset when touching the screen inside the rectangle. */
    private val listOffset by lazy { listOf(PointF(), PointF(), PointF(), PointF()) }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            ACTION_DOWN -> {
                // Four angles' area.
                fourAngles.forEach searching@{
                    if (event.x in it.coordination.x - DEFAULT_TOUCH_RANGE * 4..it.coordination.x + DEFAULT_TOUCH_RANGE &&
                        event.y in it.coordination.y - DEFAULT_TOUCH_RANGE..it.coordination.y + DEFAULT_TOUCH_RANGE) {
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
                    selectedAreaCallback?.invoke(fourAngles[0].coordination.x.toInt(),
                                                 fourAngles[0].coordination.y.toInt(),
                                                 fourAngles[1].coordination.x.toInt() - fourAngles[0].coordination.x.toInt(),
                                                 fourAngles[1].coordination.y.toInt() - fourAngles[0].coordination.y.toInt())
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
                drawRect(it.coordination.x - DEFAULT_ANGLE_WIDTH, it.coordination.y - DEFAULT_ANGLE_WIDTH,
                         it.coordination.x + DEFAULT_ANGLE_WIDTH, it.coordination.y + DEFAULT_ANGLE_WIDTH, paintAngles)
            }
        }
    }

    sealed class AnglePoint(
        var isSelected: Boolean,
        var coordination: PointF,
        var direction: Int
    ) {
        class LT(
            isSelected: Boolean,
            coordination: PointF,
            direction: Int
        ) : AnglePoint(isSelected, coordination, direction)

        class LB(
            isSelected: Boolean,
            coordination: PointF, direction: Int
        ) : AnglePoint(isSelected, coordination, direction)

        class RT(
            isSelected: Boolean,
            coordination: PointF, direction: Int
        ) : AnglePoint(isSelected, coordination, direction)

        class RB(
            isSelected: Boolean,
            coordination: PointF, direction: Int
        ) : AnglePoint(isSelected, coordination, direction)
    }
}

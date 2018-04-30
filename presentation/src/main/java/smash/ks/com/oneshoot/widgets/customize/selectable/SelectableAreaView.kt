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
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.graphics.Paint
import android.graphics.Paint.Style.STROKE
import android.graphics.PointF
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
        const val DEFAULT_GAP = 100f
        const val DEFAULT_STROKE_WIDTH = 5f
        const val DEFAULT_TOUCH_RANGE = 10f
        const val DEFAULT_POSITION = 50f
    }

    private var isTouch = false
    private val paint by lazy {
        Paint().apply {
            color = RED
            isAntiAlias = true
            style = STROKE
            strokeWidth = DEFAULT_STROKE_WIDTH
        }
    }
    private val paintAngles by lazy {
        Paint().apply {
            color = GREEN
            isAntiAlias = true
        }
    }
    private val leftTopPoint by lazy { LT(false, PointF(DEFAULT_POSITION, DEFAULT_POSITION)) }
    private val rightBottomPoint by lazy {
        RB(false, PointF(DEFAULT_GAP + DEFAULT_POSITION, DEFAULT_GAP + DEFAULT_POSITION))
    }
    private val leftBottomPoint by lazy { LB(false, PointF(DEFAULT_GAP + DEFAULT_POSITION, DEFAULT_POSITION)) }
    private val rightTopPoint by lazy { RT(false, PointF(DEFAULT_POSITION, DEFAULT_GAP + DEFAULT_POSITION)) }
    private val fourAngles by lazy { listOf(leftTopPoint, rightBottomPoint, leftBottomPoint, rightTopPoint) }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            ACTION_DOWN -> {
                fourAngles.forEach searching@{
                    if (event.x in it.coordination.x - DEFAULT_TOUCH_RANGE..it.coordination.x + DEFAULT_TOUCH_RANGE &&
                        event.y in it.coordination.y - DEFAULT_TOUCH_RANGE..it.coordination.y + DEFAULT_TOUCH_RANGE
                    ) {
                        it.isSelected = true
                        isTouch = true
                        return@searching
                    }
                }
            }
            ACTION_MOVE -> {
                if (!isTouch) return true

                val p = fourAngles.find(AnglePoint::isSelected) ?: throw Resources.NotFoundException()
                p.coordination.x = event.x
                p.coordination.y = event.y

                invalidate()
            }
            ACTION_UP -> {
                fourAngles.find(AnglePoint::isSelected)?.isSelected = false
                isTouch = false
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.apply {
            // Center image
            drawRect(leftTopPoint.coordination.x,
                     leftTopPoint.coordination.y,
                     rightBottomPoint.coordination.x,
                     rightBottomPoint.coordination.y,
                     paint)
            // Four Angles
            fourAngles.forEach {
                drawRect(it.coordination.x - DEFAULT_TOUCH_RANGE, it.coordination.y - DEFAULT_TOUCH_RANGE,
                         it.coordination.x + DEFAULT_TOUCH_RANGE, it.coordination.y + DEFAULT_TOUCH_RANGE, paintAngles)
            }
        }
    }

    sealed class AnglePoint(
        var isSelected: Boolean,
        var coordination: PointF
    ) {
        class LT(isSelected: Boolean, coordination: PointF) : AnglePoint(isSelected, coordination)
        class LB(isSelected: Boolean, coordination: PointF) : AnglePoint(isSelected, coordination)
        class RT(isSelected: Boolean, coordination: PointF) : AnglePoint(isSelected, coordination)
        class RB(isSelected: Boolean, coordination: PointF) : AnglePoint(isSelected, coordination)
    }
}
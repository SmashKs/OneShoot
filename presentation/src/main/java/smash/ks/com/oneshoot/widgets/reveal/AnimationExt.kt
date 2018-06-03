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

package smash.ks.com.oneshoot.widgets.reveal

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Build
import android.os.Parcelable
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.View
import android.view.ViewAnimationUtils
import com.devrapid.kotlinknifer.gone
import com.ks.smash.ext.const.DEFAULT_INT
import kotlinx.android.parcel.Parcelize
import kotlin.math.pow

private const val CIRCULAR_REVEAL_DURATION = 1000L

fun View.registerCircularRevealAnimation(
    revealSettings: RevealAnimationSetting,
    startColor: Int,
    endColor: Int
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                v.removeOnLayoutChangeListener(this)
                val (cx, cy, width, height) = revealSettings

                // Simply use the diagonal of the view.
                val radius = Math.sqrt((width.toDouble().pow(2) + height.toDouble().pow(2))).toFloat()
                ViewAnimationUtils.createCircularReveal(v, cx, cy, 0f, radius).apply {
                    this.duration = CIRCULAR_REVEAL_DURATION
                    interpolator = FastOutSlowInInterpolator()
                }.start()
            }
        })
        startColorAnimation(startColor, endColor, CIRCULAR_REVEAL_DURATION)
    }
}

fun View.startCircularExitAnimation(
    revealSettings: RevealAnimationSetting,
    startColor: Int,
    endColor: Int,
    listener: () -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val (cx, cy, width, height) = revealSettings

        // Simply use the diagonal of the view.
        val radius = Math.sqrt((width.toDouble().pow(2) + height.toDouble().pow(2))).toFloat()
        ViewAnimationUtils.createCircularReveal(this, cx, cy, radius, 0f).apply {
            this.duration = CIRCULAR_REVEAL_DURATION
            interpolator = FastOutSlowInInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    gone()
                    listener()
                }
            })
        }.start()
        startColorAnimation(startColor, endColor, CIRCULAR_REVEAL_DURATION)
    }
    else {
        listener()
    }
}

fun View.startColorAnimation(
    startColor: Int,
    endColor: Int,
    duration: Long
) {
    ValueAnimator().apply {
        this.duration = duration
        setIntValues(startColor, endColor)
        setEvaluator(ArgbEvaluator())
        addUpdateListener { setBackgroundColor(it.animatedValue as? Int ?: DEFAULT_INT) }
    }.start()
}

@Parcelize
data class RevealAnimationSetting(
    var centerX: Int = 0,
    var centerY: Int = 0,
    var width: Int = 0,
    var height: Int = 0
) : Parcelable

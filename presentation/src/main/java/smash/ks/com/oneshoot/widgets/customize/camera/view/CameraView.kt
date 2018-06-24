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

package smash.ks.com.oneshoot.widgets.customize.camera.view

import android.app.Activity
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH
import android.os.Build.VERSION_CODES.M
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View.MeasureSpec.AT_MOST
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.getMode
import android.view.View.MeasureSpec.getSize
import android.view.View.MeasureSpec.makeMeasureSpec
import android.widget.FrameLayout
import androidx.annotation.IntDef
import androidx.core.view.ViewCompat
import smash.ks.com.ext.castOrNull
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.widgets.customize.camera.Camera2
import smash.ks.com.oneshoot.widgets.customize.camera.Camera2Api23
import smash.ks.com.oneshoot.widgets.customize.camera.module.AspectRatio
import smash.ks.com.oneshoot.widgets.customize.camera.module.CameraViewModule
import smash.ks.com.oneshoot.widgets.customize.camera.module.Constants.DEFAULT_ASPECT_RATIO
import smash.ks.com.oneshoot.widgets.customize.camera.module.Constants.FACING_BACK
import smash.ks.com.oneshoot.widgets.customize.camera.module.Constants.FACING_FRONT
import smash.ks.com.oneshoot.widgets.customize.camera.module.Constants.FLASH_AUTO
import smash.ks.com.oneshoot.widgets.customize.camera.module.Constants.FLASH_OFF
import smash.ks.com.oneshoot.widgets.customize.camera.module.Constants.FLASH_ON
import smash.ks.com.oneshoot.widgets.customize.camera.module.Constants.FLASH_RED_EYE
import smash.ks.com.oneshoot.widgets.customize.camera.module.Constants.FLASH_TORCH
import smash.ks.com.oneshoot.widgets.customize.camera.preview.SurfaceViewPreview
import smash.ks.com.oneshoot.widgets.customize.camera.preview.TextureViewPreview
import smash.ks.com.oneshoot.widgets.customize.camera.view.DisplayOrientationDetector.Companion.DEGREE_180
import kotlin.annotation.AnnotationRetention.SOURCE

/**
 * A customize view of the camera view and thru this view.
 */
open class CameraView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    /** Direction the camera faces relative to device screen.  */
    @IntDef(FACING_BACK, FACING_FRONT)
    @Retention(SOURCE)
    annotation class Facing

    /** The mode for for the camera device's flash control  */
    @IntDef(FLASH_OFF, FLASH_ON, FLASH_TORCH, FLASH_AUTO, FLASH_RED_EYE)
    annotation class Flash

    private var adjustViewBounds: Boolean = false
    private val cameraViewModule by lazy {
        val preview = createPreview(context)

        if (SDK_INT < M) {
            Camera2(callbacks, preview, context)
        }
        else {
            Camera2Api23(callbacks, preview, context)
        }
    }
    private val displayOrientationDetector by lazy {
        // Display orientation detector
        if (isInEditMode) {
            null
        }
        else {
            object : DisplayOrientationDetector(context) {
                override fun onDisplayOrientationChanged(displayOrientation: Int) {
                    cameraViewModule.setDisplayOrientation(displayOrientation)
                }
            }
        }
    }
    private val callbacks by lazy { if (isInEditMode) null else CallbackBridge() }

    init {
        // Attributes
        context.obtainStyledAttributes(attrs, R.styleable.CameraView, defStyleAttr, R.style.Widget_CameraView).apply {
            val aspectRatio = getString(R.styleable.CameraView_aspectRatio)
            setAspectRatio(aspectRatio
                               .takeIf { null != it }
                               ?.let { AspectRatio.parse(aspectRatio) } ?: DEFAULT_ASPECT_RATIO)
            adjustViewBounds = getBoolean(R.styleable.CameraView_android_adjustViewBounds, false)
            setFacing(getInt(R.styleable.CameraView_facing, FACING_BACK))
            setAutoFocus(getBoolean(R.styleable.CameraView_autoFocus, true))
            setFlash(getInt(R.styleable.CameraView_flash, FLASH_AUTO))
        }.recycle()
    }

    //region Override methods
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (!isInEditMode) displayOrientationDetector?.enable(ViewCompat.getDisplay(this)!!)
    }

    override fun onDetachedFromWindow() {
        if (!isInEditMode) displayOrientationDetector?.disable()

        super.onDetachedFromWindow()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isInEditMode) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        // Handle android:adjustViewBounds
        if (adjustViewBounds) {
            if (!isCameraOpened()) {
                callbacks?.reserveRequestLayoutOnOpen()
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
                return
            }

            val widthMode = getMode(widthMeasureSpec)
            val heightMode = getMode(heightMeasureSpec)
            val ratio = getAspectRatio()

            if (EXACTLY == widthMode && EXACTLY != heightMode) {
                var height = (getSize(widthMeasureSpec) * ratio.toFloat()).toInt()
                if (AT_MOST == heightMode) {
                    height = Math.min(height, getSize(heightMeasureSpec))
                }

                super.onMeasure(widthMeasureSpec, makeMeasureSpec(height, EXACTLY))
            }
            else if (EXACTLY != widthMode && EXACTLY == heightMode) {
                var width = (getSize(heightMeasureSpec) * ratio.toFloat()).toInt()
                if (AT_MOST == widthMode) {
                    width = Math.min(width, getSize(widthMeasureSpec))
                }

                super.onMeasure(makeMeasureSpec(width, EXACTLY), heightMeasureSpec)
            }
            else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
        }
        else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }

        // Measure the TextureView
        val width = measuredWidth
        val height = measuredHeight
        var ratio = getAspectRatio()

        displayOrientationDetector
            ?.lastKnownDisplayOrientation
            ?.takeIf { 0 == it % DEGREE_180 }
            ?.let { ratio = ratio.inverse() }

        if (height < width * ratio.y / ratio.x) {
            cameraViewModule.view.measure(
                makeMeasureSpec(width, EXACTLY),
                makeMeasureSpec(width * ratio.y / ratio.x, EXACTLY))
        }
        else {
            cameraViewModule.view.measure(
                makeMeasureSpec(height * ratio.x / ratio.y, EXACTLY),
                makeMeasureSpec(height, EXACTLY))
        }
    }

    override fun onSaveInstanceState() = SavedState(super.onSaveInstanceState()).apply {
        facing = getFacing()
        ratio = getAspectRatio()
        autoFocus = getAutoFocus()
        flash = getFlash()
    }

    override fun onRestoreInstanceState(state: Parcelable?) = state.let { castOrNull<SavedState>(it) }?.let {
        super.onRestoreInstanceState(it.superState)
        setFacing(it.facing)
        setAspectRatio(it.ratio)
        setAutoFocus(it.autoFocus)
        setFlash(it.flash)
    } ?: super.onRestoreInstanceState(state)
    //endregion

    /**
     * Open a camera device and start showing camera preview. This is typically called from
     * [Activity.onResume].
     */
    fun start() {
        if (!cameraViewModule.start()) {
            //store the state ,and restore this state after fall back o Camera1
            val state = onSaveInstanceState()

            // OPTIMIZE(jieyi): 2018/04/25 Camera2 uses legacy hardware layer; fall back to Camera1
            // cameraViewModule = Camera1(callbacks, createPreview(context))

            onRestoreInstanceState(state)
            cameraViewModule.start()
        }
    }

    /**
     * Stop camera preview and close the device. This is typically called from [Activity.onPause].
     */
    open fun stop() = cameraViewModule.stop()

    /**
     * @return `true` if the camera is opened.
     */
    fun isCameraOpened() = cameraViewModule.isCameraOpened

    /**
     * Add a new callback.
     *
     * @param callback The [Callback] to add.
     * @see .removeCallback
     */
    fun addCallback(callback: Callback) = callbacks?.add(callback)

    /**
     * Remove a callback.
     *
     * @param callback The [Callback] to remove.
     * @see .addCallback
     */
    fun removeCallback(callback: Callback) = callbacks?.remove(callback)

    /**
     * @param adjustViewBounds `true` if you want the CameraView to adjust its bounds to
     * preserve the aspect ratio of camera.
     * @see .getAdjustViewBounds
     */
    fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        if (this.adjustViewBounds == adjustViewBounds) return

        this.adjustViewBounds = adjustViewBounds
        requestLayout()
    }

    /**
     * @return True when this CameraView is adjusting its bounds to preserve the aspect ratio of
     * camera.
     * @see .setAdjustViewBounds
     */
    fun getAdjustViewBounds() = adjustViewBounds

    /**
     * Chooses camera by the direction it faces.
     *
     * @param facing The camera facing. Must be either [FACING_BACK] or
     * [FACING_FRONT].
     */
    fun setFacing(@Facing facing: Int) {
        cameraViewModule.facing = facing
    }

    /**
     * Gets the direction that the current camera faces.
     *
     * @return The camera facing.
     */
    @Facing
    fun getFacing() = cameraViewModule.facing

    /**
     * Gets all the aspect ratios supported by the current camera.
     */
    fun getSupportedAspectRatios() = cameraViewModule.supportedAspectRatios

    /**
     * Sets the aspect ratio of camera.
     *
     * @param ratio The [AspectRatio] to be set.
     */
    fun setAspectRatio(ratio: AspectRatio?) {
        if (null != ratio && cameraViewModule.setAspectRatio(ratio)) {
            requestLayout()
        }
    }

    /**
     * Gets the current aspect ratio of camera.
     *
     * @return The current [AspectRatio]. Can be `null` if no camera is opened yet.
     */
    fun getAspectRatio() = cameraViewModule.aspectRatio

    /**
     * Enables or disables the continuous auto-focus mode. When the current camera doesn't support
     * auto-focus, calling this method will be ignored.
     *
     * @param autoFocus `true` to enable continuous auto-focus mode. `false` to
     * disable it.
     */
    fun setAutoFocus(autoFocus: Boolean) {
        cameraViewModule.autoFocus = autoFocus
    }

    /**
     * Returns whether the continuous auto-focus mode is enabled.
     *
     * @return `true` if the continuous auto-focus mode is enabled. `false` if it is
     * disabled, or if it is not supported by the current camera.
     */
    fun getAutoFocus() = cameraViewModule.autoFocus

    /**
     * Sets the flash mode.
     *
     * @param flash The desired flash mode.
     */
    fun setFlash(@Flash flash: Int) {
        cameraViewModule.flash = flash
    }

    /**
     * Gets the current flash mode.
     *
     * @return The current flash mode.
     */
    @Flash
    fun getFlash() = cameraViewModule.flash

    /**
     * Take a picture. The result will be returned to
     * [Callback.onPictureTaken].
     */
    fun takePicture() = cameraViewModule.takePicture()

    private fun createPreview(context: Context) = if (SDK_INT < ICE_CREAM_SANDWICH) {
        SurfaceViewPreview(context, this)
    }
    else {
        TextureViewPreview(context, this)
    }

    /**
     * Callback for monitoring events about [CameraView].
     */
    abstract class Callback {
        /**
         * Called when camera is opened.
         *
         * @param cameraView The associated [CameraView].
         */
        open fun onCameraOpened(cameraView: CameraView) {}

        /**
         * Called when camera is closed.
         *
         * @param cameraView The associated [CameraView].
         */
        open fun onCameraClosed(cameraView: CameraView) {}

        /**
         * Called when a picture is taken.
         *
         * @param cameraView The associated [CameraView].
         * @param data JPEG data.
         */
        open fun onPictureTaken(cameraView: CameraView, data: ByteArray) {}
    }

    protected class SavedState : BaseSavedState {
        var ratio: AspectRatio? = null
        var autoFocus = false
        @Facing var facing = 0
        @Flash var flash = 0

        constructor(source: Parcel, loader: ClassLoader) : super(source) {
            facing = source.readInt()
            ratio = source.readParcelable(loader)
            autoFocus = source.readByte().toInt() != 0
            flash = source.readInt()
        }

        constructor(superState: Parcelable) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(facing)
            out.writeParcelable(ratio, 0)
            out.writeByte((if (autoFocus) 1 else 0).toByte())
            out.writeInt(flash)
        }
    }

    private inner class CallbackBridge : CameraViewModule.Callback {
        private val callbacks by lazy { ArrayList<Callback>() }
        private var requestLayoutOnOpen = false

        fun add(callback: Callback) = callbacks.add(callback)

        fun remove(callback: Callback) = callbacks.remove(callback)

        override fun onCameraOpened() {
            if (requestLayoutOnOpen) {
                requestLayoutOnOpen = false
                requestLayout()
            }
            for (callback in callbacks) callback.onCameraOpened(this@CameraView)
        }

        override fun onCameraClosed() {
            for (callback in callbacks) callback.onCameraClosed(this@CameraView)
        }

        override fun onPictureTaken(data: ByteArray) {
            for (callback in callbacks) callback.onPictureTaken(this@CameraView, data)
        }

        fun reserveRequestLayoutOnOpen() {
            requestLayoutOnOpen = true
        }
    }
}

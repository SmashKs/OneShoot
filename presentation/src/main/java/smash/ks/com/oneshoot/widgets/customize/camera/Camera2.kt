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

package smash.ks.com.oneshoot.widgets.customize.camera

import android.annotation.TargetApi
import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.graphics.ImageFormat.JPEG
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES
import android.hardware.camera2.CameraCharacteristics.CONTROL_AF_MODE_OFF
import android.hardware.camera2.CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL
import android.hardware.camera2.CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY
import android.hardware.camera2.CameraCharacteristics.LENS_FACING
import android.hardware.camera2.CameraCharacteristics.LENS_FACING_BACK
import android.hardware.camera2.CameraCharacteristics.LENS_FACING_FRONT
import android.hardware.camera2.CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
import android.hardware.camera2.CameraCharacteristics.SENSOR_ORIENTATION
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW
import android.hardware.camera2.CameraDevice.TEMPLATE_STILL_CAPTURE
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CaptureRequest.CONTROL_AE_MODE
import android.hardware.camera2.CaptureRequest.CONTROL_AE_MODE_ON
import android.hardware.camera2.CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH
import android.hardware.camera2.CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
import android.hardware.camera2.CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE
import android.hardware.camera2.CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER
import android.hardware.camera2.CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_IDLE
import android.hardware.camera2.CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START
import android.hardware.camera2.CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED
import android.hardware.camera2.CaptureRequest.CONTROL_AF_MODE
import android.hardware.camera2.CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
import android.hardware.camera2.CaptureRequest.CONTROL_AF_TRIGGER
import android.hardware.camera2.CaptureRequest.CONTROL_AF_TRIGGER_CANCEL
import android.hardware.camera2.CaptureRequest.CONTROL_AF_TRIGGER_IDLE
import android.hardware.camera2.CaptureRequest.CONTROL_AF_TRIGGER_START
import android.hardware.camera2.CaptureRequest.FLASH_MODE
import android.hardware.camera2.CaptureRequest.FLASH_MODE_OFF
import android.hardware.camera2.CaptureRequest.FLASH_MODE_TORCH
import android.hardware.camera2.CaptureRequest.JPEG_ORIENTATION
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.CaptureResult.CONTROL_AE_STATE
import android.hardware.camera2.CaptureResult.CONTROL_AE_STATE_CONVERGED
import android.hardware.camera2.CaptureResult.CONTROL_AE_STATE_PRECAPTURE
import android.hardware.camera2.CaptureResult.CONTROL_AF_STATE
import android.hardware.camera2.CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED
import android.hardware.camera2.CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED
import android.hardware.camera2.TotalCaptureResult
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.media.ImageReader.newInstance
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.util.Log
import android.util.SparseIntArray
import org.jetbrains.anko.toast
import smash.ks.com.oneshoot.widgets.customize.camera.Camera2.PictureCaptureCallback.Companion.STATE_LOCKING
import smash.ks.com.oneshoot.widgets.customize.camera.Camera2.PictureCaptureCallback.Companion.STATE_PREVIEW
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
import smash.ks.com.oneshoot.widgets.customize.camera.module.Preview
import smash.ks.com.oneshoot.widgets.customize.camera.module.Size
import smash.ks.com.oneshoot.widgets.customize.camera.module.SizeMap
import smash.ks.com.oneshoot.widgets.customize.camera.view.DisplayOrientationDetector.Companion.DEGREE_360

/**
 * The Camera2 Api below Android api [LOLLIPOP] 22.
 */
@TargetApi(LOLLIPOP)
open class Camera2(callback: Callback?, preview: Preview, context: Context) : CameraViewModule(callback, preview) {
    companion object {
        private val FACINGS by lazy {
            SparseIntArray().apply {
                put(FACING_BACK, LENS_FACING_BACK)
                put(FACING_FRONT, LENS_FACING_FRONT)
            }
        }

        private const val TAG = "Camera2"
        /** Max preview width that is guaranteed by Camera2 API */
        private const val MAX_PREVIEW_WIDTH = 1920
        /** Max preview height that is guaranteed by Camera2 API */
        private const val MAX_PREVIEW_HEIGHT = 1080
    }

    var captureCallback = object : PictureCaptureCallback() {
        override fun onPrecaptureRequired() {
            previewRequestBuilder!!.set(CONTROL_AE_PRECAPTURE_TRIGGER, CONTROL_AE_PRECAPTURE_TRIGGER_START)
            setState(STATE_PRECAPTURE)
            try {
                captureSession!!.capture(previewRequestBuilder!!.build(), this, null)
                previewRequestBuilder!!.set(CONTROL_AE_PRECAPTURE_TRIGGER, CONTROL_AE_PRECAPTURE_TRIGGER_IDLE)
            }
            catch (e: CameraAccessException) {
                Log.e(TAG, "Failed to run precapture sequence.", e)
            }
        }

        override fun onReady() {
            captureStillPicture()
        }
    }
    var camera: CameraDevice? = null
    var captureSession: CameraCaptureSession? = null
    var previewRequestBuilder: CaptureRequest.Builder? = null
    // Revert
    override var flash = 0
        set(flash) {
            if (this.flash == flash) return

            val saved = this.flash
            field = flash
            if (null != previewRequestBuilder) {
                updateFlash()
                if (null != captureSession) {
                    try {
                        captureSession!!.setRepeatingRequest(previewRequestBuilder!!.build(), captureCallback, null)
                    }
                    catch (e: CameraAccessException) {
                        field = saved
                    }
                }
            }
        }
    override var facing
        get() = mFacing
        set(facing) {
            if (mFacing == facing) return

            mFacing = facing

            if (isCameraOpened) {
                stop()
                start()
            }
        }
    // Revert
    override var autoFocus
        get() = mAutoFocus
        set(autoFocus) {
            if (mAutoFocus == autoFocus) return

            mAutoFocus = autoFocus
            if (null != previewRequestBuilder) {
                updateAutoFocus()
                if (null != captureSession) {
                    try {
                        captureSession!!.setRepeatingRequest(previewRequestBuilder!!.build(), captureCallback, null)
                    }
                    catch (e: CameraAccessException) {
                        mAutoFocus = !mAutoFocus
                    }
                }
            }
        }

    private var cameraId: String? = null
    private var cameraCharacteristics: CameraCharacteristics? = null
    private var imageReader: ImageReader? = null
    private var displayOrientation = 0
    private var mFacing = 0
    private var mAspectRatio = DEFAULT_ASPECT_RATIO
    private var mAutoFocus = false
    override val aspectRatio get() = mAspectRatio
    override val isCameraOpened get() = null != camera
    override val supportedAspectRatios get() = previewSizes.ratios()
    private val previewSizes by lazy { SizeMap() }
    private val pictureSizes by lazy { SizeMap() }
    private val cameraManager by lazy {
        context.getSystemService(CAMERA_SERVICE) as? CameraManager ?: throw ClassCastException()
    }
    private val cameraDeviceCallback by lazy {
        object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                this@Camera2.camera = camera
                this@Camera2.callback?.onCameraOpened()
                startCaptureSession()
            }

            override fun onClosed(camera: CameraDevice) {
                this@Camera2.callback?.onCameraClosed()
            }

            override fun onDisconnected(camera: CameraDevice) {
                this@Camera2.camera = null
            }

            override fun onError(camera: CameraDevice, error: Int) {
                Log.e(TAG, "onError: " + camera.id + " (" + error + ")")
                this@Camera2.camera = null
            }
        }
    }
    private val sessionCallback by lazy {
        object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                if (null == camera) return

                // When the session is ready, we start displaying the preview.
                captureSession = session
                try {
                    // Auto focus should be continuous for camera preview.
                    updateAutoFocus()
                    // Flash is automatically enabled when necessary.
                    updateFlash()
                    // Finally, we start displaying the camera preview.
                    val previewRequest = previewRequestBuilder?.build()

                    captureSession!!.setRepeatingRequest(previewRequest, captureCallback, null)
                }
                catch (e: CameraAccessException) {
                    Log.e(TAG, "Failed to start camera preview because it couldn't access camera", e)
                }
                catch (e: IllegalStateException) {
                    Log.e(TAG, "Failed to start camera preview.", e)
                }
                catch (e: NullPointerException) {
                    Log.e(TAG, "CaptureSession is a null point.", e)
                }
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                context.toast("Failed to configure capture session.")
                Log.e(TAG, "Failed to configure capture session.")
            }

            override fun onClosed(session: CameraCaptureSession) {
                if (null != captureSession && captureSession == session) {
                    captureSession = null
                }
            }
        }
    }
    private val mOnImageAvailableListener by lazy {
        OnImageAvailableListener { reader ->
            reader.acquireNextImage().use { image ->
                val planes = image.planes
                if (planes.isNotEmpty()) {
                    val buffer = planes[0].buffer
                    val data = ByteArray(buffer.remaining())
                    buffer[data]
                    this.callback?.onPictureTaken(data)
                }
            }
        }
    }

    init {
        this.preview.onSurfaceChanged = { startCaptureSession() }
    }

    protected open fun collectPictureSizes(sizes: SizeMap, map: StreamConfigurationMap) {
        for (size in map.getOutputSizes(JPEG)) {
            pictureSizes.add(Size(size.width, size.height))
        }
    }

    override fun start(): Boolean {
//        if (!chooseCameraIdByFacing()) return false
        chooseCameraIdByFacing()

        collectCameraInfo()
        prepareImageReader()
        startOpeningCamera()

        return true
    }

    override fun stop() {
        if (captureSession != null) {
            captureSession!!.close()
            captureSession = null
        }
        if (camera != null) {
            camera!!.close()
            camera = null
        }
        if (imageReader != null) {
            imageReader!!.close()
            imageReader = null
        }
    }

    override fun setAspectRatio(ratio: AspectRatio): Boolean {
        if (ratio == mAspectRatio || !previewSizes.ratios().contains(ratio)) {
            // TODO: Better error handling
            return false
        }
        mAspectRatio = ratio
        prepareImageReader()
        if (null != captureSession) {
            captureSession!!.close()
            captureSession = null
            startCaptureSession()
        }
        return true
    }

    override fun takePicture() = if (mAutoFocus) lockFocus() else captureStillPicture()

    override fun setDisplayOrientation(displayOrientation: Int) {
        this.displayOrientation = displayOrientation
        preview.setDisplayOrientation(this.displayOrientation)
    }

    /**
     * Starts a capture session for camera preview.
     *
     * This rewrites [previewRequestBuilder].
     *
     * The result will be continuously processed in [sessionCallback].
     */
    fun startCaptureSession() {
        if (!isCameraOpened || !preview.isReady || null == imageReader) return

        val previewSize = chooseOptimalSize()
        preview.setBufferSize(previewSize.width, previewSize.height)
        val surface = preview.surface

        try {
            previewRequestBuilder = camera!!.createCaptureRequest(TEMPLATE_PREVIEW)
            previewRequestBuilder!!.addTarget(surface)
            camera!!.createCaptureSession(listOf(surface, imageReader!!.surface), sessionCallback, null)
        }
        catch (e: CameraAccessException) {
            throw RuntimeException("Failed to start camera session")
        }
    }

    /**
     * Updates the state of auto-focus to [mAutoFocus].
     */
    fun updateAutoFocus() = when {
        mAutoFocus -> {
            val modes = cameraCharacteristics!![CONTROL_AF_AVAILABLE_MODES]
            // Auto focus is not supported
            previewRequestBuilder!![CONTROL_AF_MODE] = when {
                modes.isEmpty() || 1 == modes.size && CONTROL_AF_MODE_OFF == modes[0] -> {
                    mAutoFocus = false
                    CONTROL_AF_MODE_OFF
                }
                else -> CONTROL_AF_MODE_CONTINUOUS_PICTURE
            }
        }
        else -> previewRequestBuilder!![CONTROL_AF_MODE] = CONTROL_AF_MODE_OFF
    }

    /**
     * Updates the state of flash to [flash].
     */
    fun updateFlash() {
        when (flash) {
            FLASH_OFF -> listOf(CONTROL_AE_MODE to CONTROL_AE_MODE_ON, FLASH_MODE to FLASH_MODE_OFF)
            FLASH_ON -> listOf(CONTROL_AE_MODE to CONTROL_AE_MODE_ON_ALWAYS_FLASH, FLASH_MODE to FLASH_MODE_OFF)
            FLASH_TORCH -> listOf(CONTROL_AE_MODE to CONTROL_AE_MODE_ON, FLASH_MODE to FLASH_MODE_TORCH)
            FLASH_AUTO -> listOf(CONTROL_AE_MODE to CONTROL_AE_MODE_ON_AUTO_FLASH, FLASH_MODE to FLASH_MODE_OFF)
            FLASH_RED_EYE -> listOf(CONTROL_AE_MODE to CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE,
                                    FLASH_MODE to FLASH_MODE_OFF)
            else -> emptyList()
        }.forEach { previewRequestBuilder!![it.first] = it.second }
    }

    /**
     * Captures a still picture.
     */
    fun captureStillPicture() {
        try {
            camera!!.createCaptureRequest(TEMPLATE_STILL_CAPTURE).apply {
                addTarget(imageReader!!.surface)
                set(CONTROL_AF_MODE, previewRequestBuilder!![CONTROL_AF_MODE])
                when (flash) {
                    FLASH_OFF -> listOf(CONTROL_AE_MODE to CONTROL_AE_MODE_ON, FLASH_MODE to FLASH_MODE_OFF)
                    FLASH_ON -> listOf(CONTROL_AE_MODE to CONTROL_AE_MODE_ON_ALWAYS_FLASH)
                    FLASH_TORCH -> listOf(CONTROL_AE_MODE to CONTROL_AE_MODE_ON, FLASH_MODE to FLASH_MODE_TORCH)
                    FLASH_AUTO -> listOf(CONTROL_AE_MODE to CONTROL_AE_MODE_ON_AUTO_FLASH)
                    FLASH_RED_EYE -> listOf(CONTROL_AE_MODE to CONTROL_AE_MODE_ON_AUTO_FLASH)
                    else -> emptyList()
                }.forEach { set(it.first, it.second) }
                // Calculate JPEG orientation.
                val sensorOrientation = cameraCharacteristics!![SENSOR_ORIENTATION]!!
                set(JPEG_ORIENTATION,
                    (sensorOrientation + displayOrientation * (if (FACING_FRONT == mFacing) 1 else -1) + DEGREE_360) % DEGREE_360)
                // Stop preview and capture a still picture.
                captureSession!!.apply {
                    stopRepeating()
                    capture(build(), object : CameraCaptureSession.CaptureCallback() {
                        override fun onCaptureCompleted(
                            session: CameraCaptureSession,
                            request: CaptureRequest,
                            result: TotalCaptureResult
                        ) = unlockFocus()
                    }, null)
                }
            }
        }
        catch (e: CameraAccessException) {
            Log.e(TAG, "Cannot capture a still picture.", e)
        }

    }

    /**
     * Unlocks the auto-focus and restart camera preview. This is supposed to be called after
     * capturing a still picture.
     */
    fun unlockFocus() {
        previewRequestBuilder!![CONTROL_AF_TRIGGER] = CONTROL_AF_TRIGGER_CANCEL
        try {
            captureSession!!.capture(previewRequestBuilder!!.build(), captureCallback, null)
            updateAutoFocus()
            updateFlash()
            previewRequestBuilder!![CONTROL_AF_TRIGGER] = CONTROL_AF_TRIGGER_IDLE
            captureSession!!.setRepeatingRequest(previewRequestBuilder!!.build(), captureCallback, null)
            captureCallback.setState(STATE_PREVIEW)
        }
        catch (e: CameraAccessException) {
            Log.e(TAG, "Failed to restart camera preview.", e)
        }
    }

    /**
     *
     * Chooses a camera ID by the specified camera facing ([mFacing]).
     *
     * This rewrites [cameraId], [cameraCharacteristics], and optionally
     * [mFacing].
     */
    private fun chooseCameraIdByFacing(): Boolean {
        try {
            val facing = FACINGS[mFacing]
            val ids = cameraManager.cameraIdList
            // No camera
            if (ids.isEmpty()) throw RuntimeException("No camera available.")

            for (id in ids) {
                val characteristics = cameraManager.getCameraCharacteristics(id)
                val level = characteristics[INFO_SUPPORTED_HARDWARE_LEVEL]

                if (null == level || INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY == level) continue

                val internal = characteristics[LENS_FACING]!!

                if (internal == facing) {
                    cameraId = id
                    cameraCharacteristics = characteristics

                    return true
                }
            }

            // Not found
            cameraId = ids[0]
            cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId!!)
            val level = cameraCharacteristics!![INFO_SUPPORTED_HARDWARE_LEVEL]
            if (null == level || INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY == level) return false

            val internal = cameraCharacteristics!![LENS_FACING]
            for (index in 0 until FACINGS.size()) {
                if (FACINGS.valueAt(index) == internal) {
                    mFacing = FACINGS.keyAt(index)
                    return true
                }
            }
            // The operation can reach here when the only camera device is an external one.
            // We treat it as facing back.
            mFacing = FACING_BACK
            return true
        }
        catch (e: CameraAccessException) {
            throw RuntimeException("Failed to get a list of camera devices", e)
        }
    }

    /**
     * Collects some information from [cameraCharacteristics].
     *
     * This rewrites [previewSizes], [pictureSizes], and optionally, [mAspectRatio].
     */
    private fun collectCameraInfo() {
        val map =
            cameraCharacteristics!![SCALER_STREAM_CONFIGURATION_MAP] ?: throw IllegalStateException("Failed to get configuration map: " + cameraId!!)

        previewSizes.clear()
        for (size in map.getOutputSizes(preview.outputClass)) {
            val width = size.width
            val height = size.height

            if (MAX_PREVIEW_WIDTH >= width && MAX_PREVIEW_HEIGHT >= height) previewSizes.add(Size(width, height))
        }

        pictureSizes.clear()
        collectPictureSizes(pictureSizes, map)

        for (ratio in previewSizes.ratios()) {
            if (!pictureSizes.ratios().contains(ratio)) previewSizes.remove(ratio)
        }
        // HACK(jieyi): 2018/04/25 Check this action.
        if (!previewSizes.ratios().contains(mAspectRatio)) mAspectRatio = previewSizes.ratios().last()
    }

    private fun prepareImageReader() {
        imageReader.takeIf { null != it }?.close()
        // OPTIMIZE(jieyi): 2018/04/25 Hard code here.
        val largest = pictureSizes.sizes(mAspectRatio)?.last() ?: Size(300, 300)
        imageReader = newInstance(largest.width, largest.height, JPEG, /* maxImages */ 2).apply {
            setOnImageAvailableListener(mOnImageAvailableListener, null)
        }
    }

    /**
     * Starts opening a camera device.
     *
     * The result will be processed in [cameraDeviceCallback].
     */
    private fun startOpeningCamera() {
        try {
            cameraManager.openCamera(cameraId!!, cameraDeviceCallback, null)
        }
        catch (e: CameraAccessException) {
            throw RuntimeException("Failed to open camera: " + cameraId!!, e)
        }
        catch (e: SecurityException) {
            throw RuntimeException("You need to open the permission: ", e)
        }
    }

    /**
     * Chooses the optimal preview size based on [previewSizes] and the surface size.
     *
     * @return The picked size for camera preview.
     */
    private fun chooseOptimalSize(): Size {
        val surfaceLonger: Int
        val surfaceShorter: Int
        val surfaceWidth = preview.width
        val surfaceHeight = preview.height

        if (surfaceWidth < surfaceHeight) {
            surfaceLonger = surfaceHeight
            surfaceShorter = surfaceWidth
        }
        else {
            surfaceLonger = surfaceWidth
            surfaceShorter = surfaceHeight
        }

        val candidates = previewSizes.sizes(mAspectRatio)

        // Pick the smallest of those big enough
        if (null != candidates) {
            for (size in candidates) {
                if (size.width >= surfaceLonger && size.height >= surfaceShorter) return size
            }
        }
        // If no size is big enough, pick the largest one.
        return candidates!!.last()
    }

    /**
     * Locks the focus as the first step for a still image capture.
     */
    private fun lockFocus() {
        try {
            // This is how to tell the camera to lock focus.
            previewRequestBuilder!!.set(CONTROL_AF_TRIGGER, CONTROL_AF_TRIGGER_START)
            // Tell captureCallback to wait for the lock.
            captureCallback.setState(STATE_LOCKING)
            captureSession!!.capture(previewRequestBuilder!!.build(), captureCallback, null)
        }
        catch (e: CameraAccessException) {
            Log.e(TAG, "Failed to lock focus.", e)
        }
    }

    /**
     * A [CameraCaptureSession.CaptureCallback] for capturing a still picture.
     */
    abstract class PictureCaptureCallback : CameraCaptureSession.CaptureCallback() {
        companion object {
            const val STATE_PREVIEW = 0
            const val STATE_LOCKING = 1
            const val STATE_LOCKED = 2
            const val STATE_PRECAPTURE = 3
            const val STATE_WAITING = 4
            const val STATE_CAPTURING = 5
        }

        private var state = 0

        fun setState(state: Int) {
            this.state = state
        }

        override fun onCaptureProgressed(
            session: CameraCaptureSession,
            request: CaptureRequest,
            partialResult: CaptureResult
        ) = process(partialResult)

        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) = process(result)

        private fun process(result: CaptureResult) {
            val ae = result[CONTROL_AE_STATE]

            when (state) {
                STATE_LOCKING -> {
                    val af = result[CONTROL_AF_STATE] ?: return

                    if (CONTROL_AF_STATE_FOCUSED_LOCKED == af || CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == af) {
                        if (CONTROL_AE_STATE_CONVERGED == ae) {
                            setState(STATE_CAPTURING)
                            onReady()
                        }
                        else {
                            setState(STATE_LOCKED)
                            onPrecaptureRequired()
                        }
                    }
                }
                STATE_PRECAPTURE -> if (CONTROL_AE_STATE_PRECAPTURE == ae ||
                                        CONTROL_AE_STATE_FLASH_REQUIRED == ae ||
                                        CONTROL_AE_STATE_CONVERGED == ae) {
                    setState(STATE_WAITING)
                }
                STATE_WAITING -> if (CONTROL_AE_STATE_PRECAPTURE != ae) {
                    setState(STATE_CAPTURING)
                    onReady()
                }
            }
        }

        /**
         * Called when it is ready to take a still picture.
         */
        abstract fun onReady()

        /**
         * Called when it is necessary to run the precapture sequence.
         */
        abstract fun onPrecaptureRequired()
    }
}

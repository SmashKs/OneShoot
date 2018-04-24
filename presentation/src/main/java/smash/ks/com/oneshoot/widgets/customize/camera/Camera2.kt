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
import android.graphics.ImageFormat
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
import android.util.Log
import android.util.SparseIntArray
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
import java.util.Arrays

@TargetApi(21)
open class Camera2(callback: Callback?, preview: Preview, context: Context) : CameraViewModule(callback, preview) {
    private val mCameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val mCameraDeviceCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            mCamera = camera
            mCallback?.onCameraOpened()
            startCaptureSession()
        }

        override fun onClosed(camera: CameraDevice) {
            mCallback?.onCameraClosed()
        }

        override fun onDisconnected(camera: CameraDevice) {
            mCamera = null
        }

        override fun onError(camera: CameraDevice, error: Int) {
            Log.e(TAG, "onError: " + camera.id + " (" + error + ")")
            mCamera = null
        }
    }

    private val mSessionCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            if (mCamera == null) {
                return
            }
            mCaptureSession = session
            updateAutoFocus()
            updateFlash()
            try {
                mCaptureSession!!.setRepeatingRequest(mPreviewRequestBuilder!!.build(),
                                                      mCaptureCallback, null)
            }
            catch (e: CameraAccessException) {
                Log.e(TAG, "Failed to start camera preview because it couldn't access camera", e)
            }
            catch (e: IllegalStateException) {
                Log.e(TAG, "Failed to start camera preview.", e)
            }

        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            Log.e(TAG, "Failed to configure capture session.")
        }

        override fun onClosed(session: CameraCaptureSession) {
            if (mCaptureSession != null && mCaptureSession == session) {
                mCaptureSession = null
            }
        }
    }

    var mCaptureCallback: PictureCaptureCallback = object : PictureCaptureCallback() {
        override fun onPrecaptureRequired() {
            mPreviewRequestBuilder!!.set(CONTROL_AE_PRECAPTURE_TRIGGER,
                                         CONTROL_AE_PRECAPTURE_TRIGGER_START)
            setState(Camera2.PictureCaptureCallback.STATE_PRECAPTURE)
            try {
                mCaptureSession!!.capture(mPreviewRequestBuilder!!.build(), this, null)
                mPreviewRequestBuilder!!.set(CONTROL_AE_PRECAPTURE_TRIGGER,
                                             CONTROL_AE_PRECAPTURE_TRIGGER_IDLE)
            }
            catch (e: CameraAccessException) {
                Log.e(TAG, "Failed to run precapture sequence.", e)
            }

        }

        override fun onReady() {
            captureStillPicture()
        }

    }

    private val mOnImageAvailableListener = ImageReader.OnImageAvailableListener { reader ->
        reader.acquireNextImage().use({ image ->
                                          val planes = image.planes
                                          if (planes.isNotEmpty()) {
                                              val buffer = planes[0].buffer
                                              val data = ByteArray(buffer.remaining())
                                              buffer.get(data)
                                              mCallback?.onPictureTaken(data)
                                          }
                                      })
    }

    private var mCameraId: String? = null
    private var mCameraCharacteristics: CameraCharacteristics? = null
    var mCamera: CameraDevice? = null
    var mCaptureSession: CameraCaptureSession? = null
    var mPreviewRequestBuilder: CaptureRequest.Builder? = null
    private var mImageReader: ImageReader? = null
    private val mPreviewSizes = SizeMap()
    private val mPictureSizes = SizeMap()
    private var mFacing: Int = 0
    private var mAspectRatio: AspectRatio = DEFAULT_ASPECT_RATIO
    private var mAutoFocus: Boolean = false

    // Revert
    override var flash: Int = 0
        set(flash) {
            if (this.flash == flash) {
                return
            }
            val saved = this.flash
            field = flash
            if (mPreviewRequestBuilder != null) {
                updateFlash()
                if (mCaptureSession != null) {
                    try {
                        mCaptureSession!!.setRepeatingRequest(mPreviewRequestBuilder!!.build(),
                                                              mCaptureCallback, null)
                    }
                    catch (e: CameraAccessException) {
                        field = saved
                    }

                }
            }
        }

    private var mDisplayOrientation: Int = 0
    override val isCameraOpened: Boolean
        get() = mCamera != null
    override var facing: Int
        get() = mFacing
        set(facing) {
            if (mFacing == facing) {
                return
            }
            mFacing = facing
            if (isCameraOpened) {
                stop()
                start()
            }
        }
    override val supportedAspectRatios: Set<AspectRatio>
        get() = mPreviewSizes.ratios()
    // Revert
    override var autoFocus: Boolean
        get() = mAutoFocus
        set(autoFocus) {
            if (mAutoFocus == autoFocus) {
                return
            }
            mAutoFocus = autoFocus
            if (mPreviewRequestBuilder != null) {
                updateAutoFocus()
                if (mCaptureSession != null) {
                    try {
                        mCaptureSession!!.setRepeatingRequest(mPreviewRequestBuilder!!.build(),
                                                              mCaptureCallback, null)
                    }
                    catch (e: CameraAccessException) {
                        mAutoFocus = !mAutoFocus
                    }

                }
            }
        }

    init {
        mPreview.onSurfaceChanged = { startCaptureSession() }
    }

    override fun start(): Boolean {
        if (!chooseCameraIdByFacing()) {
            return false
        }
        collectCameraInfo()
        prepareImageReader()
        startOpeningCamera()
        return true
    }

    override fun stop() {
        if (mCaptureSession != null) {
            mCaptureSession!!.close()
            mCaptureSession = null
        }
        if (mCamera != null) {
            mCamera!!.close()
            mCamera = null
        }
        if (mImageReader != null) {
            mImageReader!!.close()
            mImageReader = null
        }
    }

    override val aspectRatio: AspectRatio?
        get() = mAspectRatio

    override fun setAspectRatio(ratio: AspectRatio): Boolean {
        if (ratio == mAspectRatio || !mPreviewSizes.ratios().contains(ratio)) {
            // TODO: Better error handling
            return false
        }
        mAspectRatio = ratio
        prepareImageReader()
        if (mCaptureSession != null) {
            mCaptureSession!!.close()
            mCaptureSession = null
            startCaptureSession()
        }
        return true
    }

    override fun takePicture() = if (mAutoFocus) lockFocus() else captureStillPicture()

    override fun setDisplayOrientation(displayOrientation: Int) {
        mDisplayOrientation = displayOrientation
        mPreview.setDisplayOrientation(mDisplayOrientation)
    }

    /**
     *
     * Chooses a camera ID by the specified camera facing ([.mFacing]).
     *
     * This rewrites [.mCameraId], [.mCameraCharacteristics], and optionally
     * [.mFacing].
     */
    private fun chooseCameraIdByFacing(): Boolean {
        try {
            val Facing = _FACINGS.get(mFacing)
            val ids = mCameraManager.cameraIdList
            if (ids.isEmpty()) { // No camera
                throw RuntimeException("No camera available.")
            }
            for (id in ids) {
                val characteristics = mCameraManager.getCameraCharacteristics(id)
                val level = characteristics.get(
                    INFO_SUPPORTED_HARDWARE_LEVEL)
                if (level == null || level == INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                    continue
                }
                val internal =
                    characteristics.get(LENS_FACING) ?: throw NullPointerException("Unexpected state: LENS_FACING null")
                if (internal == Facing) {
                    mCameraId = id
                    mCameraCharacteristics = characteristics
                    return true
                }
            }
            // Not found
            mCameraId = ids[0]
            mCameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraId!!)
            val level = mCameraCharacteristics!!.get(
                INFO_SUPPORTED_HARDWARE_LEVEL)
            if (level == null || level == INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                return false
            }
            val internal =
                mCameraCharacteristics!!.get(LENS_FACING) ?: throw NullPointerException("Unexpected state: LENS_FACING null")
            var i = 0
            val count = _FACINGS.size()
            while (i < count) {
                if (_FACINGS.valueAt(i) == internal) {
                    mFacing = _FACINGS.keyAt(i)
                    return true
                }
                i++
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
     * Collects some information from [.mCameraCharacteristics].
     *
     * This rewrites [.mPreviewSizes], [.mPictureSizes], and optionally,
     * [.mAspectRatio].
     */
    private fun collectCameraInfo() {
        val map = mCameraCharacteristics!!.get(
            SCALER_STREAM_CONFIGURATION_MAP) ?: throw IllegalStateException("Failed to get configuration map: " + mCameraId!!)
        mPreviewSizes.clear()
        for (size in map.getOutputSizes(mPreview.outputClass)) {
            val width = size.width
            val height = size.height
            if (width <= MAX_PREVIEW_WIDTH && height <= MAX_PREVIEW_HEIGHT) {
                mPreviewSizes.add(Size(width, height))
            }
        }
        mPictureSizes.clear()
        collectPictureSizes(mPictureSizes, map)
        for (ratio in mPreviewSizes.ratios()) {
            if (!mPictureSizes.ratios().contains(ratio)) {
                mPreviewSizes.remove(ratio)
            }
        }

        if (!mPreviewSizes.ratios().contains(mAspectRatio)) {
            mAspectRatio = mPreviewSizes.ratios().iterator().next()
        }
    }

    protected open fun collectPictureSizes(sizes: SizeMap, map: StreamConfigurationMap) {
        for (size in map.getOutputSizes(ImageFormat.JPEG)) {
            mPictureSizes.add(Size(size.width, size.height))
        }
    }

    private fun prepareImageReader() {
        if (mImageReader != null) {
            mImageReader!!.close()
        }
        val largest = mPictureSizes.sizes(mAspectRatio)?.last() ?: Size(100, 100)
        mImageReader = ImageReader.newInstance(largest.width, largest.height,
                                               ImageFormat.JPEG, /* maxImages */ 2)
        mImageReader!!.setOnImageAvailableListener(mOnImageAvailableListener, null)
    }

    /**
     *
     * Starts opening a camera device.
     *
     * The result will be processed in [.mCameraDeviceCallback].
     */
    private fun startOpeningCamera() {
        try {
            mCameraManager.openCamera(mCameraId!!, mCameraDeviceCallback, null)
        }
        catch (e: CameraAccessException) {
            throw RuntimeException("Failed to open camera: " + mCameraId!!, e)
        }
    }

    /**
     *
     * Starts a capture session for camera preview.
     *
     * This rewrites [.mPreviewRequestBuilder].
     *
     * The result will be continuously processed in [.mSessionCallback].
     */
    fun startCaptureSession() {
        if (!isCameraOpened || !mPreview.isReady || mImageReader == null) {
            return
        }
        val previewSize = chooseOptimalSize()
        mPreview.setBufferSize(previewSize.width, previewSize.height)
        val surface = mPreview.surface
        try {
            mPreviewRequestBuilder = mCamera!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            mPreviewRequestBuilder!!.addTarget(surface)
            mCamera!!.createCaptureSession(Arrays.asList(surface, mImageReader!!.surface),
                                           mSessionCallback, null)
        }
        catch (e: CameraAccessException) {
            throw RuntimeException("Failed to start camera session")
        }

    }

    /**
     * Chooses the optimal preview size based on [.mPreviewSizes] and the surface size.
     *
     * @return The picked size for camera preview.
     */
    private fun chooseOptimalSize(): Size {
        val surfaceLonger: Int
        val surfaceShorter: Int
        val surfaceWidth = mPreview.width
        val surfaceHeight = mPreview.height
        if (surfaceWidth < surfaceHeight) {
            surfaceLonger = surfaceHeight
            surfaceShorter = surfaceWidth
        }
        else {
            surfaceLonger = surfaceWidth
            surfaceShorter = surfaceHeight
        }
        val candidates = mPreviewSizes.sizes(mAspectRatio)

        // Pick the smallest of those big enough
        if (candidates != null) {
            for (size in candidates) {
                if (size.width >= surfaceLonger && size.height >= surfaceShorter) {
                    return size
                }
            }
        }
        // If no size is big enough, pick the largest one.
        return candidates!!.last()
    }

    /**
     * Updates the  state of auto-focus to [.mAutoFocus].
     */
    fun updateAutoFocus() {
        if (mAutoFocus) {
            val modes = mCameraCharacteristics!!.get(
                CONTROL_AF_AVAILABLE_MODES)
            // Auto focus is not supported
            if (modes == null || modes.isEmpty() ||
                modes.size == 1 && modes[0] == CONTROL_AF_MODE_OFF
            ) {
                mAutoFocus = false
                mPreviewRequestBuilder!!.set(CONTROL_AF_MODE,
                                             CaptureRequest.CONTROL_AF_MODE_OFF)
            }
            else {
                mPreviewRequestBuilder!!.set(CONTROL_AF_MODE,
                                             CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            }
        }
        else {
            mPreviewRequestBuilder!!.set(CONTROL_AF_MODE,
                                         CaptureRequest.CONTROL_AF_MODE_OFF)
        }
    }

    /**
     * Updates the  state of flash to [.mFlash].
     */
    fun updateFlash() {
        when (flash) {
            FLASH_OFF -> {
                mPreviewRequestBuilder!!.set(CONTROL_AE_MODE,
                                             CONTROL_AE_MODE_ON)
                mPreviewRequestBuilder!!.set(FLASH_MODE,
                                             FLASH_MODE_OFF)
            }
            FLASH_ON -> {
                mPreviewRequestBuilder!!.set(CONTROL_AE_MODE,
                                             CONTROL_AE_MODE_ON_ALWAYS_FLASH)
                mPreviewRequestBuilder!!.set(FLASH_MODE,
                                             FLASH_MODE_OFF)
            }
            FLASH_TORCH -> {
                mPreviewRequestBuilder!!.set(CONTROL_AE_MODE,
                                             CONTROL_AE_MODE_ON)
                mPreviewRequestBuilder!!.set(FLASH_MODE,
                                             FLASH_MODE_TORCH)
            }
            FLASH_AUTO -> {
                mPreviewRequestBuilder!!.set(CONTROL_AE_MODE,
                                             CONTROL_AE_MODE_ON_AUTO_FLASH)
                mPreviewRequestBuilder!!.set(FLASH_MODE,
                                             FLASH_MODE_OFF)
            }
            FLASH_RED_EYE -> {
                mPreviewRequestBuilder!!.set(CONTROL_AE_MODE,
                                             CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE)
                mPreviewRequestBuilder!!.set(FLASH_MODE,
                                             FLASH_MODE_OFF)
            }
        }
    }

    /**
     * Locks the focus as the first step for a still image capture.
     */
    private fun lockFocus() {
        mPreviewRequestBuilder!!.set(CONTROL_AF_TRIGGER,
                                     CONTROL_AF_TRIGGER_START)
        try {
            mCaptureCallback.setState(PictureCaptureCallback.STATE_LOCKING)
            mCaptureSession!!.capture(mPreviewRequestBuilder!!.build(), mCaptureCallback, null)
        }
        catch (e: CameraAccessException) {
            Log.e(TAG, "Failed to lock focus.", e)
        }

    }

    /**
     * Captures a still picture.
     */
    fun captureStillPicture() {
        try {
            val captureRequestBuilder = mCamera!!.createCaptureRequest(
                CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureRequestBuilder.addTarget(mImageReader!!.surface)
            captureRequestBuilder.set(CONTROL_AF_MODE,
                                      mPreviewRequestBuilder!!.get(CONTROL_AF_MODE))
            when (flash) {
                FLASH_OFF -> {
                    captureRequestBuilder.set(CONTROL_AE_MODE, CONTROL_AE_MODE_ON)
                    captureRequestBuilder.set(FLASH_MODE, FLASH_MODE_OFF)
                }
                FLASH_ON -> captureRequestBuilder.set(CONTROL_AE_MODE, CONTROL_AE_MODE_ON_ALWAYS_FLASH)
                FLASH_TORCH -> {
                    captureRequestBuilder.set(CONTROL_AE_MODE, CONTROL_AE_MODE_ON)
                    captureRequestBuilder.set(FLASH_MODE,
                                              FLASH_MODE_TORCH)
                }
                FLASH_AUTO -> captureRequestBuilder.set(CONTROL_AE_MODE, CONTROL_AE_MODE_ON_AUTO_FLASH)
                FLASH_RED_EYE -> captureRequestBuilder.set(CONTROL_AE_MODE, CONTROL_AE_MODE_ON_AUTO_FLASH)
            }
            // Calculate JPEG orientation.
            val sensorOrientation = mCameraCharacteristics!!.get(
                SENSOR_ORIENTATION)!!
            captureRequestBuilder.set(JPEG_ORIENTATION,
                                      ((sensorOrientation +
                                        mDisplayOrientation * (if (mFacing == FACING_FRONT) 1 else -1) + 360) % 360))
            // Stop preview and capture a still picture.
            mCaptureSession!!.stopRepeating()
            mCaptureSession!!.capture(captureRequestBuilder.build(),
                                      object : CameraCaptureSession.CaptureCallback() {
                                          override fun onCaptureCompleted(
                                              session: CameraCaptureSession,
                                              request: CaptureRequest,
                                              result: TotalCaptureResult
                                          ) {
                                              unlockFocus()
                                          }
                                      }, null)
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
        mPreviewRequestBuilder!!.set(CONTROL_AF_TRIGGER,
                                     CONTROL_AF_TRIGGER_CANCEL)
        try {
            mCaptureSession!!.capture(mPreviewRequestBuilder!!.build(), mCaptureCallback, null)
            updateAutoFocus()
            updateFlash()
            mPreviewRequestBuilder!!.set(CONTROL_AF_TRIGGER,
                                         CONTROL_AF_TRIGGER_IDLE)
            mCaptureSession!!.setRepeatingRequest(mPreviewRequestBuilder!!.build(), mCaptureCallback, null)
            mCaptureCallback.setState(PictureCaptureCallback.STATE_PREVIEW)
        }
        catch (e: CameraAccessException) {
            Log.e(TAG, "Failed to restart camera preview.", e)
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

        private var mState: Int = 0

        fun setState(state: Int) {
            mState = state
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
            when (mState) {
                STATE_LOCKING -> {
                    val af = result.get(CONTROL_AF_STATE) ?: return
                    if ((af == CONTROL_AF_STATE_FOCUSED_LOCKED || af == CONTROL_AF_STATE_NOT_FOCUSED_LOCKED)) {
                        val ae = result.get(CONTROL_AE_STATE)
                        if (ae == null || ae == CONTROL_AE_STATE_CONVERGED) {
                            setState(STATE_CAPTURING)
                            onReady()
                        }
                        else {
                            setState(STATE_LOCKED)
                            onPrecaptureRequired()
                        }
                    }
                }
                STATE_PRECAPTURE -> {
                    val ae = result.get(CONTROL_AE_STATE)
                    if ((ae == null || ae == CONTROL_AE_STATE_PRECAPTURE ||
                         ae == CONTROL_AE_STATE_FLASH_REQUIRED ||
                         ae == CONTROL_AE_STATE_CONVERGED)
                    ) {
                        setState(STATE_WAITING)
                    }
                }
                STATE_WAITING -> {
                    val ae = result.get(CONTROL_AE_STATE)
                    if (ae == null || ae != CONTROL_AE_STATE_PRECAPTURE) {
                        setState(STATE_CAPTURING)
                        onReady()
                    }
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

    companion object {
        private val _FACINGS by lazy { SparseIntArray() }

        init {
            _FACINGS.put(FACING_BACK, LENS_FACING_BACK)
            _FACINGS.put(FACING_FRONT, LENS_FACING_FRONT)
        }

        private const val TAG = "Camera2"
        /** Max preview width that is guaranteed by Camera2 API */
        private const val MAX_PREVIEW_WIDTH = 1920
        /** Max preview height that is guaranteed by Camera2 API */
        private const val MAX_PREVIEW_HEIGHT = 1080
    }

}
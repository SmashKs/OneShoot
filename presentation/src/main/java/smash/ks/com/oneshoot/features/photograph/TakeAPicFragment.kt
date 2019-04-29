/*
 * Copyright (C) 2019 The Smash Ks Open Project
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

package smash.ks.com.oneshoot.features.photograph

import android.Manifest.permission.CAMERA
import android.animation.AnimatorInflater
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.KeyEvent.KEYCODE_BACK
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.devrapid.dialogbuilder.support.QuickDialogFragment
import com.devrapid.kotlinknifer.displayPixels
import com.devrapid.kotlinknifer.toBitmap
import com.devrapid.kotlinknifer.visible
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.Flash.AUTO
import com.otaliastudios.cameraview.Flash.OFF
import com.otaliastudios.cameraview.Flash.ON
import com.otaliastudios.cameraview.PictureResult
import kotlinx.android.synthetic.main.dialog_fragment_options.view.ib_analyze
import kotlinx.android.synthetic.main.dialog_fragment_options.view.ib_upload
import kotlinx.android.synthetic.main.dialog_fragment_options.view.iv_snippet
import kotlinx.android.synthetic.main.fragment_take_a_pic.cv_camera
import kotlinx.android.synthetic.main.fragment_take_a_pic.ib_flash
import kotlinx.android.synthetic.main.fragment_take_a_pic.iv_preview
import kotlinx.android.synthetic.main.fragment_take_a_pic.sav_selection
import kotlinx.android.synthetic.main.fragment_take_a_pic.v_flash
import kotlinx.android.synthetic.main.merge_bottom_shot_bar.fab_shot
import kotlinx.coroutines.delay
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.sdk25.coroutines.onClick
import smash.ks.com.ext.const.Constant.CAMERA_QUALITY
import smash.ks.com.ext.const.Constant.DEBOUNCE_DELAY
import smash.ks.com.ext.const.DEFAULT_INT
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.bases.AdvFragment
import smash.ks.com.oneshoot.ext.image.glide.loadByAny
import smash.ks.com.oneshoot.ext.resource.gStrings
import smash.ks.com.oneshoot.features.fake.FakeFragment.Parameter.REQUEST_CAMERA_PERMISSION
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt
import kotlinx.android.synthetic.main.dialog_fragment_options.view.ib_close as option_close

class TakeAPicFragment : AdvFragment<PhotographActivity, TakeAPicViewModel>() {
    //region Static parameters
    companion object Parameter {
        // The key name of the fragment initialization parameters.
        const val ARG_IMAGE_DATA = "param image data array"

        private const val DIALOG_FRAGMENT_WIDTH_RATIO = .85f
        private const val DIALOG_FRAGMENT_HEIGHT_RATIO = .65f
    }
    //endregion

    //region *** Private Variable ***
    private lateinit var byteArrayPhoto: ByteArray
    private var selectionDialog: QuickDialogFragment? = null
    private var shotDebounce = false
    private var prevDebounce = false
    private val flashCycle by lazy {
        listOf(OFF to R.drawable.ic_flash_off,
               ON to R.drawable.ic_flash_on,
               AUTO to R.drawable.ic_flash_auto)
    }
    private val cameraCallback by lazy {
        object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                scaleBitmap(result.data)
            }
        }
    }
    private val selectedRectF = object {
        var x = 0
        var y = 0
        var w = 0
        var h = 0
    }
    //endregion

    //region Fragment Lifecycle
    override fun onResume() {
        super.onResume()

        // Request the authority of the camera.
        when {
            checkSelfPermission(parent, CAMERA) == PERMISSION_GRANTED -> cv_camera.open()
            shouldShowRequestPermissionRationale(CAMERA) -> QuickDialogFragment.Builder(this) {
                message = gStrings(R.string.camera_permission_confirmation)
                btnPositiveText = "Ok" to { _ ->
                    requestPermissions(parent, arrayOf(CAMERA), REQUEST_CAMERA_PERMISSION)
                }
                btnNegativeText = "Deny" to { _ ->
                    makeText(parent, gStrings(R.string.camera_permission_not_granted), LENGTH_SHORT).show()
                }
            }.build().show()
            else -> requestPermissions(parent, arrayOf(CAMERA), REQUEST_CAMERA_PERMISSION)
        }
    }

    override fun onPause() {
        super.onPause()

        cv_camera.close()
    }

    override fun onDestroy() {
        super.onDestroy()

        selectionDialog?.takeIf(Fragment::isVisible)?.dismiss()
        selectionDialog = null
        cv_camera?.let(CameraView::destroy)
    }
    //endregion

    //region Base Fragment
    override fun rendered(savedInstanceState: Bundle?) {
        cv_camera.apply {
            setLifecycleOwner(this@TakeAPicFragment)
            clearCameraListeners()
            addCameraListener(cameraCallback)
        }
        fab_shot.setOnClickListener {
            if (!shotDebounce) {
                shotDebounce = true
                cv_camera.takePicture()
                makeCameraFlashEffecting()
            }
        }
        ib_flash.apply {
            currentFlashState()?.second?.let(::setImageResource)
            setOnClickListener {
                val state = nextFlashState()

                cv_camera.flash = state.first
                ib_flash.setImageResource(state.second)
            }
        }
        iv_preview.apply {
            setOnClickListener {
                if (!prevDebounce) {
                    prevDebounce = true
                    showSelectionDialog(drawable.toBitmap())
                }
            }
        }
        sav_selection.selectedAreaCallback = { x, y, w, h ->
            selectedRectF.x = x
            selectedRectF.y = y
            selectedRectF.w = w
            selectedRectF.h = h
        }
    }

    override fun provideInflateView() = R.layout.fragment_take_a_pic
    //endregion

    //region Showing From ViewModel
    private fun showSelectionDialog(bitmap: Bitmap) {
        selectionDialog = QuickDialogFragment.Builder(this) {
            var debouncing = false

            viewResCustom = R.layout.dialog_fragment_options
            cancelable = false
            onStartBlock = {
                val (width, height) = requireNotNull(it.activity?.displayPixels())
                val realWidth = width * DIALOG_FRAGMENT_WIDTH_RATIO
                val realHeight = height * DIALOG_FRAGMENT_HEIGHT_RATIO
                it.dialog?.window?.apply {
                    setWindowAnimations(R.style.KsDialog)
                    setLayout(realWidth.roundToInt(), realHeight.roundToInt())
                }
            }
            fetchComponents = { v, df ->
                fun navigateTo(@IdRes navigationAction: Int) {
                    dismissOptionDialog()
                    view?.findNavController()?.navigate(navigationAction, bundleOf(ARG_IMAGE_DATA to byteArrayPhoto))
                }

                v.apply {
                    iv_snippet.loadByAny(bitmap)
                    option_close.onClick {
                        if (false == debouncing) {
                            debouncing = true
                            delay(DEBOUNCE_DELAY)
                            dismissOptionDialog()
                        }
                    }
                    ib_analyze.onClick { navigateTo(R.id.action_takeAPicFragment_to_analyzeFragment) }
                    ib_upload.onClick { navigateTo(R.id.action_takeAPicFragment_to_uploadPicFragment) }
                }

                // For touch the back press key then close the dialog fragment.
                df.dialog?.setOnKeyListener { _, keyCode, _ ->
                    when (keyCode) {
                        KEYCODE_BACK -> {
                            dismissOptionDialog()
                            true
                        }
                        else -> false
                    }
                }

                // Open the dialog well then the debounce can re-trigger again.
                shotDebounce = false
                prevDebounce = false
            }
        }.build()

        selectionDialog?.takeUnless(Fragment::isVisible)?.show()
    }

    private fun dismissOptionDialog() {
        selectionDialog?.dismiss()
        selectionDialog = null
    }
    //endregion

    //region Camera Effective
    private fun makeCameraFlashEffecting() {
        if (!v_flash.isVisible) v_flash.visible()
        AnimatorInflater.loadAnimator(requireContext(), R.animator.camera_flash)
            .apply { setTarget(v_flash) }
            .start()
    }

    private fun currentFlashState() = flashCycle.find { cv_camera.flash == it.first }

    private fun currentFlashStateIndex(): Int {
        var currentIndex = DEFAULT_INT

        flashCycle.forEachWithIndex { index, flash ->
            if (cv_camera.flash == flash.first) currentIndex = index
        }

        return currentIndex
    }

    private fun nextFlashState() = flashCycle[(currentFlashStateIndex() + 1) % flashCycle.size]
    //endregion

    private fun scaleBitmap(data: ByteArray) {
        BitmapFactory.decodeByteArray(data, 0, data.size).also { bmp ->
            selectedRectF.apply {
                // Round the x, y, width, and height for avoiding the range is over than bitmap size.
                var roundWidth = (x + w).let { if (it > bmp.width) bmp.width - x else w }
                var roundHeight = (y + h).let { if (it > bmp.height) bmp.height - y else h }
                val roundX = x.takeIf { 0 < it } ?: let { roundWidth = w + x; 0 }
                val roundY = y.takeIf { 0 < it } ?: let { roundHeight = h + y; 0 }

                val ratioX = bmp.width.toFloat() / cv_camera.width
                val ratioY = bmp.height.toFloat() / cv_camera.height

                val bitmap = Bitmap.createBitmap(bmp,
                                                 (roundX * ratioX).toInt(),
                                                 (roundY * ratioY).toInt(),
                                                 (roundWidth * ratioX).toInt(),
                                                 (roundHeight * ratioY).toInt())

                // Cropped bitmap translates to byte array again.
                val stream = ByteArrayOutputStream()
                bitmap.compress(JPEG, CAMERA_QUALITY, stream)
                byteArrayPhoto = stream.toByteArray()
                // Let image view and dialog show the bitmap.
                iv_preview.loadByAny(bitmap)
                showSelectionDialog(bitmap)
            }
        }.recycle()
    }
}

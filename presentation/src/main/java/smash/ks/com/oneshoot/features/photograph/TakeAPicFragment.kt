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

package smash.ks.com.oneshoot.features.photograph

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import com.devrapid.dialogbuilder.support.QuickDialogFragment
import com.devrapid.kotlinknifer.logw
import com.ks.smash.ext.const.DEFAULT_INT
import kotlinx.android.synthetic.main.fragment_take_a_pic.cv_camera
import kotlinx.android.synthetic.main.fragment_take_a_pic.ib_shot
import kotlinx.android.synthetic.main.fragment_take_a_pic.iv_preview
import kotlinx.android.synthetic.main.fragment_take_a_pic.sav_selection
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.sdk25.coroutines.onClick
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.bases.AdvFragment
import smash.ks.com.oneshoot.bases.LoadView
import smash.ks.com.oneshoot.ext.resource.gStrings
import smash.ks.com.oneshoot.ext.stubview.hideLoadingView
import smash.ks.com.oneshoot.ext.stubview.hideRetryView
import smash.ks.com.oneshoot.ext.stubview.showErrorView
import smash.ks.com.oneshoot.ext.stubview.showLoadingView
import smash.ks.com.oneshoot.ext.stubview.showRetryView
import smash.ks.com.oneshoot.features.fake.FakeFragment.Factory.REQUEST_CAMERA_PERMISSION
import smash.ks.com.oneshoot.widgets.customize.camera.view.CameraView

class TakeAPicFragment : AdvFragment<PhotographActivity, TakeAPicViewModel>(), LoadView {
    //region Instance
    companion object Factory {
        // The key name of the fragment initialization parameters.
        const val ARG_RANDOM_ID = "param random image id"

        /**
         * Use this factory method to create a new instance of this fragment using the provided parameters.
         *
         * @return A new instance of fragment [TakeAPicFragment].
         */
        fun newInstance(arg1: Int = DEFAULT_INT) = TakeAPicFragment().apply {
            arguments = bundleOf(ARG_RANDOM_ID to arg1)
        }
    }
    //endregion

    private val cameraCallback by lazy {
        object : CameraView.Callback() {
            override fun onPictureTaken(cameraView: CameraView, data: ByteArray) {
                BitmapFactory.decodeByteArray(data, 0, data.size).also { bmp ->
                    selectedRectF.apply {
                        // Round the x, y, width, and height for avoiding the range is over than bitmap size.
                        var roundWidth = (x + w).let { if (it > bmp.width) bmp.width - x else w }
                        var roundHeight = (y + h).let { if (it > bmp.height) bmp.height - y else h }
                        val roundX = x.takeIf { 0 < it } ?: let { roundWidth = w + x; 0 }
                        val roundY = y.takeIf { 0 < it } ?: let { roundHeight = h + y; 0 }

                        iv_preview.imageBitmap = Bitmap.createBitmap(bmp, roundX, roundY, roundWidth, roundHeight)
                    }
                }.recycle()
            }
        }
    }
    private val selectedRectF = object {
        var x = 0
        var y = 0
        var w = 0
        var h = 0
    }
    // The fragment initialization parameters.
    private val randomId by lazy { arguments?.getInt(ARG_RANDOM_ID) ?: DEFAULT_INT }

    //region Fragment Lifecycle
    override fun onResume() {
        super.onResume()

        logw(randomId)

        when {
            checkSelfPermission(parent, CAMERA) == PERMISSION_GRANTED -> cv_camera.start()
            shouldShowRequestPermissionRationale(parent, CAMERA) -> QuickDialogFragment.Builder(this) {
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
        cv_camera.stop()
    }
    //endregion

    //region Base Fragment
    override fun rendered(savedInstanceState: Bundle?) {
        cv_camera.addCallback(cameraCallback)
        ib_shot.onClick { cv_camera.takePicture() }
        sav_selection.selectedAreaCallback = { x, y, w, h ->
            selectedRectF.x = x
            selectedRectF.y = y
            selectedRectF.w = w
            selectedRectF.h = h
        }
    }

    override fun provideInflateView() = R.layout.fragment_take_a_pic
    //endregion

    //region View Implementation for the Presenter.
    override fun showLoading() = parent.showLoadingView()

    override fun hideLoading() = parent.hideLoadingView()

    override fun showRetry() = parent.showRetryView()

    override fun hideRetry() = parent.hideRetryView()

    override fun showError(message: String) = parent.showErrorView(message)
    //endregion
}

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
import android.graphics.Bitmap.CompressFormat.PNG
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.graphics.scale
import androidx.recyclerview.widget.LinearLayoutManager
import com.devrapid.dialogbuilder.support.QuickDialogFragment
import com.devrapid.kotlinknifer.dp
import com.devrapid.kotlinknifer.ui
import kotlinx.android.synthetic.main.dialog_fragment_labels.view.ib_close
import kotlinx.android.synthetic.main.dialog_fragment_labels.view.rv_labels
import kotlinx.android.synthetic.main.fragment_take_a_pic.cv_camera
import kotlinx.android.synthetic.main.fragment_take_a_pic.ib_shot
import kotlinx.android.synthetic.main.fragment_take_a_pic.iv_preview
import kotlinx.android.synthetic.main.fragment_take_a_pic.sav_selection
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.kodein.di.generic.instance
import smash.ks.com.domain.models.KsResponse
import smash.ks.com.ext.cast
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.bases.AdvFragment
import smash.ks.com.oneshoot.ext.aac.observeNonNull
import smash.ks.com.oneshoot.ext.resource.gStrings
import smash.ks.com.oneshoot.features.fake.FakeFragment.Factory.REQUEST_CAMERA_PERMISSION
import smash.ks.com.oneshoot.internal.di.tag.ObjectLabel.LABEL_ADAPTER
import smash.ks.com.oneshoot.internal.di.tag.ObjectLabel.LINEAR_LAYOUT_VERTICAL
import smash.ks.com.oneshoot.widgets.customize.camera.view.CameraView
import smash.ks.com.oneshoot.widgets.recyclerview.MultiTypeAdapter
import smash.ks.com.oneshoot.widgets.recyclerview.RVAdapterAny
import smash.ks.com.oneshoot.widgets.recyclerview.decorator.VerticalItemDecorator
import java.io.ByteArrayOutputStream

class TakeAPicFragment : AdvFragment<PhotographActivity, TakeAPicViewModel>() {
    //region Instance
    companion object Factory {
        private const val INPUT_SIZE = 224

        /**
         * Use this factory method to create a new instance of this fragment using the provided parameters.
         *
         * @return A new instance of fragment [TakeAPicFragment].
         */
        fun newInstance() = TakeAPicFragment().apply { arguments = bundleOf() }
    }

    //endregion
    private val linearLayoutManager by instance<LinearLayoutManager>(LINEAR_LAYOUT_VERTICAL)
    private val adapter by lazy {
        val innerAdapter by instance<RVAdapterAny>(LABEL_ADAPTER)

        cast<MultiTypeAdapter>(innerAdapter)
    }
    private val decorator by lazy { VerticalItemDecorator(8.dp.toInt(), 8.dp.toInt()) }
    private val cameraCallback by lazy {
        object : CameraView.Callback() {
            override fun onPictureTaken(cameraView: CameraView, data: ByteArray) {
                var byteArray = byteArrayOf()

                BitmapFactory.decodeByteArray(data, 0, data.size).also { bmp ->
                    selectedRectF.apply {
                        // Round the x, y, width, and height for avoiding the range is over than bitmap size.
                        var roundWidth = (x + w).let { if (it > bmp.width) bmp.width - x else w }
                        var roundHeight = (y + h).let { if (it > bmp.height) bmp.height - y else h }
                        val roundX = x.takeIf { 0 < it } ?: let { roundWidth = w + x; 0 }
                        val roundY = y.takeIf { 0 < it } ?: let { roundHeight = h + y; 0 }
                        val bitmap = Bitmap.createBitmap(bmp, roundX, roundY, roundWidth, roundHeight)

                        // Show the image into the view.
                        iv_preview.imageBitmap = bitmap
                        bitmap.scale(INPUT_SIZE, INPUT_SIZE, false).apply {
                            val stream = ByteArrayOutputStream()
                            compress(PNG, 100, stream)
                            byteArray = stream.toByteArray()
                        }.recycle()

                        ui { vm.analyzeImage(byteArray) }
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

    //region Fragment Lifecycle
    override fun onResume() {
        super.onResume()

        // Request the authority of the camera.
        when {
            checkSelfPermission(parent, CAMERA) == PERMISSION_GRANTED -> cv_camera.start()
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

        this@TakeAPicFragment.observeNonNull(vm.labels) {
            if (it is KsResponse.Success) {
                QuickDialogFragment.Builder(this@TakeAPicFragment) {
                    viewResCustom = R.layout.dialog_fragment_labels
                    cancelable = false
                    fetchComponents = { v, df ->
                        v.apply {
                            rv_labels.also {
                                it.layoutManager = linearLayoutManager
                                it.adapter = adapter
                                it.addItemDecoration(decorator)
                            }
                            ib_close.setOnClickListener {
                                adapter.clearList()

                                rv_labels.apply {
                                    layoutManager = null
                                    adapter = null
                                    removeItemDecoration(decorator)
                                }

                                df.dismiss()
                            }
                        }

                        // Transforming the data into [KsMultiVisitable] type.
                        adapter.appendList(it.data!!.toMutableList())
                    }
                }.build().show()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        cv_camera.stop()
    }
    //endregion

    //region Base Fragment
    override fun rendered(savedInstanceState: Bundle?) {
        if (!cv_camera.hasCallback(cameraCallback)) {
            cv_camera.addCallback(cameraCallback)
        }
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
}

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
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.graphics.scale
import com.devrapid.dialogbuilder.support.QuickDialogFragment
import com.devrapid.kotlinknifer.logw
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.find
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.sdk25.coroutines.onClick
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.R.id.cv_camera
import smash.ks.com.oneshoot.R.id.ib_shot
import smash.ks.com.oneshoot.R.id.iv_preview
import smash.ks.com.oneshoot.R.id.sav_selection
import smash.ks.com.oneshoot.bases.AdvFragment
import smash.ks.com.oneshoot.ext.aac.observeNonNull
import smash.ks.com.oneshoot.ext.resource.gStrings
import smash.ks.com.oneshoot.features.fake.FakeFragment.Factory.REQUEST_CAMERA_PERMISSION
import smash.ks.com.oneshoot.widgets.customize.camera.view.CameraView
import smash.ks.com.oneshoot.widgets.customize.selectable.SelectableAreaView
import java.io.ByteArrayOutputStream

class TakeAPicFragment : AdvFragment<PhotographActivity, TakeAPicViewModel>() {
    //region Instance
    companion object Factory {
        private const val MODEL_FILE = "mobilenet_quant_v1_224.tflite"
        private const val LABEL_FILE = "labels.txt"
        private const val INPUT_SIZE = 224

        /**
         * Use this factory method to create a new instance of this fragment using the provided parameters.
         *
         * @return A new instance of fragment [TakeAPicFragment].
         */
        fun newInstance() = TakeAPicFragment().apply { arguments = bundleOf() }
    }
    //endregion

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
                        view?.find<ImageView>(iv_preview)?.imageBitmap = bitmap

                        //region ORIGINAL ML CODE
//                        // Tensorflow Lite.
//                        val croppedBitmap = bitmap.scale(INPUT_SIZE, INPUT_SIZE, false)
//                        val classifier =
//                            TFLiteImageClassifier.create(activity!!.assets, MODEL_FILE, LABEL_FILE, INPUT_SIZE)
//
////                        val stream = ByteArrayOutputStream()
////                        val a = croppedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
////                        val straming = stream.toByteArray()
//
//                        launch {
//                            val time = measureTimeMillis {
//                                val results = classifier.recognizeImage(croppedBitmap)
//                                logv("Detect: $results")
//                            }
//                            logw(time)
//                        }
//
//                        // Firebase ML Kit.
//                        val textImage = FirebaseVisionImage.fromBitmap(croppedBitmap)
//                        val detector = FirebaseVision.getInstance().visionLabelDetector
//
//                        detector.detectInImage(textImage).addOnCompleteListener {
//                            it.result.forEach {
//                                logw("[${it.entityId}]${it.label}: ${it.confidence * 100}%")
//                            }
//                        }
//
//                        croppedBitmap.recycle()
                        //endregion

                        bitmap.scale(INPUT_SIZE, INPUT_SIZE, false).apply {
                            val stream = ByteArrayOutputStream()
                            compress(Bitmap.CompressFormat.PNG, 100, stream)
                            byteArray = stream.toByteArray()
                        }.recycle()

                        this@TakeAPicFragment.observeNonNull(vm.analyzeImage(byteArray)) {
                            logw(it.data)
                        }
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

        when {
            checkSelfPermission(parent, CAMERA) == PERMISSION_GRANTED -> view?.find<CameraView>(cv_camera)?.start()
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
        view?.find<CameraView>(cv_camera)?.stop()
    }
    //endregion

    //region Base Fragment
    override fun rendered(savedInstanceState: Bundle?) {
        view?.find<CameraView>(cv_camera)?.addCallback(cameraCallback)
        view?.find<ImageButton>(ib_shot)?.onClick { view?.find<CameraView>(cv_camera)?.takePicture() }
        view?.find<SelectableAreaView>(sav_selection)?.selectedAreaCallback = { x, y, w, h ->
            selectedRectF.x = x
            selectedRectF.y = y
            selectedRectF.w = w
            selectedRectF.h = h
        }
    }

    override fun provideInflateView() = R.layout.fragment_take_a_pic
    //endregion
}

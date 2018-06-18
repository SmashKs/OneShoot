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

package smash.ks.com.oneshoot.features.fake

import android.os.Bundle
import android.os.SystemClock.uptimeMillis
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.devrapid.kotlinknifer.logi
import com.devrapid.kotlinknifer.toBitmap
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.find
import org.kodein.di.generic.instance
import smash.ks.com.domain.datas.KsResponse
import smash.ks.com.ext.const.DEFAULT_INT
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.bases.AdvFragment
import smash.ks.com.oneshoot.bases.LoadView
import smash.ks.com.oneshoot.classifiers.TFLiteImageClassifier.create
import smash.ks.com.oneshoot.ext.aac.observeNonNull
import smash.ks.com.oneshoot.ext.aac.peelResponse
import smash.ks.com.oneshoot.ext.stubview.hideLoadingView
import smash.ks.com.oneshoot.ext.stubview.hideRetryView
import smash.ks.com.oneshoot.ext.stubview.showErrorView
import smash.ks.com.oneshoot.ext.stubview.showLoadingView
import smash.ks.com.oneshoot.ext.stubview.showRetryView
import smash.ks.com.oneshoot.internal.di.tag.ObjectLabel.KS_ADAPTER
import smash.ks.com.oneshoot.internal.di.tag.ObjectLabel.LINEAR_LAYOUT_VERTICAL

class FakeFragment : AdvFragment<FakeActivity, FakeViewModel>(), LoadView {
    //region Instance
    companion object Factory {
        const val REQUEST_CAMERA_PERMISSION = 1
        // The key name of the fragment initialization parameters.
        const val ARG_RANDOM_ID = "param random image id"
        private const val MODEL_FILE = "mobilenet_quant_v1_224.tflite"
        private const val LABEL_FILE = "labels.txt"
        private const val INPUT_SIZE = 3224

        /**
         * Use this factory method to create a new instance of this fragment using the provided parameters.
         *
         * @return A new instance of fragment [FakeFragment].
         */
        fun newInstance(arg1: Int = DEFAULT_INT) = FakeFragment().apply {
            arguments = bundleOf(ARG_RANDOM_ID to arg1)
        }
    }
    //endregion

    private val linearLayoutManager by instance<LinearLayoutManager>(LINEAR_LAYOUT_VERTICAL)
    private val adapter by instance<RecyclerView.Adapter<*>>(KS_ADAPTER)
    // The fragment initialization parameters.
    private val randomId by lazy { arguments?.getInt(ARG_RANDOM_ID) ?: DEFAULT_INT }

    //region Base Fragment
    override fun rendered(savedInstanceState: Bundle?) {
        vm.apply {
            // For testing, that's why they are called in the beginning.
            observeNonNull(retrieveId(randomId), ::updateTemp)
//            observe(storeImage())
        }

        find<RecyclerView>(R.id.rv_fake).also {
            // rv_fake.also {
            it.layoutManager = linearLayoutManager
            it.adapter = adapter
        }

        // Machine learning.
        val classifier = create(act.assets, MODEL_FILE, LABEL_FILE, INPUT_SIZE)
        val croppedBitmap = R.drawable.basketball.toBitmap(act)
        var lastProcessingTimeMs = 0L

        launch {
            val startTime = uptimeMillis()
            val results = classifier.recognizeImage(croppedBitmap)
            lastProcessingTimeMs = uptimeMillis() - startTime
            logi("Detect: ", results)
        }

    }

    override fun provideInflateView() = R.layout.fragment_fake
    //endregion

    //region View Implementation for the Presenter.
    override fun showLoading() = parent.showLoadingView()

    override fun hideLoading() = parent.hideLoadingView()

    override fun showRetry() = parent.showRetryView()

    override fun hideRetry() = parent.hideRetryView()

    override fun showError(message: String) = parent.showErrorView(message)
    //endregion

    //region Presenter Implementation.
    private fun updateTemp(response: KsResponse<String>) {
        peelResponse(response, this@FakeFragment::showImageUri)
    }

    private fun showImageUri(uri: String) {
        find<TextView>(R.id.tv_label).text = uri
//        tv_label.text = uri
    }
    //endregion
}

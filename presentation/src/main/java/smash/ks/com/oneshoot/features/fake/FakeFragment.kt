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
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.ks.smash.ext.const.DEFAULT_INT
import kotlinx.android.synthetic.main.fragment_main.rv_fake
import kotlinx.android.synthetic.main.fragment_main.tv_label
import org.jetbrains.anko.bundleOf
import org.kodein.di.generic.instance
import smash.ks.com.domain.objects.KsResponse
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.bases.AdvFragment
import smash.ks.com.oneshoot.bases.LoadView
import smash.ks.com.oneshoot.ext.aac.observe
import smash.ks.com.oneshoot.ext.aac.responseReaction
import smash.ks.com.oneshoot.ext.stubview.hideLoadingView
import smash.ks.com.oneshoot.ext.stubview.hideRetryView
import smash.ks.com.oneshoot.ext.stubview.showErrorView
import smash.ks.com.oneshoot.ext.stubview.showLoadingView
import smash.ks.com.oneshoot.ext.stubview.showRetryView
import smash.ks.com.oneshoot.widgets.recyclerview.KsMultiVisitable
import smash.ks.com.oneshoot.widgets.recyclerview.MultiTypeAdapter

class FakeFragment : AdvFragment<FakeActivity, FakeViewModel>(), LoadView {
    //region Instance
    companion object Factory {
        const val REQUEST_CAMERA_PERMISSION = 1
        // The key name of the fragment initialization parameters.
        const val ARG_RANDOM_ID = "param random image id"

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

    private val list by instance<MutableList<KsMultiVisitable>>("ks entity")
    // The fragment initialization parameters.
    private val randomId by lazy { arguments?.getInt(ARG_RANDOM_ID) ?: DEFAULT_INT }

    //region Base Fragment
    override fun rendered(savedInstanceState: Bundle?) {
        vm.apply {
            observe(temp, ::updateTemp)

            retrieveId(randomId)
        }

        rv_fake.apply {
            layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            adapter = MultiTypeAdapter(list, context)
        }
    }

    override fun provideInflateView() = R.layout.fragment_main
    //endregion

    //region View Implementation for the Presenter.
    override fun showLoading() = parent.showLoadingView()

    override fun hideLoading() = parent.hideLoadingView()

    override fun showRetry() = parent.showRetryView()

    override fun hideRetry() = parent.hideRetryView()

    override fun showError(message: String) = parent.showErrorView(message)
    //endregion

    //region Presenter Implementation.
    private fun updateTemp(response: KsResponse?) {
        responseReaction(response) { showImageUri(it.data as String) }
    }

    private fun showImageUri(uri: String) {
        tv_label.text = uri
    }
    //endregion
}
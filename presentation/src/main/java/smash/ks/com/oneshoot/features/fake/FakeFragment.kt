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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_fake.btn_append
import kotlinx.android.synthetic.main.fragment_fake.rv_fake
import kotlinx.android.synthetic.main.fragment_fake.tv_label
import org.jetbrains.anko.bundleOf
import org.kodein.di.generic.instance
import smash.ks.com.domain.models.KsResponse
import smash.ks.com.ext.cast
import smash.ks.com.ext.const.DEFAULT_INT
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.bases.AdvFragment
import smash.ks.com.oneshoot.entities.KsEntity
import smash.ks.com.oneshoot.ext.aac.observeNonNull
import smash.ks.com.oneshoot.ext.aac.peelResponse
import smash.ks.com.oneshoot.internal.di.tag.ObjectLabel.KS_ADAPTER
import smash.ks.com.oneshoot.internal.di.tag.ObjectLabel.LINEAR_LAYOUT_VERTICAL
import smash.ks.com.oneshoot.widgets.recyclerview.MultiTypeAdapter
import smash.ks.com.oneshoot.widgets.recyclerview.RVAdapterAny

class FakeFragment : AdvFragment<FakeActivity, FakeViewModel>() {
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

    private val linearLayoutManager by instance<LinearLayoutManager>(LINEAR_LAYOUT_VERTICAL)
    private val adapter by lazy {
        val innerAdapter by instance<RVAdapterAny>(KS_ADAPTER)

        cast<MultiTypeAdapter>(innerAdapter)
    }
    // The fragment initialization parameters.
    private val randomId by lazy { arguments?.getInt(ARG_RANDOM_ID) ?: DEFAULT_INT }

    //region Base Fragment
    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    override fun bindLiveData() {
        vm.apply {
            // For testing, that's why they are called in the beginning.
            observeNonNull(retrieveId(randomId), ::updateTemp)
//            observe(storeImage())
        }
    }

    override fun rendered(savedInstanceState: Bundle?) {
        rv_fake.also {
            it.layoutManager = linearLayoutManager
            it.adapter = adapter
        }
        btn_append.setOnClickListener {
            adapter.appendList(mutableListOf(KsEntity()))
        }
    }

    override fun provideInflateView() = R.layout.fragment_fake
    //endregion

    //region Presenter Implementation.
    private fun updateTemp(response: KsResponse<String>) {
        peelResponse(response, this@FakeFragment::showImageUri)
    }

    private fun showImageUri(uri: String) {
        tv_label.text = uri
    }
    //endregion
}

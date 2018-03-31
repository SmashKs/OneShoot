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

package smash.ks.com.oneshoot.features.main

import android.os.Bundle
import com.devrapid.kotlinknifer.invisiable
import com.devrapid.kotlinknifer.visiable
import com.devrapid.kotlinshaver.isNotNull
import com.ks.smash.ext.const.DEFAULT_INT
import kotlinx.android.synthetic.main.fragment_main.btn_click
import kotlinx.android.synthetic.main.fragment_main.vs_loading
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.find
import org.kodein.generic.instance
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.bases.MvpFragment
import smash.ks.com.oneshoot.ext.coroutine.ui
import smash.ks.com.oneshoot.mvp.contracts.MainContract.Presenter
import smash.ks.com.oneshoot.mvp.contracts.MainContract.View

class MainFragment : MvpFragment<View, Presenter>(), View {
    //region Instance
    companion object Factory {
        // The key name of the fragment initialization parameters.
        const val ARG_RANDOM_ID = "param random image id"

        /**
         * Use this factory method to create a new instance of this fragment using the provided parameters.
         *
         * @return A new instance of fragment [MainFragment].
         */
        fun newInstance(arg1: Int = DEFAULT_INT) = MainFragment().apply {
            arguments = bundleOf(ARG_RANDOM_ID to arg1)
        }
    }
    //endregion

    override val presenter by instance<Presenter>()
    // The fragment initialization parameters.
    private val randomId by lazy { arguments?.getInt(ARG_RANDOM_ID) ?: DEFAULT_INT }

    //region Base Fragment
    override fun rendered(savedInstanceState: Bundle?) {
        presenter.obtainImageUri(randomId)
        btn_click.setOnClickListener {
            showLoading()
            launch(CommonPool) {
                delay(1000)
                ui { hideLoading() }
            }
        }
    }

    override fun provideCurrentFragmentView() = this

    override fun provideInflateView() = R.layout.fragment_main
    //endregion

    //region View Implementation for the Presenter.
    override fun showLoading() {
        if (vs_loading.isNotNull()) vs_loading.visiable() else find<android.view.View>(R.id.v_loading).visiable()
    }

    override fun hideLoading() {
        find<android.view.View>(R.id.v_loading).invisiable()
    }

    override fun showRetry() {
    }

    override fun hideRetry() {
    }

    override fun showError(message: String) {
    }
    //endregion

    //region Presenter Implementation.
    override fun showImageUri(uri: String) {
//        tv_label.text = uri
    }
    //endregion
}
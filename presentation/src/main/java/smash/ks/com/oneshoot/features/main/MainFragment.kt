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
import com.ks.smash.ext.const.DEFAULT_STR
import org.jetbrains.anko.bundleOf
import smash.ks.com.oneshoot.bases.MvpFragment
import smash.ks.com.oneshoot.mvp.contracts.MainContract.Presenter
import smash.ks.com.oneshoot.mvp.contracts.MainContract.View

class MainFragment : MvpFragment<View, Presenter>() {
    //region Instance
    companion object Factory {
        // The key name of the fragment initialization parameters.
        private val ARG_PARAM_ = "param_"

        /**
         * Use this factory method to create a new instance of this fragment using the provided parameters.
         *
         * @return A new instance of fragment [MainFragment].
         */
        fun newInstance(arg1: Any = DEFAULT_STR) = MainFragment().apply {
            arguments = bundleOf(ARG_PARAM_ to arg1)
        }
    }
    //endregion

    //@Inject
    override lateinit var presenter: Presenter
    // The fragment initialization parameters.
    private val arg1 by lazy { arguments?.getString(ARG_PARAM_) as Any }

    override fun provideCurrentFragmentView() = TODO()

    override fun rendered(savedInstanceState: Bundle?) {
    }

    override fun provideInflateView() = TODO()
}
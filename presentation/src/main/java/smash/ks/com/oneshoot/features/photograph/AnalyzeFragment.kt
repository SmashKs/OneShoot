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

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.UiThread
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.bases.AdvFragment

class AnalyzeFragment : AdvFragment<PhotographActivity, AnalyzeViewModel>() {
    //region Static parameters
    companion object Parameter {
        // The key name of the fragment initialization parameters.
        const val ARG_RANDOM_ID = "param random image id"
    }
    //endregion

    //region *** Private Variable ***
    //endregion

    //region Base Fragment
    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    @UiThread
    override fun bindLiveData() {
        vm.apply {
        }
    }

    @UiThread
    override fun rendered(savedInstanceState: Bundle?) {
    }

    @LayoutRes
    override fun provideInflateView() = R.layout.fragment_analyze_pic
    //endregion
}

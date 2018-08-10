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
import androidx.recyclerview.widget.LinearLayoutManager
import com.devrapid.kotlinknifer.getDisplayMetrics
import com.devrapid.kotlinknifer.gone
import com.devrapid.kotlinknifer.resizeView
import com.devrapid.kotlinknifer.statusBarHeight
import com.devrapid.kotlinknifer.visible
import com.devrapid.kotlinshaver.cast
import com.devrapid.kotlinshaver.isNull
import kotlinx.android.synthetic.main.fragment_analyze_pic.abl_main
import kotlinx.android.synthetic.main.fragment_analyze_pic.fab_upload
import kotlinx.android.synthetic.main.fragment_analyze_pic.iv_backdrop
import kotlinx.android.synthetic.main.fragment_analyze_pic.rv_analyzed
import kotlinx.android.synthetic.main.merge_recycler_item_empty.fl_empty
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.kodein.di.generic.instance
import smash.ks.com.domain.models.response.KsResponse
import smash.ks.com.ext.const.DEFAULT_INT
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.bases.AdvFragment
import smash.ks.com.oneshoot.entities.LabelEntities
import smash.ks.com.oneshoot.ext.aac.observeNonNull
import smash.ks.com.oneshoot.ext.aac.peelResponse
import smash.ks.com.oneshoot.ext.image.glide.loadByAny
import smash.ks.com.oneshoot.ext.resource.gDimens
import smash.ks.com.oneshoot.features.photograph.TakeAPicFragment.Parameter.ARG_IMAGE_DATA
import smash.ks.com.oneshoot.internal.di.tag.ObjectLabel.LABEL_ADAPTER
import smash.ks.com.oneshoot.internal.di.tag.ObjectLabel.LINEAR_LAYOUT_VERTICAL
import smash.ks.com.oneshoot.widgets.recyclerview.MultiTypeAdapter
import smash.ks.com.oneshoot.widgets.recyclerview.RVAdapterAny
import smash.ks.com.oneshoot.widgets.recyclerview.decorator.VerticalItemDecorator

class AnalyzeFragment : AdvFragment<PhotographActivity, AnalyzeViewModel>() {
    //region Static parameters
    companion object Parameter {
        // The key name of the fragment initialization parameters.
    }
    //endregion

    //region *** Private Variable ***
    private val linearLayoutManager by instance<LinearLayoutManager>(LINEAR_LAYOUT_VERTICAL)
    private val adapter by lazy {
        val innerAdapter by instance<RVAdapterAny>(LABEL_ADAPTER)

        cast<MultiTypeAdapter>(innerAdapter)
    }
    private val decorator by lazy { VerticalItemDecorator(gDimens(R.dimen.md_one_unit), gDimens(R.dimen.md_zero_unit)) }
    private val imageData by lazy { arguments?.getByteArray(ARG_IMAGE_DATA) ?: throw IllegalArgumentException() }
    //endregion

    //region Fragment Lifecycle
    //endregion

    //region Base Fragment
    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    @UiThread
    override fun bindLiveData() {
        observeNonNull(vm.labels, ::showLabels)
    }

    /** The block of unbinding from [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    @UiThread
    override fun unbindLiveData() {
        vm.labels.removeObservers(this)
    }

    @UiThread
    override fun rendered(savedInstanceState: Bundle?) {
        // Analyze the image from previous fragment.
        vm.analyzeImage(imageData)
        // Set the all components.
        iv_backdrop.loadByAny(imageData)
        rv_analyzed.apply {
            if (layoutManager.isNull()) layoutManager = linearLayoutManager
            if (adapter.isNull()) adapter = this@AnalyzeFragment.adapter
            if (0 == itemDecorationCount) addItemDecoration(decorator)
        }
        // Set the event listeners.
        fab_upload.onClick {
            // TODO(jieyi): 2018/08/10 Uploading function.
        }
    }

    @LayoutRes
    override fun provideInflateView() = R.layout.fragment_analyze_pic
    //endregion

    private fun showLabels(response: KsResponse<LabelEntities>) {
        peelResponse(response) {
            if (it.isEmpty()) showEmptyResult() else adapter.appendList(it.toMutableList())
        }
    }

    private fun showEmptyResult() {
        val actionBarHeight = parent.supportActionBar?.height ?: DEFAULT_INT
        val (statusBarHeight, screenHeightWithoutNavigationBar) = appContext.let {
            it.statusBarHeight() to it.getDisplayMetrics().heightPixels
        }
        val appBarLayoutHeight = abl_main.measuredHeight
        val betweenHeight =
            screenHeightWithoutNavigationBar - actionBarHeight - statusBarHeight - appBarLayoutHeight

        // Hide the recycler view.
        rv_analyzed.gone()
        // Show the empty error.
        fl_empty.apply {
            visible()
            resizeView(null, betweenHeight)
        }
    }
}

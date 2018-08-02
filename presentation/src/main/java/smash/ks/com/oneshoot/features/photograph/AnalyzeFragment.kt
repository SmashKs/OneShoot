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
import android.view.KeyEvent.KEYCODE_BACK
import androidx.annotation.LayoutRes
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devrapid.dialogbuilder.support.QuickDialogFragment
import com.devrapid.kotlinshaver.cast
import kotlinx.android.synthetic.main.dialog_fragment_labels.view.ib_close
import kotlinx.android.synthetic.main.dialog_fragment_labels.view.rv_labels
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.kodein.di.generic.instance
import smash.ks.com.domain.models.KsResponse
import smash.ks.com.ext.const.Constant.DEBOUNCE_DELAY
import smash.ks.com.ext.const.Constant.DEBOUNCE_SAFE_MODE_CAMERA
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.bases.AdvFragment
import smash.ks.com.oneshoot.entities.LabelEntities
import smash.ks.com.oneshoot.ext.aac.observeNonNull
import smash.ks.com.oneshoot.ext.aac.peelResponseSkipLoading
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
    private var labelDialog: QuickDialogFragment? = null
    private val linearLayoutManager by instance<LinearLayoutManager>(LINEAR_LAYOUT_VERTICAL)
    private val adapter by lazy {
        val innerAdapter by instance<RVAdapterAny>(LABEL_ADAPTER)

        cast<MultiTypeAdapter>(innerAdapter)
    }
    private val decorator by lazy {
        VerticalItemDecorator(gDimens(R.dimen.md_one_unit), gDimens(R.dimen.md_zero_unit))
    }
    private val imageData by lazy { arguments?.getByteArray(ARG_IMAGE_DATA) ?: throw IllegalArgumentException() }
    //endregion

    //region Fragment Lifecycle
    override fun onDestroy() {
        super.onDestroy()

        labelDialog?.takeIf { it.isVisible }?.dismiss()
        labelDialog = null
    }
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
        vm.analyzeImage(imageData)
    }

    @LayoutRes
    override fun provideInflateView() = R.layout.fragment_analyze_pic
    //endregion

    private fun showLabels(response: KsResponse<LabelEntities>) {
        peelResponseSkipLoading(response, ::showLabelDialog)
        // Avoid triggering again taking a pic.
        launch {
            delay(DEBOUNCE_SAFE_MODE_CAMERA)
//            shotDebounce = false
        }
    }

    private fun showLabelDialog(entities: LabelEntities) {
        labelDialog = QuickDialogFragment.Builder(this) {
            var debouncing = false

            viewResCustom = R.layout.dialog_fragment_labels
            cancelable = false
            onStartBlock = {
                it.dialog.window.setWindowAnimations(R.style.KsDialog)
            }
            fetchComponents = { v, df ->
                v.apply {
                    rv_labels.also {
                        it.layoutManager = linearLayoutManager
                        it.adapter = adapter
                        it.addItemDecoration(decorator)
                    }
                    ib_close.onClick {
                        if (false == debouncing) {
                            debouncing = true
                            delay(DEBOUNCE_DELAY)
                            dismissDialog()
                        }
                    }
                }

                df.dialog.setOnKeyListener { _, keyCode, _ ->
                    when (keyCode) {
                        KEYCODE_BACK -> {
                            dismissDialog()
                            true
                        }
                        else -> false
                    }
                }
            }
            // Transforming the data into [KsMultiVisitable] type.
            adapter.appendList(entities.toMutableList())
        }.build()

        labelDialog?.takeUnless(Fragment::isVisible)?.show()
    }

    private fun dismissDialog() {
        adapter.clearList()
        labelDialog?.view?.rv_labels?.apply {
            layoutManager = null
            adapter = null
            removeItemDecoration(decorator)
        }
        labelDialog?.dismissAllowingStateLoss()
        labelDialog = null
    }
}

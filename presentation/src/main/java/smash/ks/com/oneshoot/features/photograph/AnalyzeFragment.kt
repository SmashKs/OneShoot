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
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.annotation.UiThread
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.devrapid.dialogbuilder.support.QuickDialogFragment
import com.devrapid.kotlinknifer.displayPixels
import com.devrapid.kotlinknifer.getDisplayMetrics
import com.devrapid.kotlinknifer.getResColor
import com.devrapid.kotlinknifer.getResColorWithAlpha
import com.devrapid.kotlinknifer.gone
import com.devrapid.kotlinknifer.resizeView
import com.devrapid.kotlinknifer.setCursorPointerColor
import com.devrapid.kotlinknifer.statusBarHeight
import com.devrapid.kotlinknifer.visible
import com.devrapid.kotlinshaver.cast
import com.devrapid.kotlinshaver.isNull
import com.pchmn.materialchips.ChipsInput
import com.pchmn.materialchips.model.ChipInterface
import kotlinx.android.synthetic.main.dialog_fragment_upload.view.ci_tag
import kotlinx.android.synthetic.main.dialog_fragment_upload.view.et_author
import kotlinx.android.synthetic.main.dialog_fragment_upload.view.et_photo_title
import kotlinx.android.synthetic.main.dialog_fragment_upload.view.ib_check
import kotlinx.android.synthetic.main.dialog_fragment_upload.view.ib_close
import kotlinx.android.synthetic.main.fragment_analyze_pic.abl_main
import kotlinx.android.synthetic.main.fragment_analyze_pic.fab_upload
import kotlinx.android.synthetic.main.fragment_analyze_pic.iv_backdrop
import kotlinx.android.synthetic.main.fragment_analyze_pic.rv_analyzed
import kotlinx.android.synthetic.main.merge_recycler_item_empty.fl_empty
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import org.kodein.di.generic.instance
import smash.ks.com.domain.models.response.KsResponse
import smash.ks.com.ext.const.DEFAULT_INT
import smash.ks.com.ext.const.DEFAULT_STR
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
import kotlin.math.roundToInt

class AnalyzeFragment : AdvFragment<PhotographActivity, AnalyzeViewModel>() {
    companion object {
        private const val DIALOG_FRAGMENT_WIDTH_RATIO = .9f
        private const val DIALOG_FRAGMENT_HEIGHT_RATIO = .75f
    }

    //region *** Private Variable ***
    private val linearLayoutManager by instance<LinearLayoutManager>(LINEAR_LAYOUT_VERTICAL)
    private val adapter by lazy {
        val innerAdapter by instance<RVAdapterAny>(LABEL_ADAPTER)

        cast<MultiTypeAdapter>(innerAdapter)
    }
    private val decorator by lazy { VerticalItemDecorator(gDimens(R.dimen.md_two_unit), gDimens(R.dimen.md_two_unit)) }
    private val imageData by lazy { requireNotNull(arguments?.getByteArray(ARG_IMAGE_DATA)) }
    private val vmFactory by instance<ViewModelProvider.Factory>()
    private val vmUpload by lazy { ViewModelProviders.of(this, vmFactory)[UploadPicViewModel::class.java] }
    private val tags by lazy { mutableListOf<ChipInterface>() }
    private val dialogFragment
        get() = QuickDialogFragment.Builder(this@AnalyzeFragment) {
            viewResCustom = R.layout.dialog_fragment_upload
            onStartBlock = {
                val (width, height) = requireNotNull(it.activity?.displayPixels())
                val realWidth = width * DIALOG_FRAGMENT_WIDTH_RATIO
                val realHeight = height * DIALOG_FRAGMENT_HEIGHT_RATIO
                it.dialog.window?.apply {
                    setWindowAnimations(R.style.KsDialog)
                    setLayout(realWidth.roundToInt(), realHeight.roundToInt())
                }
            }
            fetchComponents = { v, dialog ->
                fun collectionAllData() {
                    val title = v.et_photo_title.text.toString()
                    val author = v.et_author.text.toString()
                    val labels = tags.map { it.label }

//                vm.uploadPhoto(imageData, title, author, labels)
                }

                v.apply {
                    ib_close.onClick { dialog.dismiss() }
                    ib_check.onClick { collectionAllData() }
                    ci_tag.addChipsListener(object : ChipsInput.ChipsListener {
                        override fun onChipAdded(chip: ChipInterface, newSize: Int) {
                            tags.add(chip)
                        }

                        override fun onChipRemoved(chip: ChipInterface, newSize: Int) {
                            tags.remove(chip)
                        }

                        override fun onTextChanged(s: CharSequence) {
                            if (s.isNotBlank() && s.last().toString() == " ") {
                                ci_tag.addChip(s.trim().toString(), DEFAULT_STR)
                            }
                        }
                    })
                    listOf(et_author, et_photo_title).map {
                        setCursorPointerColor(cast(it), context.getResColor(R.color.primaryLightColor))
                        cast<EditText>(it).highlightColor =
                            appContext.getResColorWithAlpha(R.color.primaryLightColor, 0.5f)
                    }
                }
            }
        }.build()
    //endregion

    override fun onStop() {
        super.onStop()
        if (dialogFragment.isVisible) dialogFragment.dismiss()
    }
    //endregion

    //region Base Fragment
    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    @UiThread
    override fun bindLiveData() {
        observeNonNull(vmUpload.uploadRes, ::showUploadError)
        observeNonNull(vm.labels, ::showLabels)
    }

    /** The block of unbinding from [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    @UiThread
    override fun unbindLiveData() {
        vm.labels.removeObservers(this)
        vmUpload.uploadRes.removeObservers(this)
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
            dialogFragment.show()
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
        // Calculating the error page's height.
        val actionBarHeight = parent.supportActionBar?.height ?: DEFAULT_INT
        val (statusBarHeight, screenHeightWithoutNavigationBar) = appContext.let {
            it.statusBarHeight() to it.getDisplayMetrics().heightPixels
        }
        val appBarLayoutHeight = abl_main.measuredHeight
        val betweenHeight = screenHeightWithoutNavigationBar - actionBarHeight - statusBarHeight - appBarLayoutHeight

        // Hide the recycler view.
        rv_analyzed.gone()
        // Show the empty error.
        fl_empty.apply { resizeView(null, betweenHeight) }.visible()
    }

    private fun showUploadError(response: KsResponse<Unit>) {
        peelResponse(response, { parent.toast(it) }, null)
    }
}

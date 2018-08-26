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
import com.pchmn.materialchips.ChipsInput
import com.pchmn.materialchips.model.ChipInterface
import kotlinx.android.synthetic.main.fragment_upload_pic.ci_tag
import kotlinx.android.synthetic.main.fragment_upload_pic.et_author
import kotlinx.android.synthetic.main.fragment_upload_pic.et_photo_title
import kotlinx.android.synthetic.main.fragment_upload_pic.ib_cancel
import kotlinx.android.synthetic.main.fragment_upload_pic.ib_check
import kotlinx.android.synthetic.main.fragment_upload_pic.iv_upload
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import smash.ks.com.ext.const.DEFAULT_STR
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.bases.AdvFragment
import smash.ks.com.oneshoot.ext.aac.navigation.findNavController
import smash.ks.com.oneshoot.ext.aac.observeNonNull
import smash.ks.com.oneshoot.ext.aac.peelResponseForCompleted
import smash.ks.com.oneshoot.ext.image.glide.loadByAny
import smash.ks.com.oneshoot.features.photograph.TakeAPicFragment.Parameter.ARG_IMAGE_DATA

class UploadPicFragment : AdvFragment<PhotographActivity, UploadPicViewModel>() {
    //region *** Private Variable ***
    // The fragment initialization parameters.
    private val imageData by lazy { requireNotNull(arguments?.getByteArray(ARG_IMAGE_DATA)) }

    private var isUploaded = false
    private val tags by lazy { mutableListOf<ChipInterface>() }
    //endregion

    //region Base Implementation
    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    @UiThread
    override fun bindLiveData() {
        observeNonNull(vm.uploadRes) { peelResponseForCompleted(it, ::showToast, ::uploadCompleted) }
    }

    @UiThread
    override fun unbindLiveData() {
        vm.uploadRes.removeObservers(this)
    }

    @UiThread
    override fun rendered(savedInstanceState: Bundle?) {
        // Set the components.
        iv_upload.loadByAny(imageData)
        // Set the components' event listeners.
        setEventListeners()
    }

    @LayoutRes
    override fun provideInflateView() = R.layout.fragment_upload_pic
    //endregion

    private fun setEventListeners() {
        ib_check.onClick {
            if (isUploaded) {
                showToast(getString(R.string.toast_has_uploaded))
            }
            collectionAllData()
        }
        ib_cancel.onClick { findNavController()?.navigateUp() }
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
    }

    private fun collectionAllData() {
        val title = et_photo_title.text.toString()
        val author = et_author.text.toString()
        val labels = tags.map { it.label }

        vm.uploadPhoto(imageData, title, author, labels)
    }

    private fun uploadCompleted() {
        isUploaded = true
        showToast(getString(R.string.toast_upload_success))
    }

    private fun showToast(msg: String) {
        parent.toast(msg)
    }
}

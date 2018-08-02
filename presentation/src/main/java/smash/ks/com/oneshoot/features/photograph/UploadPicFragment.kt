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
import com.devrapid.kotlinknifer.logw
import kotlinx.android.synthetic.main.fragment_upload_pic.et_author
import kotlinx.android.synthetic.main.fragment_upload_pic.et_photo_title
import kotlinx.android.synthetic.main.fragment_upload_pic.ib_cancel
import kotlinx.android.synthetic.main.fragment_upload_pic.ib_check
import kotlinx.android.synthetic.main.fragment_upload_pic.iv_upload
import org.jetbrains.anko.sdk25.coroutines.onClick
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.bases.AdvFragment
import smash.ks.com.oneshoot.ext.aac.navigation.findNavController
import smash.ks.com.oneshoot.ext.aac.observe
import smash.ks.com.oneshoot.ext.image.glide.loadByAny
import smash.ks.com.oneshoot.features.photograph.TakeAPicFragment.Parameter.ARG_IMAGE_DATA

class UploadPicFragment : AdvFragment<PhotographActivity, UploadPicViewModel>() {
    //region Static parameters
    companion object Parameter {
        // The key name of the fragment initialization parameters.
    }
    //endregion

    //region *** Private Variable ***
    // The fragment initialization parameters.
    private val imageData by lazy { arguments?.getByteArray(ARG_IMAGE_DATA) ?: throw IllegalArgumentException() }
    //endregion

    //region Base Implementation
    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    @UiThread
    override fun bindLiveData() {
        vm.apply {
            observe(uploadRes) {
                logw(it)
            }
        }
    }

    @UiThread
    override fun rendered(savedInstanceState: Bundle?) {
        iv_upload.loadByAny(imageData)
        ib_check.onClick { collectionAllData() }
        ib_cancel.onClick { findNavController()?.navigateUp() }
    }

    @LayoutRes
    override fun provideInflateView() = R.layout.fragment_upload_pic
    //endregion

    private fun collectionAllData() {
        vm.uploadPhoto("", et_photo_title.text.toString(), et_author.text.toString(), emptyList())
    }
}

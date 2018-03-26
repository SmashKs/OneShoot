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

package smash.ks.com.oneshoot.mvp.contracts

import smash.ks.com.oneshoot.mvp.presenters.BasePresenter
import smash.ks.com.oneshoot.mvp.views.MvpView

/**
 * This specifies the contract between the [smash.ks.com.oneshoot.mvp.presenters.MvpPresenter] and
 * the [MvpView].
 *
 * @author  Jieyi Wu
 * @since   2017/09/25
 */
interface MainContract {
    abstract class Presenter : BasePresenter<View>() {
        abstract fun obtainImageUri(imageId: Int)
    }

    interface View : MvpView {
        fun showImageUri(uri: String)
    }
}
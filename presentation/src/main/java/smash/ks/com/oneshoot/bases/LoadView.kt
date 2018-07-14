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

package smash.ks.com.oneshoot.bases

import androidx.annotation.UiThread

/**
 * Interface representing a View that will use to load data.
 */
interface LoadView {
    /**
     * Show a view with a progress bar indicating a loading process.
     */
    @UiThread
    fun showLoading()

    /**
     * Hide a loading view.
     */
    @UiThread
    fun hideLoading()

    /**
     * Show a retry view in case of an error when retrieving data.
     */
    @UiThread
    fun showRetry()

    /**
     * Hide a retry view shown if there was an error when retrieving data.
     */
    @UiThread
    fun hideRetry()

    /**
     * Show an error message
     *
     * @param message A string representing an error.
     */
    @UiThread
    fun showError(message: String)
}

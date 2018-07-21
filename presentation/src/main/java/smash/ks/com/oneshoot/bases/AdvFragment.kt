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

import android.os.Bundle
import androidx.annotation.UiThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.devrapid.kotlinshaver.cast
import org.kodein.di.generic.instance
import smash.ks.com.oneshoot.ext.stubview.hideLoadingView
import smash.ks.com.oneshoot.ext.stubview.hideRetryView
import smash.ks.com.oneshoot.ext.stubview.showErrorView
import smash.ks.com.oneshoot.ext.stubview.showLoadingView
import smash.ks.com.oneshoot.ext.stubview.showRetryView
import java.lang.reflect.ParameterizedType

/**
 * The basic fragment is in MVVM architecture which prepares all necessary variables or functions.
 */
abstract class AdvFragment<out A : BaseActivity, out VM : ViewModel> : BaseFragment<A>(), LoadView {
    /** Add the AAC [ViewModel] for each fragments. */
    @Suppress("UNCHECKED_CAST")
    protected val vm by lazy {
        vmCreateMethod.invoke(vmProviders, vmConcreteClass) as? VM ?: throw ClassCastException()
    }

    private val viewModelFactory by instance<ViewModelProvider.Factory>()
    /** [VM] is the first (index: 1) in the generic declare. */
    private val vmConcreteClass
        get() = cast<Class<*>>(cast<ParameterizedType>(this::class.java.genericSuperclass).actualTypeArguments[1])
    private val vmProviders by lazy { ViewModelProviders.of(this, viewModelFactory) }
    /** The [ViewModelProviders.of] function for obtaining a [ViewModel]. */
    private val vmCreateMethod get() = vmProviders.javaClass.getMethod("get", vmConcreteClass.superclass.javaClass)

    //region Fragment's lifecycle.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindLiveData()
    }
    //endregion

    //region View Implementation for the Presenter.
    @UiThread
    override fun showLoading() = activity?.showLoadingView() ?: Unit

    @UiThread
    override fun hideLoading() = activity?.hideLoadingView() ?: Unit

    @UiThread
    override fun showRetry() = activity?.showRetryView() ?: Unit

    @UiThread
    override fun hideRetry() = activity?.hideRetryView() ?: Unit

    @UiThread
    override fun showError(message: String) = activity?.showErrorView(message) ?: Unit
    //endregion

    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    @UiThread
    protected open fun bindLiveData() = Unit
}

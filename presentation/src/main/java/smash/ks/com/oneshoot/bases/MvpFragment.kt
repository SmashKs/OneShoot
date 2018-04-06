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

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import smash.ks.com.oneshoot.mvp.presenters.BasePresenter
import smash.ks.com.oneshoot.mvp.views.MvpView
import java.lang.reflect.ParameterizedType

abstract class MvpFragment<V : MvpView, out P : BasePresenter<V>, out A : BaseActivity, out VM : ViewModel> :
    BaseFragment<A>() {
    abstract val presenter: P
    abstract val viewModelFactory: ViewModelProvider.Factory?

    /** Add the AAC [ViewModel] for each fragments. */
    protected val vm by lazy { vmCreateMethod.invoke(vmProviders, vmConcreteClass) as VM }
    /** [VM] is the fourth (index: 3) in the generic declare. */
    private val vmConcreteClass get() = (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[3] as Class<*>
    private val vmProviders by lazy { ViewModelProviders.of(this, viewModelFactory) }
    /** The [ViewModelProviders.of.get] function for obtaining a [ViewModel]. */
    private val vmCreateMethod get() = vmProviders.javaClass.getMethod("get", vmConcreteClass.superclass.javaClass)

    //region Fragment lifecycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter.create(this)
        presenter.view = provideCurrentFragmentView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.init()
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onPause() {
        super.onPause()
        presenter.pause()
    }

    override fun onDestroy() {
        // After super.onDestroy() is executed, the presenter will be destroy. So the presenter should be
        // executed before super.onDestroy().
        presenter.resume()
        super.onDestroy()
    }
    //endregion

    abstract fun provideCurrentFragmentView(): V

    protected fun showLoadView() {}
}
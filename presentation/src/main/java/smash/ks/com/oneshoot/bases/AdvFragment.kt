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
import org.kodein.di.generic.instance
import java.lang.reflect.ParameterizedType

abstract class AdvFragment<out VM : ViewModel, out A : BaseActivity> : BaseFragment<A>() {
    /** Add the AAC [ViewModel] for each fragments. */
    protected val vm by lazy { vmCreateMethod.invoke(vmProviders, vmConcreteClass) as VM }

    private val viewModelFactory by instance<ViewModelProvider.Factory>()
    /** [VM] is the first (index: 0) in the generic declare. */
    private val vmConcreteClass
        get() = (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
    private val vmProviders by lazy { ViewModelProviders.of(this, viewModelFactory) }
    /** The [ViewModelProviders.of.get] function for obtaining a [ViewModel]. */
    private val vmCreateMethod get() = vmProviders.javaClass.getMethod("get", vmConcreteClass.superclass.javaClass)
}
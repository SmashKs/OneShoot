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

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.entities.KsEntity
import smash.ks.com.oneshoot.features.main.FakeViewHolder
import smash.ks.com.oneshoot.internal.di.modules.ViewHolderEntry
import smash.ks.com.oneshoot.internal.di.modules.ViewModelEntries
import smash.ks.com.oneshoot.internal.di.modules.dependencies.fragment.MainModule.mainModule
import smash.ks.com.oneshoot.widgets.viewmodel.ViewModelFactory

abstract class BaseFragment<out A : BaseActivity> : RxFragment(), KodeinAware {
    override val kodeinContext get() = kcontext(activity)
    override val kodein = Kodein.lazy {
        extend(parentKodein)
        /* fragment specific bindings */
        import(mainModule())

        val viewModelSet by instance<ViewModelEntries>()
        bind<ViewModelProvider.Factory>() with singleton {
            ViewModelFactory(instance(), viewModelSet.toMap().toMutableMap())
        }
        // ***  ***
        bind<ViewHolderEntry>().inSet() with provider {
            KsEntity::class.hashCode() to Pair(R.layout.item_fake, ::FakeViewHolder)
        }
    }
    protected val appContext by instance<Context>()
    protected val parent by lazy { activity as A }  // If there's no parent, forcing crashing the app.
    private var rootView: View? = null
    private val parentKodein by closestKodein()

    //region Fragment lifecycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Keep the instance data.
        retainInstance = true
        // FIXED: https://www.zybuluo.com/kimo/note/255244
        rootView ?: let { rootView = inflater.inflate(provideInflateView(), null) }
        val parent: ViewGroup? = rootView?.parent as ViewGroup?
        parent?.removeView(rootView)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rendered(savedInstanceState)
    }
    //endregion

    /**
     * Initialize method.
     *
     * @param savedInstanceState before status.
     */
    protected abstract fun rendered(savedInstanceState: Bundle?)

    /**
     * Set the parentView for inflating.
     *
     * @return [LayoutRes] layout xml.
     */
    @LayoutRes
    protected abstract fun provideInflateView(): Int

    protected fun View.clickedThenHideKeyboard() {
        if (hasOnClickListeners()) return
//        bindingToUiClicks(this@BaseFragment, 0).subscribe { hideSoftKeyboard() }
    }
}
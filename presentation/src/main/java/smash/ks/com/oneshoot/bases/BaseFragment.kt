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

import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.devrapid.kotlinknifer.hideSoftKeyboard
import com.devrapid.kotlinshaver.castOrNull
import org.jetbrains.anko.findOptional
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import org.kodein.di.generic.singleton
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.internal.di.modules.ViewModelEntries
import smash.ks.com.oneshoot.internal.di.modules.dependencies.fragment.SuperFragmentModule.fragmentModule
import smash.ks.com.oneshoot.widgets.viewmodel.ViewModelFactory

/**
 * The basic fragment is for the normal activity which prepares all necessary variables or functions.
 */
abstract class BaseFragment<out A : BaseActivity> : Fragment(), KodeinAware {
    override val kodeinContext get() = kcontext(requireActivity())
    override val kodein = Kodein.lazy {
        extend(parentKodein)
        /* fragment specific bindings */
        import(fragmentModule())

        bind<ViewModelProvider.Factory>() with singleton {
            ViewModelFactory(instance(), instance<ViewModelEntries>().toMap().toMutableMap())
        }
    }
    @Suppress("UNCHECKED_CAST")
    protected val parent by lazy { requireActivity() as A }  // If there's no parent, forcing crashing the app.
    protected val appContext by instance<Context>()
    private var rootView: View? = null
    private val parentKodein by closestKodein()

    //region Fragment lifecycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Keep the instance data.
        retainInstance = true

        val localInflater = customTheme()?.let {
            // Create ContextThemeWrapper from the original Activity Context with the custom theme
            val contextThemeWrapper = ContextThemeWrapper(activity, it)
            // Clone the inflater using the ContextThemeWrapper
            inflater.cloneInContext(contextThemeWrapper)
        } ?: inflater

        // FIXED: https://www.zybuluo.com/kimo/note/255244
        // inflate the layout using the cloned inflater, not default inflater
        rootView ?: let { rootView = localInflater.inflate(provideInflateView(), null) }
        val parent = castOrNull<ViewGroup>(rootView?.parent)
        parent?.removeView(rootView)

        return rootView
    }

    /**
     * For initializing the view components and setting the listeners.
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rendered(savedInstanceState)
        // When the fragment has base_layout id, it'll attach the function of hiding soft keyboard.
        view.findOptional<View>(R.id.base_layout)?.clickedThenHideKeyboard()
    }
    //endregion

    /**
     * Initialize method.
     *
     * @param savedInstanceState before status.
     */
    @UiThread
    protected abstract fun rendered(savedInstanceState: Bundle?)

    /**
     * Set the parentView for inflating.
     *
     * @return [LayoutRes] layout xml.
     */
    @UiThread
    @LayoutRes
    protected abstract fun provideInflateView(): Int

    @UiThread
    @StyleRes
    protected open fun customTheme(): Int? = null

    /**
     * Attaching the function of hiding the soft keyboard into a [View].
     */
    @UiThread
    protected fun View.clickedThenHideKeyboard() {
        if (!hasOnClickListeners()) onClick { hideSoftKeyboard() }
    }
}

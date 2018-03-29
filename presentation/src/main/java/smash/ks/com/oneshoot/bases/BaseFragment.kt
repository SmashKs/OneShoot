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
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment

abstract class BaseFragment : RxFragment() {
    /** From an activity for providing to children searchFragments. */
//    @Inject lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>
//    protected val appContext: Context by lazy { activity?.applicationContext ?: gContext() }
    protected var rootView: View? = null

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

//    protected fun View.clickedThenHideKeyboard() {
//        if (hasOnClickListeners()) return
//        bindingToUiClicks(this@BaseFragment, 0).subscribe { hideSoftKeyboard() }
//    }
}
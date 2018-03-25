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
import smash.ks.com.oneshoot.mvp.presenters.BasePresenter
import smash.ks.com.oneshoot.mvp.views.MvpView

abstract class MvpActivity<V : MvpView, P : BasePresenter<V>> : BaseActivity() {
    abstract var presenter: P

    //region Activity lifecycle
    override fun onContentChanged() {
        super.onContentChanged()
        presenter.create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mvpOnCreate = {
            // OPTIMIZE(jieyi): 11/9/17 Here might restart activity again, this function won't be ran!?
            presenter.view = provideCurrentActivityView()
            presenter.init()
        }
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }

    override fun onPause() {
        super.onPause()
        presenter.pause()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onDestroy() {
        // After super.onDestroy() is executed, the presenter will be destroy. So the presenter should be
        // executed before super.onDestroy().
        presenter.destroy()
        super.onDestroy()
    }
    //endregion

    abstract fun provideCurrentActivityView(): V
}
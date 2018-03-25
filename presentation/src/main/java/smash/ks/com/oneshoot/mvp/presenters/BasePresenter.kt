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

package smash.ks.com.oneshoot.mvp.presenters

import com.trello.rxlifecycle2.LifecycleProvider
import smash.ks.com.oneshoot.mvp.views.MvpView

abstract class BasePresenter<V : MvpView> : MvpPresenter {
    open lateinit var view: V
    protected lateinit var lifecycleProvider: LifecycleProvider<*>

    override fun <E> create(lifecycleProvider: LifecycleProvider<E>) {
        this.lifecycleProvider = lifecycleProvider
    }

    override fun init() {}

    override fun start() {}

    override fun resume() {}

    override fun pause() {}

    override fun stop() {}

    override fun destroy() {}
}
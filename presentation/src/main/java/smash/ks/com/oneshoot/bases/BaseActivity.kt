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
import com.hwangjr.rxbus.Bus
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import org.kodein.Kodein
import org.kodein.KodeinAware
import org.kodein.android.closestKodein
import org.kodein.generic.instance
import org.kodein.generic.kcontext
import smash.ks.com.oneshoot.internal.di.modules.dependencies.activity.MainModule.mainModule

abstract class BaseActivity : RxAppCompatActivity(), KodeinAware {
    override val kodeinContext get() = kcontext(this)
    override val kodein by Kodein.lazy {
        extend(_parentKodein)
        /* activity specific bindings */
        import(mainModule())
    }
    protected var mvpOnCreate: (() -> Unit)? = null
    protected val bus by instance<Bus>()
    private val _parentKodein by closestKodein()

    //region RxBus Example
    // Register it in the parent class that it will be not reflected.
    protected var busEvent = object {
//        @Subscribe(tags = arrayOf(Tag(RxbusTag.NAVIGATOR)))
//        fun test(test: String) { }
    }
    //endregion

    //region Activity lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(provideLayoutId())
        mvpOnCreate?.invoke()

        // Register RxBus.
        bus.register(busEvent)
        init(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Unregister RxBus.
        bus.unregister(busEvent)
    }
    //endregion

    abstract fun init(savedInstanceState: Bundle?)

    @LayoutRes
    abstract fun provideLayoutId(): Int
}

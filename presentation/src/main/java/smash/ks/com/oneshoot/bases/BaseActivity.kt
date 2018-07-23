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
import androidx.annotation.LayoutRes
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.hwangjr.rxbus.Bus
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import smash.ks.com.oneshoot.internal.di.modules.dependencies.activity.SuperActivityModule.activityModule

/**
 * The basic activity is for the normal activity which prepares all necessary variables or functions.
 */
abstract class BaseActivity : AppCompatActivity(), KodeinAware {
    override val kodeinContext get() = kcontext(this)
    override val kodein by retainedKodein {
        extend(parentKodein)
        /* activity specific bindings */
        import(activityModule())
    }
    protected val bus by instance<Bus>()
    private val parentKodein by closestKodein()

    //region RxBus Example
    // OPTIMIZE(jieyi): 2018/04/19 This's a simple example for RxBus.
    // Register it in the parent class that it will be not reflected.
    protected var busEvent = object {
//        @Subscribe(tags = [Tag(RxbusTag.NAVIGATOR)])
//        fun test(test: String) { }
    }
    //endregion

    //region Activity lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(provideLayoutId())

        // Register RxBus.
        bus.register(busEvent)
        init(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Unregister RxBus.
        bus.unregister(busEvent)
        uninit()
    }
    //endregion

    @UiThread
    protected abstract fun init(savedInstanceState: Bundle?)

    @UiThread
    protected open fun uninit() {
    }

    @LayoutRes
    abstract fun provideLayoutId(): Int
}

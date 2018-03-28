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
import com.hwangjr.rxbus.RxBus
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

abstract class BaseActivity : RxAppCompatActivity() {
    /** For providing to searchFragments. */
//    @Inject lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>
    var mvpOnCreate: (() -> Unit)? = null

    // Register it in the parent class that it will be not reflected.
    protected var busEvent = object {
//        @Subscribe(tags = arrayOf(Tag(RxbusTag.NAVIGATOR)))
//        fun test(test: String) {
//        }
    }

    //region Activity lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(provideLayoutId())
        mvpOnCreate?.invoke()

        // Register RxBus.
        RxBus.get().register(busEvent)
        init(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Unregister RxBus.
        RxBus.get().unregister(busEvent)
    }
    //endregion

    abstract fun init(savedInstanceState: Bundle?)

    @LayoutRes
    abstract fun provideLayoutId(): Int
}

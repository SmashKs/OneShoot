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

package smash.ks.com.oneshoot.features.splash

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.UiThread
import org.jetbrains.anko.startActivity
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.bases.BaseActivity
import smash.ks.com.oneshoot.features.photograph.PhotographActivity

class SplashActivity : BaseActivity() {
    @UiThread
    override fun init(savedInstanceState: Bundle?) {
        startActivity<PhotographActivity>()
        finish()
    }

    @LayoutRes
    override fun provideLayoutId() = R.layout.activity_splash
}

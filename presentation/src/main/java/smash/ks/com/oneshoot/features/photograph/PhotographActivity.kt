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

package smash.ks.com.oneshoot.features.photograph

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.bases.BaseActivity

class PhotographActivity : BaseActivity() {
    private val navController by lazy { findNavController(R.id.f_camera) }
    override fun init(savedInstanceState: Bundle?) {
        // Set up Action Bar
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun provideLayoutId() = R.layout.activity_camera

    override fun onSupportNavigateUp() = navController.navigateUp()
}

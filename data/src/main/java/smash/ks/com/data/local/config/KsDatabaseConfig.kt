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

package smash.ks.com.data.local.config

import com.raizlabs.android.dbflow.annotation.Database
import smash.ks.com.data.local.config.KsDatabaseConfig.VERSION

/**
 * The setting for creating a database by dbflow library with the database version
 * and migration setting.
 */
@Database(name = "KsDatabase", version = VERSION)
object KsDatabaseConfig {
    const val VERSION = 1

    // Here we can code migration class.
}

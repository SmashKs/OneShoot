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

package smash.ks.com.oneshoot.internal.di.modules

import com.hwangjr.rxbus.Bus
import com.hwangjr.rxbus.RxBus
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import smash.ks.com.data.JobExecutor
import smash.ks.com.domain.executors.PostExecutionThread
import smash.ks.com.domain.executors.ThreadExecutor
import smash.ks.com.oneshoot.UiThread

object AppModule {
    fun appModule() = Module {
        bind<Bus>() with instance(RxBus.get())
        // For RxJava Thread Scheduler.
        bind<ThreadExecutor>() with instance(JobExecutor())
        bind<PostExecutionThread>() with instance(UiThread())
    }
}

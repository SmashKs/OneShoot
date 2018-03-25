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

package smash.ks.com.domain

import io.reactivex.CompletableTransformer
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.SingleTransformer
import io.reactivex.schedulers.Schedulers
import smash.ks.com.domain.BaseUseCase.RequestValues
import smash.ks.com.domain.executors.PostExecutionThread
import smash.ks.com.domain.executors.ThreadExecutor

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case in the
 * application should implement this contract).
 *
 * By convention each UseCase implementation will return the result using a [org.reactivestreams.Subscriber]
 * that will execute its job in a background thread and will post the result in the UI thread.
 *
 * For passing a request parameters [RequestValues] to data layer that set a generic type for wrapping
 * vary data.
 */
abstract class BaseUseCase<R : RequestValues>(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) {
    /** Provide a common parameter variable for the children class. */
    var requestValues: R? = null

    /**
     * Composing the transfer a new thread [ThreadExecutor] scheduler to [smash.ks.com.oneshoot.UiThread]
     * for [Observable].
     */
    protected fun <T> observableTransferSchedule() = ObservableTransformer<T, T> {
        it.subscribeOn(subscribeScheduler).observeOn(observeScheduler)
    }

    /**
     * Composing the transfer a new thread [ThreadExecutor] scheduler to [smash.ks.com.oneshoot.UiThread]
     * for [io.reactivex.Single].
     */
    protected fun <T> singleTransferSchedule() = SingleTransformer<T, T> {
        it.subscribeOn(subscribeScheduler).observeOn(observeScheduler)
    }

    /**
     * Composing the transfer a new thread [ThreadExecutor] scheduler to [smash.ks.com.oneshoot.UiThread]
     * for [io.reactivex.Completable].
     */
    protected fun completableTransferSchedule() = CompletableTransformer {
        it.subscribeOn(subscribeScheduler).observeOn(observeScheduler)
    }

    /**
     * Obtain a thread for while [Observable] is doing their tasks.
     *
     * @return [Scheduler] implement from [PostExecutionThread].
     */
    protected open val observeScheduler = postExecutionThread.scheduler

    /**
     * Obtain a thread from [java.util.concurrent.ThreadPoolExecutor] for while [Scheduler] is
     * doing their tasks.
     *
     * @return [Scheduler] implement from [ThreadExecutor].
     */
    protected open val subscribeScheduler = Schedulers.from(threadExecutor)

    /** Interface for wrap a data for passing to a request.*/
    interface RequestValues
}

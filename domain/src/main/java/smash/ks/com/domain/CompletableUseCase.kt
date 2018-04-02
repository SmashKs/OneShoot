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

import com.devrapid.kotlinshaver.CompletablePlugin
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import smash.ks.com.domain.executors.PostExecutionThread
import smash.ks.com.domain.executors.ThreadExecutor

abstract class CompletableUseCase<R : BaseUseCase.RequestValues>(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BaseUseCase<R>(threadExecutor, postExecutionThread) {
    //region UseCase with an anonymous function.
    /**
     * Executes the current use case.
     *
     * @param lifecycleProvider the life cycle provider for cutting RxJava runs.
     * @param block add some chain actions between [subscribeOn] and [observeOn].
     * @param completableObserver a reaction of [Completable] from presentation, the data are omitted from
     *                            database or remote.
     */
    fun execute(
        lifecycleProvider: LifecycleProvider<*>? = null,
        block: Completable.() -> Completable,
        completableObserver: CompletableObserver
    ) = buildCompletableUseCase(block)
        .apply { lifecycleProvider?.bindToLifecycle<Unit>() }
        .subscribe(completableObserver)

    /**
     * Executes the current use case with request [parameter].
     *
     * @param parameter the parameter for retrieving data.
     * @param lifecycleProvider the life cycle provider for cutting RxJava runs.
     * @param block add some chain actions between [subscribeOn] and [observeOn].
     * @param completableObserver a reaction of [Completable] from presentation, the data are omitted from database or remote.
     */
    fun execute(
        parameter: R,
        lifecycleProvider: LifecycleProvider<*>? = null,
        block: Completable.() -> Completable,
        completableObserver: CompletableObserver
    ) {
        requestValues = parameter
        execute(lifecycleProvider, block, completableObserver)
    }

    /**
     * Executes the current use case with an anonymous function.
     *
     * @param lifecycleProvider an activity or a fragment of the [LifecycleProvider] object.
     * @param block add some chain actions between [subscribeOn] and [observeOn].
     * @param completableObserver a reaction of [Completable] from presentation, the data are omitted from
     *                       database or remote.
     */
    fun execute(
        lifecycleProvider: LifecycleProvider<*>? = null,
        block: Completable.() -> Completable,
        completableObserver: CompletablePlugin.() -> Unit
    ) = execute(lifecycleProvider, block, CompletablePlugin().apply(completableObserver))

    /**
     * Executes the current use case with request [parameter] with an anonymous function..
     *
     * @param parameter the parameter for retrieving data.
     * @param lifecycleProvider an activity or a fragment of the [LifecycleProvider] object.
     * @param block add some chain actions between [subscribeOn] and [observeOn].
     * @param completableObserver a reaction of [Completable] from presentation, the data are omitted from database or remote.
     */
    fun execute(
        parameter: R,
        lifecycleProvider: LifecycleProvider<*>? = null,
        block: Completable.() -> Completable,
        completableObserver: CompletablePlugin.() -> Unit
    ) {
        requestValues = parameter
        execute(lifecycleProvider, block, completableObserver)
    }

    /**
     * Build an [Completable] which will be used when executing the current [SingleUseCase].
     * There is a [io.reactivex.internal.operators.observable.ObservableSubscribeOn] for fetching
     * the data from the [smash.ks.com.domain.repositories.DataRepository] works on the new thread
     * so after [io.reactivex.internal.operators.observable.ObservableSubscribeOn]'s chain function
     * will be ran on the same thread.
     * This is for who needs transfer the thread to UI, IO, or new thread again.
     *
     * @param block add some chain actions between [subscribeOn] and [observeOn].
     * @return [Completable] for connecting with a [CompletableObserver] from the kotlin layer.
     */
    private fun buildCompletableUseCase(block: (Completable.() -> Completable)) =
        fetchUseCase()
            .subscribeOn(subscribeScheduler)
            .block()
            .observeOn(observeScheduler)
    //endregion

    //region UseCase without an anonymous function.
    /**
     * Executes the current use case.
     *
     * @param lifecycleProvider the life cycle provider for cutting RxJava runs.
     * @param completableObserver a reaction of [] from presentation, the data are omitted
     *                 from database or remote.
     */
    fun execute(lifecycleProvider: LifecycleProvider<*>? = null, completableObserver: CompletableObserver) =
        buildCompletableUseCase().apply { lifecycleProvider?.bindToLifecycle<Unit>() }.subscribe(completableObserver)

    /**
     * Executes the current use case with request [parameter].
     *
     * @param parameter the parameter for retrieving data.
     * @param lifecycleProvider the life cycle provider for cutting RxJava runs.
     * @param completableObserver a reaction of [CompletableObserver] from presentation, the data are omitted from
     *                 database or remote.
     */
    fun execute(
        parameter: R,
        lifecycleProvider: LifecycleProvider<*>? = null,
        completableObserver: CompletableObserver
    ) {
        requestValues = parameter
        execute(lifecycleProvider, completableObserver)
    }

    /**
     * Executes the current use case.
     *
     * @param lifecycleProvider an activity or a fragment of the [LifecycleProvider] object.
     * @param observer a reaction of [CompletableObserver] from presentation, the data are omitted from database or remote.
     */
    fun execute(lifecycleProvider: LifecycleProvider<*>? = null, observer: CompletablePlugin.() -> Unit) =
        execute(lifecycleProvider, CompletablePlugin().apply(observer))

    /**
     * Executes the current use case with request [parameter].
     *
     * @param parameter the parameter for retrieving data.
     * @param lifecycleProvider an activity or a fragment of the [LifecycleProvider] object.
     * @param observer a reaction of [CompletableObserver] from presentation, the data are omitted from database or remote.
     */
    fun execute(
        parameter: R,
        lifecycleProvider: LifecycleProvider<*>? = null,
        observer: CompletablePlugin.() -> Unit
    ) {
        requestValues = parameter
        execute(lifecycleProvider, observer)
        fetchUseCase()
    }

    private fun buildCompletableUseCase() = fetchUseCase().compose(completableTransferSchedule())
    //endregion

    /**
     * Choose a method from [smash.ks.com.data.datastores.DataStore] and fit this use case
     * for return some data.
     *
     * @return an [Completable] for chaining on working threads.
     */
    abstract fun fetchUseCase(): Completable
}
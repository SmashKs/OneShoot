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

import com.devrapid.kotlinshaver.SinglePlugin
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.SingleSource
import smash.ks.com.domain.executors.PostExecutionThread
import smash.ks.com.domain.executors.ThreadExecutor

/**
 * Abstract class for a Use Case [Single] (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case in the
 * application should implement this contract).
 *
 * By convention each UseCase implementation will return the result using a [org.reactivestreams.Subscriber]
 * that will execute its job in a background thread and will post the result in the UI thread.
 *
 * For passing a request parameters [smash.ks.com.domain.BaseUseCase.RequestValues] to data layer
 * that set a generic type for wrapping vary data.
 */
abstract class SingleUseCase<T, R : BaseUseCase.RequestValues>(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BaseUseCase<R>(threadExecutor, postExecutionThread) {
    //region UseCase with an anonymous function.
    /**
     * Executes the current use case.
     *
     * @param lifecycleProvider the life cycle provider for cutting RxJava runs.
     * @param block add some chain actions between [subscribeOn] and [observeOn].
     * @param singleObserver a reaction of [Single] from presentation, the data are omitted from
     *                       database or remote.
     */
    fun <F> execute(
        lifecycleProvider: LifecycleProvider<*>? = null,
        block: Single<T>.() -> SingleSource<F>,
        singleObserver: SingleObserver<F>
    ) = buildUseCaseSingle(block).compose(lifecycleProvider?.bindToLifecycle()).subscribe(singleObserver)

    /**
     * Executes the current use case with request [parameter].
     *
     * @param parameter the parameter for retrieving data.
     * @param lifecycleProvider the life cycle provider for cutting RxJava runs.
     * @param block add some chain actions between [subscribeOn] and [observeOn].
     * @param singleObserver a reaction of [Single] from presentation, the data are omitted from database or remote.
     */
    fun <F> execute(
        parameter: R,
        lifecycleProvider: LifecycleProvider<*>? = null,
        block: Single<T>.() -> SingleSource<F>,
        singleObserver: SingleObserver<F>
    ) {
        requestValues = parameter
        execute(lifecycleProvider, block, singleObserver)
    }

    /**
     * Executes the current use case with an anonymous function.
     *
     * @param lifecycleProvider an activity or a fragment of the [LifecycleProvider] object.
     * @param block add some chain actions between [subscribeOn] and [observeOn].
     * @param singleObserver a reaction of [Single] from presentation, the data are omitted from
     *                       database or remote.
     */
    fun <F> execute(
        lifecycleProvider: LifecycleProvider<*>? = null,
        block: Single<T>.() -> SingleSource<F>,
        singleObserver: SinglePlugin<F>.() -> Unit
    ) = execute(lifecycleProvider, block, SinglePlugin<F>().apply(singleObserver))

    /**
     * Executes the current use case with request [parameter] with an anonymous function..
     *
     * @param parameter the parameter for retrieving data.
     * @param lifecycleProvider an activity or a fragment of the [LifecycleProvider] object.
     * @param block add some chain actions between [subscribeOn] and [observeOn].
     * @param singleObserver a reaction of [Single] from presentation, the data are omitted from database or remote.
     */
    fun <F> execute(
        parameter: R,
        lifecycleProvider: LifecycleProvider<*>? = null,
        block: Single<T>.() -> SingleSource<F>,
        singleObserver: SinglePlugin<F>.() -> Unit
    ) {
        requestValues = parameter
        execute(lifecycleProvider, block, singleObserver)
    }

    /**
     * Build an [Single] which will be used when executing the current [SingleUseCase].
     * There is a [io.reactivex.internal.operators.observable.ObservableSubscribeOn] for fetching
     * the data from the [smash.ks.com.domain.repositories.DataRepository] works on the new thread
     * so after [io.reactivex.internal.operators.observable.ObservableSubscribeOn]'s chain function
     * will be ran on the same thread.
     * This is for who needs transfer the thread to UI, IO, or new thread again.
     *
     * @param block add some chain actions between [subscribeOn] and [observeOn].
     * @return [Single] for connecting with a [SingleObserver] from the kotlin layer.
     */
    private fun <F> buildUseCaseSingle(block: (Single<T>.() -> SingleSource<F>)) =
        fetchUseCase()
            .subscribeOn(subscribeScheduler)
            .compose(block)
            .observeOn(observeScheduler)
    //endregion

    //region UseCase without an anonymous function.
    /**
     * Executes the current use case.
     *
     * @param lifecycleProvider the life cycle provider for cutting RxJava runs.
     * @param observer a reaction of [SingleObserver] from presentation, the data are omitted
     *                 from database or remote.
     */
    fun execute(lifecycleProvider: LifecycleProvider<*>? = null, observer: SingleObserver<T>) =
        buildUseCaseSingle().compose(lifecycleProvider?.bindToLifecycle()).subscribe(observer)

    /**
     * Executes the current use case with request [parameter].
     *
     * @param parameter the parameter for retrieving data.
     * @param lifecycleProvider the life cycle provider for cutting RxJava runs.
     * @param observer a reaction of [SingleObserver] from presentation, the data are omitted from
     *                 database or remote.
     */
    fun execute(parameter: R, lifecycleProvider: LifecycleProvider<*>? = null, observer: SingleObserver<T>) {
        requestValues = parameter
        execute(lifecycleProvider, observer)
    }

    /**
     * Executes the current use case.
     *
     * @param lifecycleProvider an activity or a fragment of the [LifecycleProvider] object.
     * @param observer a reaction of [SingleObserver] from presentation, the data are omitted from database or remote.
     */
    fun execute(lifecycleProvider: LifecycleProvider<*>? = null, observer: SinglePlugin<T>.() -> Unit) =
        execute(lifecycleProvider, SinglePlugin<T>().apply(observer))

    /**
     * Executes the current use case with request [parameter].
     *
     * @param parameter the parameter for retrieving data.
     * @param lifecycleProvider an activity or a fragment of the [LifecycleProvider] object.
     * @param observer a reaction of [SingleObserver] from presentation, the data are omitted from database or remote.
     */
    fun execute(parameter: R, lifecycleProvider: LifecycleProvider<*>? = null, observer: SinglePlugin<T>.() -> Unit) {
        requestValues = parameter
        execute(lifecycleProvider, observer)
        fetchUseCase()
    }

    private fun buildUseCaseSingle() = fetchUseCase().compose(singleTransferSchedule())
    //endregion

    /**
     * Choose a method from [smash.ks.com.data.datastores.DataStore] and fit this use case
     * for return some data.
     *
     * @return an [Single] for chaining on working threads.
     */
    abstract fun fetchUseCase(): Single<T>
}

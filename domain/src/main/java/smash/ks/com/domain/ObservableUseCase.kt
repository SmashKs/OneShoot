/*
 * Copyright (C) 2019 The Smash Ks Open Project
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

import com.devrapid.kotlinshaver.rxjava2.ObserverPlugin
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Observable
import io.reactivex.Observer
import smash.ks.com.domain.executors.PostExecutionThread
import smash.ks.com.domain.executors.ThreadExecutor

/**
 * Abstract class for a Use Case [Observable] (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case in the
 * application should implement this contract).
 *
 * By convention each UseCase implementation will return the result using a [org.reactivestreams.Subscriber]
 * that will execute its job in a background thread and will post the result in the UI thread.
 *
 * For passing a request parameters [smash.ks.com.domain.BaseUseCase.RequestValues] to data layer
 * that set a generic type for wrapping vary data.
 */
abstract class ObservableUseCase<T, R : BaseUseCase.RequestValues>(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BaseUseCase<R>(threadExecutor, postExecutionThread) {
    //region UseCase with an anonymous function.
    /**
     * Executes the current use case.
     *
     * @param lifecycleProvider the life cycle provider for cutting RxJava runs.
     * @param block add some chain actions between [subscribeOn] and [observeOn].
     * @param observer a reaction of [Observer] from presentation, the data are omitted from database or remote.
     */
    fun <F> execute(
        lifecycleProvider: MaybeLifeProvider = null,
        block: ExtraObservableOpOnBkg<T, F>,
        observer: Observer<F>
    ) = buildUseCaseObservable(block).compose(lifecycleProvider?.bindToLifecycle()).subscribe(observer)

    /**
     * Executes the current use case with request [parameter].
     *
     * @param parameter the parameter for retrieving data.
     * @param lifecycleProvider the life cycle provider for cutting RxJava runs.
     * @param block add some chain actions between [subscribeOn] and [observeOn].
     * @param observer a reaction of [Observer] from presentation, the data are omitted from database or remote.
     */
    fun <F> execute(
        parameter: R,
        lifecycleProvider: MaybeLifeProvider = null,
        block: ExtraObservableOpOnBkg<T, F>,
        observer: Observer<F>
    ) {
        requestValues = parameter
        execute(lifecycleProvider, block, observer)
    }

    /**
     * Executes the current use case with an anonymous function.
     *
     * @param lifecycleProvider an activity or a fragment of the [LifecycleProvider] object.
     * @param block add some chain actions between [subscribeOn] and [observeOn].
     * @param observer a reaction of [ObserverPlugin] from presentation, the data are omitted from database or remote.
     */
    fun <F> execute(
        lifecycleProvider: MaybeLifeProvider = null,
        block: ExtraObservableOpOnBkg<T, F>,
        observer: ExtraObservableOpOnUi<F>
    ) = execute(lifecycleProvider, block, ObserverPlugin<F>().apply(observer))

    /**
     * Executes the current use case with request [parameter] with an anonymous function..
     *
     * @param parameter the parameter for retrieving data.
     * @param lifecycleProvider an activity or a fragment of the [LifecycleProvider] object.
     * @param block add some chain actions between [subscribeOn] and [observeOn].
     * @param observer a reaction of [ObserverPlugin] from presentation, the data are omitted from database or remote.
     */
    fun <F> execute(
        parameter: R,
        lifecycleProvider: MaybeLifeProvider = null,
        block: ExtraObservableOpOnBkg<T, F>,
        observer: ExtraObservableOpOnUi<F>
    ) {
        requestValues = parameter
        execute(lifecycleProvider, block, observer)
    }

    /**
     * Build an [Observable] which will be used when executing the current [ObservableUseCase].
     * There is a [io.reactivex.internal.operators.observable.ObservableSubscribeOn] for fetching
     * the data from the [smash.ks.com.domain.repositories.DataRepository] works on the new thread
     * so after [io.reactivex.internal.operators.observable.ObservableSubscribeOn]'s chain function
     * will be ran on the same thread.
     * This is for who needs transfer the thread to UI, IO, or new thread again.
     *
     * @param block add some chain actions between [subscribeOn] and [observeOn].
     * @return [Observable] for connecting with a [Observer] from the kotlin layer.
     */
    private fun <F> buildUseCaseObservable(block: ExtraObservableOpOnBkg<T, F>) =
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
     * @param observer a reaction of [Observer] from presentation, the data are omitted from database or remote.
     */
    fun execute(lifecycleProvider: MaybeLifeProvider = null, observer: Observer<T>) =
        buildUseCaseObservable().compose(lifecycleProvider?.bindToLifecycle()).subscribe(observer)

    /**
     * Executes the current use case with request [parameter].
     *
     * @param parameter the parameter for retrieving data.
     * @param lifecycleProvider the life cycle provider for cutting RxJava runs.
     * @param observer a reaction of [Observer] from presentation, the data are omitted from database or remote.
     */
    fun execute(parameter: R, lifecycleProvider: MaybeLifeProvider = null, observer: Observer<T>) {
        requestValues = parameter
        execute(lifecycleProvider, observer)
    }

    /**
     * Executes the current use case.
     *
     * @param lifecycleProvider an activity or a fragment of the [LifecycleProvider] object.
     * @param observer a reaction of [ObserverPlugin] from presentation, the data are omitted from database or remote.
     */
    fun execute(lifecycleProvider: MaybeLifeProvider = null, observer: ExtraObservableOpOnUi<T>) =
        execute(lifecycleProvider, ObserverPlugin<T>().apply(observer))

    /**
     * Executes the current use case with request [parameter].
     *
     * @param parameter the parameter for retrieving data.
     * @param lifecycleProvider an activity or a fragment of the [LifecycleProvider] object.
     * @param observer a reaction of [ObserverPlugin] from presentation, the data are omitted from database or remote.
     */
    fun execute(parameter: R, lifecycleProvider: MaybeLifeProvider = null, observer: ExtraObservableOpOnUi<T>) {
        requestValues = parameter
        execute(lifecycleProvider, observer)
        fetchUseCase()
    }

    /**
     * Build an [Observable] which will be used when executing the current [ObservableUseCase] and run on
     * the UI thread.
     *
     * @return [Observable] for connecting with a [Observer] from the kotlin layer.
     */
    private fun buildUseCaseObservable() = fetchUseCase().compose(observableTransferSchedule())
    //endregion

    /**
     * Choose a method from [smash.ks.com.data.datastores.DataStore] and fit this use case
     * for return some data.
     *
     * @return an [Observer] for chaining on working threads.
     */
    abstract fun fetchUseCase(): Observable<T>
}

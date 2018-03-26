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

package smash.ks.com.oneshoot.ext.usecase

import com.devrapid.kotlinshaver.CompletablePlugin
import com.devrapid.kotlinshaver.ObserverPlugin
import com.devrapid.kotlinshaver.SinglePlugin
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import smash.ks.com.domain.BaseUseCase
import smash.ks.com.domain.CompletableUseCase
import smash.ks.com.domain.ObservableUseCase
import smash.ks.com.domain.SingleUseCase

//region Observable
fun <T, F, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: ObservableUseCase<T, V>,
    parameter: V,
    block: Observable<T>.() -> Observable<F>,
    observer: ObserverPlugin<F>.() -> Unit
) = usecase.execute(parameter, this, block, observer)

fun <T, F, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: ObservableUseCase<T, V>,
    block: Observable<T>.() -> Observable<F>,
    observer: ObserverPlugin<F>.() -> Unit
) = usecase.execute(this, block, observer)

fun <T, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: ObservableUseCase<T, V>,
    parameter: V,
    observer: ObserverPlugin<T>.() -> Unit
) = usecase.execute(parameter, this, observer)

fun <T, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: ObservableUseCase<T, V>,
    observer: ObserverPlugin<T>.() -> Unit
) = usecase.execute(this, observer)
//endregion

//region Single
fun <T, F, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: SingleUseCase<T, V>,
    parameter: V,
    block: Single<T>.() -> Single<F>,
    singleObserver: SinglePlugin<F>.() -> Unit
) = usecase.execute(parameter, this, block, singleObserver)

fun <T, F, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: SingleUseCase<T, V>,
    block: Single<T>.() -> Single<F>,
    singleObserver: SinglePlugin<F>.() -> Unit
) = usecase.execute(this, block, singleObserver)

fun <T, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: SingleUseCase<T, V>,
    parameter: V,
    singleObserver: SinglePlugin<T>.() -> Unit
) = usecase.execute(parameter, this, singleObserver)

fun <T, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: SingleUseCase<T, V>,
    singleObserver: SinglePlugin<T>.() -> Unit
) = usecase.execute(this, singleObserver)
//endregion

//region Completable
fun <V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: CompletableUseCase<V>,
    parameter: V,
    block: Completable.() -> Completable,
    completableObserver: CompletablePlugin.() -> Unit
) = usecase.execute(parameter, this, block, completableObserver)

fun <V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: CompletableUseCase<V>,
    block: Completable.() -> Completable,
    completableObserver: CompletablePlugin.() -> Unit
) = usecase.execute(this, block, completableObserver)

fun <V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: CompletableUseCase<V>,
    parameter: V,
    completableObserver: CompletablePlugin.() -> Unit
) = usecase.execute(parameter, this, completableObserver)

fun <V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: CompletableUseCase<V>,
    completableObserver: CompletablePlugin.() -> Unit
) = usecase.execute(this, completableObserver)
//endregion

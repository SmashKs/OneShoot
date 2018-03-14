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

import com.devrapid.kotlinshaver.ObserverPlugin
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Observable
import smash.ks.com.domain.usecases.BaseUseCase

fun <T, F, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: BaseUseCase<T, V>,
    parameter: V,
    block: Observable<T>.() -> Observable<F>,
    observer: ObserverPlugin<F>.() -> Unit
) = usecase.execute(parameter, this, block, observer)

fun <T, F, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: BaseUseCase<T, V>,
    block: Observable<T>.() -> Observable<F>,
    observer: ObserverPlugin<F>.() -> Unit
) = usecase.execute(this, block, observer)

fun <T, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: BaseUseCase<T, V>,
    parameter: V,
    observer: ObserverPlugin<T>.() -> Unit
) = usecase.execute(parameter, this, observer)

fun <T, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: BaseUseCase<T, V>,
    observer: ObserverPlugin<T>.() -> Unit
) = usecase.execute(this, observer)
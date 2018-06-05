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

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import smash.ks.com.domain.ObservableUseCase
import smash.ks.com.domain.SingleUseCase
import smash.ks.com.domain.datas.KsResponse

// Reactive block with Function Parameter

typealias ObservableTransformer<T, F> = Observable<T>.() -> Observable<F>
typealias SingleTransformer<T, U> = Single<T>.() -> Single<U>
typealias CompletableTransformer = Completable.() -> Completable

typealias ObservableCaseWithResponse<D, V> = ObservableUseCase<KsResponse<D>, V>
typealias SingleCaseWithResponse<D, V> = SingleUseCase<KsResponse<D>, V>

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
import com.devrapid.kotlinshaver.ObserverPlugin
import com.devrapid.kotlinshaver.SinglePlugin
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.SingleSource
import smash.ks.com.domain.models.KsModel
import smash.ks.com.domain.models.LabelModel
import smash.ks.com.domain.models.UploadResultModel
import smash.ks.com.domain.models.response.KsResponse
import smash.ks.com.ext.const.LookUp

// Generic with Type Parameters

typealias Parameters = LookUp<String>
typealias AnyParameters = HashMap<String, Any>

// Usecase with Function Parameters

typealias ExtraObservableOpOnBkg<T, F> = Observable<T>.() -> ObservableSource<F>
typealias ExtraSingleOpOnBkg<T, F> = Single<T>.() -> SingleSource<F>
typealias ExtraCompletableOpOnBkg = Completable.() -> CompletableSource
typealias MaybeLifeProvider = LifecycleProvider<*>?
typealias ExtraObservableOpOnUi<T> = ObserverPlugin<T>.() -> Unit
typealias ExtraSingleOpOnUi<T> = SinglePlugin<T>.() -> Unit
typealias ExtraCompletableOpOnUi = CompletablePlugin.() -> Unit

// Parameters

typealias ResponseKsModel = KsResponse<KsModel>
typealias ResponseKsLabels = KsResponse<Labels>
typealias ResponseUploadResult = KsResponse<UploadResultModel>
typealias Label = String
typealias Tag = String
typealias Labels = List<LabelModel>

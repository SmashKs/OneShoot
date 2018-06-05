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

@file:Suppress("NOTHING_TO_INLINE")

package smash.ks.com.oneshoot.ext.usecase

import com.trello.rxlifecycle2.LifecycleProvider
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.rx2.await
import kotlinx.coroutines.experimental.rx2.awaitSingle
import smash.ks.com.domain.BaseUseCase
import smash.ks.com.domain.CompletableUseCase
import smash.ks.com.domain.ExtraCompletableOpOnUi
import smash.ks.com.domain.ExtraObservableOpOnUi
import smash.ks.com.domain.ExtraSingleOpOnUi
import smash.ks.com.domain.ObservableUseCase
import smash.ks.com.domain.SingleUseCase
import smash.ks.com.domain.datas.Data
import smash.ks.com.domain.datas.KsResponse
import smash.ks.com.domain.datas.KsResponse.Error
import smash.ks.com.domain.datas.KsResponse.Success
import smash.ks.com.oneshoot.entities.Entity
import smash.ks.com.oneshoot.entities.mappers.Mapper

//region ========== Those methods might be deprecated. ==========
//region Observable
fun <T, F, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: ObservableUseCase<T, V>,
    parameter: V,
    block: ObservableTransformer<T, F>,
    observer: ExtraObservableOpOnUi<F>
) = usecase.execute(parameter, this, block, observer)

fun <T, F, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: ObservableUseCase<T, V>,
    block: ObservableTransformer<T, F>,
    observer: ExtraObservableOpOnUi<F>
) = usecase.execute(this, block, observer)

fun <T, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: ObservableUseCase<T, V>,
    parameter: V,
    observer: ExtraObservableOpOnUi<T>
) = usecase.execute(parameter, this, observer)

fun <T, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: ObservableUseCase<T, V>,
    observer: ExtraObservableOpOnUi<T>
) = usecase.execute(this, observer)
//endregion

//region Single
fun <T, F, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: SingleUseCase<T, V>,
    parameter: V,
    block: SingleTransformer<T, F>,
    singleObserver: ExtraSingleOpOnUi<F>
) = usecase.execute(parameter, this, block, singleObserver)

fun <T, F, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: SingleUseCase<T, V>,
    block: SingleTransformer<T, F>,
    singleObserver: ExtraSingleOpOnUi<F>
) = usecase.execute(this, block, singleObserver)

fun <T, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: SingleUseCase<T, V>,
    parameter: V,
    singleObserver: ExtraSingleOpOnUi<T>
) = usecase.execute(parameter, this, singleObserver)

fun <T, V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: SingleUseCase<T, V>,
    singleObserver: ExtraSingleOpOnUi<T>
) = usecase.execute(this, singleObserver)
//endregion

//region Completable
fun <V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: CompletableUseCase<V>,
    parameter: V,
    block: CompletableTransformer,
    completableObserver: ExtraCompletableOpOnUi
) = usecase.execute(parameter, this, block, completableObserver)

fun <V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: CompletableUseCase<V>,
    block: CompletableTransformer,
    completableObserver: ExtraCompletableOpOnUi
) = usecase.execute(this, block, completableObserver)

fun <V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: CompletableUseCase<V>,
    parameter: V,
    completableObserver: ExtraCompletableOpOnUi
) = usecase.execute(parameter, this, completableObserver)

fun <V : BaseUseCase.RequestValues, E> LifecycleProvider<E>.execute(
    usecase: CompletableUseCase<V>,
    completableObserver: ExtraCompletableOpOnUi
) = usecase.execute(this, completableObserver)
//endregion
//endregion

//region Observable
/**
 * Connected [ObservableUseCase] and unwrapping and letting the usecase become a async
 * [kotlinx.coroutines.experimental.Deferred] object.
 */
fun <D : Data, V : BaseUseCase.RequestValues> ObservableCaseWithResponse<D, V>.ayncCase(
    parameter: V? = null
) = async { this@ayncCase.apply { requestValues = parameter }.fetchUseCase() }

/**
 * Connected [ObservableUseCase] and unwrapping and letting the usecase become a await
 * [kotlinx.coroutines.experimental.Deferred] object with the mapper.
 *
 * @param mapper the mapper for translating from [Data] to [Entity].
 * @param parameter the usecase's parameter.
 */
suspend fun <D : Data, E : Entity, V : BaseUseCase.RequestValues> ObservableCaseWithResponse<D, V>.toAwait(
    mapper: Mapper<D, E>,
    parameter: V? = null
) = async {
    this@toAwait.apply { requestValues = parameter }.fetchUseCase().awaitSingle().run { mapToEntity(mapper) }
}

/**
 * Connected [ObservableUseCase] and unwrapping and letting the usecase become a await
 * [kotlinx.coroutines.experimental.Deferred] object without the mapper (Because the
 * variables should be primitive variable).
 *
 * @param parameter the usecase's parameter.
 */
suspend fun <D : Any, V : BaseUseCase.RequestValues> ObservableCaseWithResponse<D, V>.toAwait(
    parameter: V? = null
) = async { this@toAwait.apply { requestValues = parameter }.fetchUseCase().awaitSingle() }
//endregion

//region Single
/**
 * Connected [SingleUseCase] and unwrapping and letting the usecase become a async
 * [kotlinx.coroutines.experimental.Deferred] object.
 */
fun <D : Data, V : BaseUseCase.RequestValues> SingleCaseWithResponse<D, V>.ayncCase(
    parameter: V? = null
) = async { this@ayncCase.apply { requestValues = parameter }.fetchUseCase() }

/**
 * Connected [SingleUseCase] and unwrapping and letting the usecase become a await
 * [kotlinx.coroutines.experimental.Deferred] object. with the mapper.
 *
 * @param mapper the mapper for translating from [Data] to [Entity].
 * @param parameter the usecase's parameter.
 */
suspend fun <D : Data, E : Entity, V : BaseUseCase.RequestValues> SingleCaseWithResponse<D, V>.toAwait(
    mapper: Mapper<D, E>,
    parameter: V? = null
) = async {
    this@toAwait
        .apply { requestValues = parameter }
        .fetchUseCase()
        .await()
        .run { mapToEntity(mapper) }
}

/**
 * Connected [SingleUseCase] and unwrapping and letting the usecase become a await
 * [kotlinx.coroutines.experimental.Deferred] object. without the mapper (Because the
 * variables should be primitive variable).
 *
 * @param parameter the usecase's parameter.
 */
suspend fun <D : Any, V : BaseUseCase.RequestValues> SingleCaseWithResponse<D, V>.toAwait(
    parameter: V? = null
) = async { this@toAwait.apply { requestValues = parameter }.fetchUseCase().await() }
//endregion

/**
 * A mapper which unboxing the [KsResponse]<[Data]> then getting items we needs. Make a [KsResponse]
 * again and boxing the [Entity] which mapping from [Data] to [Entity] to be a [KsResponse]<[Entity]>.
 */
private fun <D : Data, E : Entity> KsResponse<D>.mapToEntity(mapper: Mapper<D, E>) =
    data?.let(mapper::toEntityFrom)?.wrapInSuccess() ?: "No response result".wrapInError<E>()

//region Completable
/**
 * Connected [CompletableUseCase] and unwrapping and letting the usecase become a async
 * [kotlinx.coroutines.experimental.Deferred] object.
 */
fun <V : BaseUseCase.RequestValues> CompletableUseCase<V>.ayncCase(
    parameter: V? = null
) = async { this@ayncCase.apply { requestValues = parameter }.fetchUseCase() }

/**
 * Connected [CompletableUseCase] and unwrapping and letting the usecase become a await
 * [kotlinx.coroutines.experimental.Deferred] object.
 */
suspend fun <V : BaseUseCase.RequestValues> CompletableUseCase<V>.toAwait(
    parameter: V? = null
) = async { this@toAwait.apply { requestValues = parameter }.fetchUseCase().await().let { Success(it) } }
//endregion

/**
 * Wrapping the [this] into [Success].
 */
private inline fun <E> E.wrapInSuccess() = Success(this)

/**
 * Wrapping the [String] msg into [Error].
 */
private inline fun <E> String.wrapInError() = Error<E>(msg = this)

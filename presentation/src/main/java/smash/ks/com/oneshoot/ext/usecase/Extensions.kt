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
import smash.ks.com.domain.models.KsResponse
import smash.ks.com.domain.models.KsResponse.Error
import smash.ks.com.domain.models.KsResponse.Success
import smash.ks.com.domain.models.Model
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
fun <M : Model, V : BaseUseCase.RequestValues> ObservableCaseWithResponse<M, V>.ayncCase(
    parameter: V? = null
) = async { this@ayncCase.apply { requestValues = parameter }.fetchUseCase() }

/**
 * Connected [ObservableUseCase] and unwrapping and letting the usecase become a await
 * [kotlinx.coroutines.experimental.Deferred] object with the mapper.
 *
 * @param mapper the mapper for translating from [Model] to [Entity].
 * @param parameter the usecase's parameter.
 */
suspend fun <M : Model, E : Entity, V : BaseUseCase.RequestValues> ObservableCaseWithResponse<M, V>.toAwait(
    mapper: Mapper<M, E>,
    parameter: V? = null
) = async {
    this@toAwait.apply { requestValues = parameter }.fetchUseCase().awaitSingle().run { mapToEntity(mapper) }
}

/**
 * Connected [ObservableUseCase] and unwrapping and letting the usecase become a await
 * [kotlinx.coroutines.experimental.Deferred] object. with the mapper.
 *
 * @param mapper the mapper for translating from List<[Model]> to List<[Entity]>.
 * @param parameter the usecase's parameter.
 */
suspend fun <M : Model, E : Entity, V : BaseUseCase.RequestValues, MS : List<M>> ObservableCaseWithResponse<MS, V>.toListAwait(
    mapper: Mapper<M, E>,
    parameter: V? = null
) = async {
    this@toListAwait
        .apply { requestValues = parameter }
        .fetchUseCase()
        .awaitSingle()
        .run { mapToEntities(mapper) }
}

/**
 * Connected [ObservableUseCase] and unwrapping and letting the usecase become a await
 * [kotlinx.coroutines.experimental.Deferred] object without the mapper (Because the
 * variables should be primitive variable).
 *
 * @param parameter the usecase's parameter.
 */
suspend fun <M : Any, V : BaseUseCase.RequestValues> ObservableCaseWithResponse<M, V>.toAwait(
    parameter: V? = null
) = async { this@toAwait.apply { requestValues = parameter }.fetchUseCase().awaitSingle() }
//endregion

//region Single
/**
 * Connected [SingleUseCase] and unwrapping and letting the usecase become a async
 * [kotlinx.coroutines.experimental.Deferred] object.
 */
fun <M : Model, V : BaseUseCase.RequestValues> SingleCaseWithResponse<M, V>.ayncCase(
    parameter: V? = null
) = async { this@ayncCase.apply { requestValues = parameter }.fetchUseCase() }

/**
 * Connected [SingleUseCase] and unwrapping and letting the usecase become a await
 * [kotlinx.coroutines.experimental.Deferred] object. with the mapper.
 *
 * @param mapper the mapper for translating from [Model] to [Entity].
 * @param parameter the usecase's parameter.
 */
suspend fun <M : Model, E : Entity, V : BaseUseCase.RequestValues> SingleCaseWithResponse<M, V>.toAwait(
    mapper: Mapper<M, E>,
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
 * [kotlinx.coroutines.experimental.Deferred] object. with the mapper.
 *
 * @param mapper the mapper for translating from List<[Model]> to List<[Entity]>.
 * @param parameter the usecase's parameter.
 */
suspend fun <M : Model, E : Entity, V : BaseUseCase.RequestValues, MS : List<M>> SingleCaseWithResponse<MS, V>.toListAwait(
    mapper: Mapper<M, E>,
    parameter: V? = null
) = async {
    this@toListAwait
        .apply { requestValues = parameter }
        .fetchUseCase()
        .await()
        .run { mapToEntities(mapper) }
}

/**
 * Connected [SingleUseCase] and unwrapping and letting the usecase become a await
 * [kotlinx.coroutines.experimental.Deferred] object. without the mapper (Because the
 * variables should be primitive variable).
 *
 * @param parameter the usecase's parameter.
 */
suspend fun <M : Any, V : BaseUseCase.RequestValues> SingleCaseWithResponse<M, V>.toAwait(
    parameter: V? = null
) = async { this@toAwait.apply { requestValues = parameter }.fetchUseCase().await() }
//endregion

/**
 * A mapper which unboxing the [KsResponse]<[Model]> then getting items we needs. Make a [KsResponse]
 * again and boxing the [Entity] which mapping from [Model] to [Entity] to be a [KsResponse]<[Entity]>.
 */
private fun <M : Model, E : Entity> KsResponse<M>.mapToEntity(mapper: Mapper<M, E>) =
    data?.let(mapper::toEntityFrom)?.wrapInSuccess() ?: "No response result".wrapInError<E>()

/**
 * A mapper which unboxing the [KsResponse]<List<[Model]>> then getting items we needs. Make a [KsResponse]
 * again and boxing the List<[Entity]> which mapping from List<[Model]> to List<[Entity]> to be a
 * [KsResponse]<List<[Entity]>>.
 */
private fun <M : Model, E : Entity, MS : List<M>> KsResponse<MS>.mapToEntities(mapper: Mapper<M, E>) =
    data?.map(mapper::toEntityFrom)?.wrapListInSuccess() ?: "No response result".wrapInError<List<E>>()

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
 * Wrapping the List<[this]> into [Success].
 */
private inline fun <E, ES : List<E>> ES.wrapListInSuccess() = Success(this)

/**
 * Wrapping the [String] msg into [Error].
 */
private inline fun <E> String.wrapInError() = Error<E>(msg = this)

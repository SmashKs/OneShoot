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

package smash.ks.com.domain.usecases.user

import smash.ks.com.domain.executors.PostExecutionThread
import smash.ks.com.domain.executors.ThreadExecutor
import smash.ks.com.domain.objects.KsObject
import smash.ks.com.domain.repostories.DataRepository
import smash.ks.com.domain.usecases.BaseUseCase
import smash.ks.com.domain.usecases.user.GetUserUsecase.Requests

/**
 * This class is an implementation of [BaseUseCase] that represents a use case for an example.
 */
class GetUserUsecase(
    private val repository: DataRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BaseUseCase<KsObject, Requests>(threadExecutor, postExecutionThread) {
    override fun fetchUsecase() = TODO()

    /** Wrapping data requests for general situation.*/
    class Requests(val obj: KsObject) : RequestValues
}
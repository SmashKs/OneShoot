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

package smash.ks.com.oneshoot.features.main

import smash.ks.com.domain.objects.KsObject
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.usecases.GetKsImageCase
import smash.ks.com.domain.usecases.fake.GetKsImageUsecase.Requests
import smash.ks.com.oneshoot.entities.KsEntity
import smash.ks.com.oneshoot.entities.mappers.Mapper
import smash.ks.com.oneshoot.ext.coroutine.ui
import smash.ks.com.oneshoot.ext.usecase.awaitCase
import smash.ks.com.oneshoot.mvp.contracts.MainContract

class MainFragmentPresenter(
    private val getKsImageCase: GetKsImageCase,
    private val mapper: Mapper<KsObject, KsEntity>
) : MainContract.Presenter() {
    override fun obtainImageUri(imageId: Int) {
        view.showLoading()
        ui {
            val entity = getKsImageCase.awaitCase(mapper, Requests(KsParam(imageId)))

            view.showImageUri(entity.await().uri)
            view.hideLoading()
        }

//        lifecycleProvider.execute(getKsImageCase, Requests(KsParam(imageId))) {
//            onSuccess {
//                view.showImageUri(mapper.toEntityFrom(it).uri)
//                view.hideLoading()
//            }
//            onError { view.showError("Something went to wrong.") }
//        }
    }
}
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

package smash.ks.com.oneshoot.internal.di.modules.dependencies

import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton
import smash.ks.com.domain.usecases.GetImageContentWordsCase
import smash.ks.com.domain.usecases.GetImageTagsCase
import smash.ks.com.domain.usecases.GetKsImageCase
import smash.ks.com.domain.usecases.UploadImageToFirebaseCase
import smash.ks.com.domain.usecases.analysis.GetImageContentWordsUsecase
import smash.ks.com.domain.usecases.analysis.GetImageTagsUsecase
import smash.ks.com.domain.usecases.fake.GetKsImageUsecase
import smash.ks.com.domain.usecases.upload.UploadImageToFirebaseUsecase
import smash.ks.com.oneshoot.internal.di.scope.fragmentScope

object UsecaseModule {
    fun usecaseModule() = Module {
        //region For Fragments
        //region Fake
        bind<GetKsImageCase>() with scoped(fragmentScope).singleton {
            GetKsImageUsecase(instance(), instance(), instance())
        }
        //endregion

        bind<GetImageTagsCase>() with scoped(fragmentScope).singleton {
            GetImageTagsUsecase(instance(), instance(), instance())
        }

        bind<GetImageContentWordsCase>() with scoped(fragmentScope).singleton {
            GetImageContentWordsUsecase(instance(), instance(), instance())
        }

        bind<UploadImageToFirebaseCase>() with scoped(fragmentScope).singleton {
            UploadImageToFirebaseUsecase(instance(), instance(), instance())
        }
        //endregion
    }
}
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
import smash.ks.com.domain.usecases.SaveKsImageCase
import smash.ks.com.domain.usecases.UploadImageToFirebaseCase
import smash.ks.com.domain.usecases.analysis.FindImageContentWordsUsecase
import smash.ks.com.domain.usecases.analysis.FindImageTagsUsecase
import smash.ks.com.domain.usecases.fake.FindKsImageUsecase
import smash.ks.com.domain.usecases.fake.PersistKsImageUsecase
import smash.ks.com.domain.usecases.upload.UploadImageToFirebaseUsecase
import smash.ks.com.oneshoot.internal.di.scope.fragmentScope

/**
 * To provide the necessary usecase objects for the specific activities or fragments.
 */
object UsecaseModule {
    fun usecaseProvider() = Module("Use Cases Module") {
        //region For Fragments
        //region Fake
        bind<GetKsImageCase>() with scoped(fragmentScope).singleton {
            FindKsImageUsecase(instance(), instance(), instance())
        }

        bind<SaveKsImageCase>() with scoped(fragmentScope).singleton {
            PersistKsImageUsecase(instance(), instance(), instance())
        }
        //endregion

        bind<GetImageTagsCase>() with scoped(fragmentScope).singleton {
            FindImageTagsUsecase(instance(), instance(), instance())
        }

        bind<GetImageContentWordsCase>() with scoped(fragmentScope).singleton {
            FindImageContentWordsUsecase(instance(), instance(), instance())
        }

        bind<UploadImageToFirebaseCase>() with scoped(fragmentScope).singleton {
            UploadImageToFirebaseUsecase(instance(), instance(), instance())
        }
        //endregion
    }
}

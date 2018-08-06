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

package smash.ks.com.oneshoot.internal.di.modules

import android.content.Context
import com.cloudinary.android.MediaManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import smash.ks.com.data.local.ml.Classifier
import smash.ks.com.data.local.services.KsDatabase
import smash.ks.com.data.local.services.KsFlow
import smash.ks.com.data.local.v1.KsDbFlowImpl
import smash.ks.com.data.local.v1.KsFlowImpl
import smash.ks.com.data.remote.RestfulApiFactory
import smash.ks.com.data.remote.config.KsConfig
import smash.ks.com.data.remote.services.KsCloudinary
import smash.ks.com.data.remote.services.KsFirebase
import smash.ks.com.data.remote.services.KsService
import smash.ks.com.data.remote.v2.KsCloudinaryImpl
import smash.ks.com.data.remote.v2.KsFirebaseImpl
import smash.ks.com.oneshoot.classifiers.TFLiteImageClassifier
import smash.ks.com.oneshoot.internal.di.modules.NetModule.netProvider
import smash.ks.com.oneshoot.internal.di.tag.KsTag.ML_LABEL

/**
 * To provide the necessary objects for the remote/local data store service.
 */
object ServiceModule {
    fun serviceProvider(context: Context) = Module("RESTFul Service Module") {
        //region For the [Remote] data module.
        import(netProvider(context))
        import(firebaseProvider())
        import(cloudinaryProvider())

        // TODO(jieyi): 2018/08/06 This ks service might be separated.
        bind<KsConfig>() with instance(RestfulApiFactory().createKsConfig())
        bind<KsService>() with singleton {
            with(instance<Retrofit.Builder>()) {
                baseUrl(instance<KsConfig>().apiBaseUrl)
                build()
            }.create(KsService::class.java)
        }

        bind<KsFirebase>() with singleton { KsFirebaseImpl(instance(), instance()) }

        bind<KsCloudinary>() with singleton { KsCloudinaryImpl(instance()) }
        //endregion

        //region For the [Local] data module.
        import(tensorFlowProvider(context))

        bind<KsDatabase>() with instance(KsDbFlowImpl())
        bind<KsFlow>() with singleton { KsFlowImpl(instance(ML_LABEL)) }
        //endregion
    }

    //region Remote Providers
    /**
     * To provide the necessary objects [FirebaseDatabase] into the repository.
     */
    private fun firebaseProvider() = Module("Firebase Module") {
        bind<FirebaseDatabase>() with provider { FirebaseDatabase.getInstance() }
        bind<FirebaseVisionLabelDetector>() with provider { FirebaseVision.getInstance().visionLabelDetector }
    }

    /**
     * To provide the necessary objects [MediaManager] into the repository.
     */
    private fun cloudinaryProvider() = Module("Cloudinary Module") {
        bind<MediaManager>() with provider { MediaManager.get() }
    }
    //endregion

    //region Local Providers
    private const val ML_MODEL_FILE = "mobilenet_quant_v1_224.tflite"
    private const val ML_MODEL_LABEL = "mobilenet_quant_v1_224.tflite"
    private const val ML_MODEL_SIZE = 224

    /**
     * To provide the necessary objects [TFLiteImageClassifier] into the repository.
     */
    private fun tensorFlowProvider(context: Context) = Module("TensorFlow Module") {
        bind<Classifier>(ML_LABEL) with singleton {
            TFLiteImageClassifier.create(context.assets, ML_MODEL_FILE, ML_MODEL_LABEL, ML_MODEL_SIZE)
        }
    }
    //endregion
}

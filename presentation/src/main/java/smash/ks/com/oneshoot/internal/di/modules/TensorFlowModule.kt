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
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import smash.ks.com.data.local.ml.Classifier
import smash.ks.com.oneshoot.classifiers.TFLiteImageClassifier
import smash.ks.com.oneshoot.internal.di.tag.KsTag.ML_LABEL

/**
 * To provide the necessary objects into the repository.
 */
object TensorFlowModule {
    private const val ML_MODEL_FILE = "mobilenet_quant_v1_224.tflite"
    private const val ML_MODEL_LABEL = "mobilenet_quant_v1_224.tflite"
    private const val ML_MODEL_SIZE = 224

    fun tensorFlowProvider(context: Context) = Module("TensorFlow Module") {
        bind<Classifier>(ML_LABEL) with singleton {
            TFLiteImageClassifier.create(context.assets, ML_MODEL_FILE, ML_MODEL_LABEL, ML_MODEL_SIZE)
        }
    }
}

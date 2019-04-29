/*
 * Copyright (C) 2019 The Smash Ks Open Project
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

package smash.ks.com.data.local.v1

import android.graphics.BitmapFactory
import com.devrapid.kotlinshaver.rxjava2.single
import smash.ks.com.data.datas.LabelData
import smash.ks.com.data.datas.LabelDatas
import smash.ks.com.data.local.ml.Classifier
import smash.ks.com.data.local.services.KsFlow
import smash.ks.com.domain.parameters.Parameterable
import smash.ks.com.ext.const.DEFAULT_FLOAT
import smash.ks.com.ext.const.DEFAULT_INT

class KsFlowImpl(
    private val classifier: Classifier
) : KsFlow {
    override fun retrieveImageTagsByML(imageByteArray: ByteArray) = single<LabelDatas> {
        val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)

        it.onSuccess(classifier.recognizeImage(bitmap).map {
            LabelData(it.id?.toIntOrNull() ?: DEFAULT_INT,
                      it.title.orEmpty(),
                      it.confidence ?: DEFAULT_FLOAT)
        })
    }

    override fun retrieveImageWordContentByML(params: Parameterable) = throw UnsupportedOperationException()
}

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

package smash.ks.com.data.remote.v2

import android.graphics.BitmapFactory
import com.devrapid.kotlinshaver.completable
import com.devrapid.kotlinshaver.isNotNull
import com.devrapid.kotlinshaver.isNull
import com.devrapid.kotlinshaver.single
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector
import smash.ks.com.data.datas.AlbumData
import smash.ks.com.data.datas.KsData
import smash.ks.com.data.datas.LabelData
import smash.ks.com.data.datas.LabelDatas
import smash.ks.com.data.remote.services.KsFirebase
import smash.ks.com.domain.Label
import smash.ks.com.domain.parameters.Parameterable
import smash.ks.com.ext.extractNumber

/**
 * The implementation for accessing the data from Firebase.
 */
class KsFirebaseImpl constructor(
    private val database: FirebaseDatabase,
    private val detector: FirebaseVisionLabelDetector
) : KsFirebase {
    companion object {
        private const val V2_CHILD_PROPERTIES = "ImageVersion2"
        private const val V2_CHILD_ANONYMOUS = "anonymous"

        private const val V2_CHILD_URI = "uri"
        private const val TO_PERCENT = 100
    }

    private val ref get() = database.reference

    //region Fake
    override fun retrieveImages(name: String) = single<KsData> {
        ref.child(V2_CHILD_PROPERTIES)
            .child(name)
            // FIXME(jieyi): 2018/06/27 Add another listener for getting the all albums.
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val list = dataSnapshot.children.toList().map { it.getValue(AlbumData::class.java) }
                    val firstOfList = list.first()
                    val firstKey = firstOfList?.uris?.keys?.first()
                    val firstUri = firstOfList?.uris?.get(firstKey)

                    firstUri?.run { it.onSuccess(KsData(uri = this)) } ?: it.onError(ClassCastException())
                }

                override fun onCancelled(error: DatabaseError) = it.onError(error.toException())
            })
    }
    //endregion

    override fun uploadImage(params: Parameterable) = completable {
        val root = ref.child(V2_CHILD_PROPERTIES).child(V2_CHILD_ANONYMOUS)
        val key = root.push().key

        if (key.isNull()) {
            it.onError(Exception("There's something wrong."))
            return@completable
        }

        root.child(key!!).setValue(params.toAnyParameter())

        it.onComplete()
    }

    override fun retrieveImageTagsByML(imageByteArray: ByteArray) = single<LabelDatas> { emitter ->
        val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
        val textImage = FirebaseVisionImage.fromBitmap(bitmap)

        detector.detectInImage(textImage).addOnCompleteListener {
            if (it.isSuccessful) {
                it.result.map {
                    "[${it.entityId}]${it.label}: ${it.confidence * TO_PERCENT}%"
                    LabelData(it.entityId.extractNumber().first(), it.label, it.confidence * TO_PERCENT)
                }.let(emitter::onSuccess)
            }
            else if (it.isCanceled)
                it.exception.takeIf(Any?::isNotNull)?.let(emitter::onError)
        }
    }

    override fun retrieveImageWordContentByML(params: Parameterable) = single<Label> { }
}

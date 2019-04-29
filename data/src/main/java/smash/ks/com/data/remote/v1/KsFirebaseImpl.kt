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

package smash.ks.com.data.remote.v1

import com.devrapid.kotlinshaver.castOrNull
import com.devrapid.kotlinshaver.rxjava2.completable
import com.devrapid.kotlinshaver.rxjava2.single
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import smash.ks.com.data.datas.KsData
import smash.ks.com.data.remote.FirebaseData
import smash.ks.com.data.remote.services.KsFirebase
import smash.ks.com.domain.parameters.Parameterable

/**
 * The implementation for accessing the data from Firebase.
 */
class KsFirebaseImpl constructor(private val database: FirebaseDatabase) : KsFirebase {
    companion object {
        private const val V1_CHILD_PROPERTIES = "image_v1"
    }

    private val ref by lazy { database.reference }

    //region Fake
    override fun retrieveImages(name: String) = single<KsData> {
        ref.child(V1_CHILD_PROPERTIES).child(name).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val ti = object : GenericTypeIndicator<FirebaseData>() {}
                val entry = dataSnapshot.children.toList().first().getValue(ti)

                castOrNull<String>(entry?.get("url"))
                    ?.run { it.onSuccess(KsData(uri = this)) } ?: it.onError(ClassCastException())
            }

            override fun onCancelled(error: DatabaseError) = it.onError(error.toException())
        })
    }
    //endregion

    override fun uploadImage(params: Parameterable) = completable {
        ref.child(V1_CHILD_PROPERTIES)

        it.onComplete()
    }

    override fun retrieveImageTagsByML(imageByteArray: ByteArray) = throw UnsupportedOperationException()

    override fun retrieveImageWordContentByML(params: Parameterable) = throw UnsupportedOperationException()
}

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

import com.devrapid.kotlinshaver.completable
import com.devrapid.kotlinshaver.single
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import smash.ks.com.data.models.KsAlbum
import smash.ks.com.data.models.KsModel
import smash.ks.com.data.remote.services.KsFirebase
import smash.ks.com.domain.Label
import smash.ks.com.domain.Labels
import smash.ks.com.domain.parameters.Parameterable
import smash.ks.com.ext.castOrNull

/**
 * The implementation for accessing the data from Firebase.
 */
class KsFirebaseImpl constructor(private val database: FirebaseDatabase) : KsFirebase {
    companion object {
        private const val V2_CHILD_PROPERTIES = "ImageVersion2"
        private const val V2_CHILD_URI = "uri"
    }

    private val ref by lazy { database.reference }

    //region Fake
    override fun retrieveImages(name: String) = single<KsModel> {
        ref.child(V2_CHILD_PROPERTIES)
            .child(name)
            // FIXME(jieyi): 2018/06/27 Add another listener for getting the all albums.
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.toList().forEach {
                        println("------------------------")
                        val t = it.getValue(KsAlbum::class.java)
                        println(t)
                        println("------------------------")
                    }
                    val entry = dataSnapshot.children.toList().first().getValue(String::class.java)

                    castOrNull<String>(entry)
                        ?.run { it.onSuccess(KsModel(uri = this)) } ?: it.onError(ClassCastException())
                }

                override fun onCancelled(error: DatabaseError) = it.onError(error.toException())
            })
    }
    //endregion

    override fun uploadImage(params: Parameterable) = completable {
        ref.child(V2_CHILD_PROPERTIES)

        it.onComplete()
    }

    override fun retrieveImageTagsByML(params: Parameterable) = single<Labels> { }

    override fun retrieveImageWordContentByML(params: Parameterable) = single<Label> { }
}

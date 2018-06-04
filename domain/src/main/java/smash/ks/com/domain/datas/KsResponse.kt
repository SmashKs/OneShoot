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

package smash.ks.com.domain.datas

import com.ks.smash.ext.const.DEFAULT_STR
import smash.ks.com.domain.datas.KsResponse.Error
import smash.ks.com.domain.datas.KsResponse.Loading
import smash.ks.com.domain.datas.KsResponse.Success

/**
 * The super class response from the data layer. There're three result of the response [Loading],
 * [Success], and [Error] for HTTP result.
 */
sealed class KsResponse {
    /**
     * A request is still processing from a remote/local service.
     */
    class Loading<out T : Any>(val data: T? = null) : KsResponse()

    /**
     * A request success getting a result from a remote/local service.
     */
    class Success<out T>(val data: T? = null) : KsResponse()

    /**
     * A request sent then a remote/local service has happened an error.
     */
    class Error<out T>(val data: T? = null, val msg: String = DEFAULT_STR) : KsResponse()
}

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

package smash.ks.com.oneshoot

import net.bytebuddy.utility.RandomString
import smash.ks.com.data.datas.Tag
import java.util.Random
import kotlin.math.absoluteValue

/**
 * Generate a random variable for each data types.
 */
object GeneratorFactory {
    const val zero = 0
    val randomInt get() = Random().nextInt().absoluteValue
    val randomLong get() = Random().nextLong().absoluteValue
    val randomFloat get() = Random().nextFloat().absoluteValue
    val randomDouble get() = Random().nextDouble().absoluteValue
    val randomString: String get() = RandomString.make()
    private const val randomSeed = 20L

    val randomTags: List<Tag>
        get() {
            val list = mutableListOf<Tag>()

            repeat(Random(randomSeed).nextInt()) { list.add(randomString) }

            return list
        }
}

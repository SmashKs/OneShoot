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

package smash.ks.com.oneshoot.widgets.customize.camera.module

import android.os.Parcelable
import android.support.v4.util.SparseArrayCompat
import kotlinx.android.parcel.Parcelize

/**
 * Immutable class for describing proportional relationship between width and height.
 */
@Parcelize
class AspectRatio private constructor(val x: Int, val y: Int) : Comparable<AspectRatio>, Parcelable {
    companion object {
        private val cache by lazy { SparseArrayCompat<SparseArrayCompat<AspectRatio>>(16) }

        /**
         * Returns an instance of [AspectRatio] specified by `x` and `y` values.
         * The values `x` and `` will be reduced by their greatest common divider.
         *
         * @param x The width
         * @param y The height
         * @return An instance of [AspectRatio]
         */
        fun of(x: Int, y: Int): AspectRatio {
            var x = x
            var y = y
            val gcd = gcd(x, y)

            x /= gcd
            y /= gcd

            var arrayX: SparseArrayCompat<AspectRatio>? = cache.get(x)

            if (arrayX == null) {
                val ratio = AspectRatio(x, y)
                arrayX = SparseArrayCompat()
                arrayX.put(y, ratio)
                cache.put(x, arrayX)
                return ratio
            }
            else {
                var ratio: AspectRatio? = arrayX.get(y)
                if (ratio == null) {
                    ratio = AspectRatio(x, y)
                    arrayX.put(y, ratio)
                }
                return ratio
            }
        }

        /**
         * Parse an [AspectRatio] from a [String] formatted like "4:3".
         *
         * @param s The string representation of the aspect ratio
         * @return The aspect ratio
         * @throws IllegalArgumentException when the format is incorrect.
         */
        fun parse(s: String): AspectRatio {
            val position = s.indexOf(':')

            if (position == -1) throw IllegalArgumentException("Malformed aspect ratio: $s")

            try {
                val x = s.substring(0, position).toInt()
                val y = s.substring(position + 1).toInt()
                return of(x, y)
            }
            catch (e: NumberFormatException) {
                throw IllegalArgumentException("Malformed aspect ratio: $s", e)
            }
        }

        private fun gcd(a: Int, b: Int): Int {
            var a = a
            var b = b

            while (b != 0) {
                val c = b
                b = a % b
                a = c
            }

            return a
        }
    }

    override fun equals(other: Any?) = when {
        null == other -> false
        this === other -> true
        other is AspectRatio -> other.x == x && other.y == y
        else -> false
    }

    override fun toString() = "$x : $y"

    override fun hashCode(): Int {
        // assuming most sizes are <2^16, doing a rotate will give us perfect hashing
        return y xor (x shl Integer.SIZE / 2 or x.ushr(Integer.SIZE / 2))
    }

    override fun compareTo(other: AspectRatio) = when {
        equals(other) -> 0
        0 < toFloat() - other.toFloat() -> 1
        else -> -1
    }

    /**
     * @return The inverse of this [AspectRatio].
     */
    fun inverse() = of(y, x)

    fun toFloat() = x.toFloat() / y

    fun matches(size: Size): Boolean {
        val gcd =
            gcd(size.width, size.height)
        val x = size.width / gcd
        val y = size.height / gcd

        return this.x == x && this.y == y
    }
}
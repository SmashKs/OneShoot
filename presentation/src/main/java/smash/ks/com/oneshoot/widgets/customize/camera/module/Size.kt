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

/**
 * Immutable class for describing width and height dimensions in pixels.
 *
 * @property width The width of the size, in pixels
 * @property height The height of the size, in pixels
 */
class Size(
    val width: Int,
    val height: Int
) : Comparable<Size> {
    override fun equals(other: Any?) = when {
        null == other -> false
        this === other -> true
        other is Size -> other.width == width && other.height == height
        else -> false
    }

    override fun toString() = "$width x $height"

    // Assuming most sizes are <2^16, doing a rotate will give us perfect hashing.
    override fun hashCode() = height xor (width shl Integer.SIZE / 2 or width.ushr(Integer.SIZE / 2))

    override fun compareTo(other: Size) = width * height - other.width * other.height
}
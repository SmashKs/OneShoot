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

@file:Suppress("NOTHING_TO_INLINE")

package smash.ks.com.ext.const

const val DEFAULT_INT = -1
const val DEFAULT_LONG = -1L
const val DEFAULT_DOUBLE = -1.0
const val DEFAULT_FLOAT = -1f
const val DEFAULT_STR = ""

inline fun Int.isDefault() = DEFAULT_INT == this
inline fun Long.isDefault() = DEFAULT_LONG == this
inline fun String.isDefault() = DEFAULT_STR == this
inline fun Double.isDefault() = DEFAULT_DOUBLE == this
inline fun Float.isDefault() = DEFAULT_FLOAT == this

inline fun <T> Int.takeIfDefault(block: (Int) -> T) = takeIf(Int::isDefault)?.let(block)
inline fun <T> Long.takeIfDefault(block: (Long) -> T) = takeIf(Long::isDefault)?.let(block)
inline fun <T> String.takeIfDefault(block: (String) -> T) = takeIf(String::isDefault)?.let(block)
inline fun <T> Double.takeIfDefault(block: (Double) -> T) = takeIf(Double::isDefault)?.let(block)
inline fun <T> Float.takeIfDefault(block: (Float) -> T) = takeIf(Float::isDefault)?.let(block)

inline fun <T> Int.takeUnlessDefault(block: (Int) -> T) = takeUnless(Int::isDefault)?.let(block)
inline fun <T> Long.takeUnlessDefault(block: (Long) -> T) = takeUnless(Long::isDefault)?.let(block)
inline fun <T> String.takeUnlessDefault(block: (String) -> T) = takeUnless(String::isDefault)?.let(block)
inline fun <T> Double.takeUnlessDefault(block: (Double) -> T) = takeUnless(Double::isDefault)?.let(block)
inline fun <T> Float.takeUnlessDefault(block: (Float) -> T) = takeUnless(Float::isDefault)?.let(block)

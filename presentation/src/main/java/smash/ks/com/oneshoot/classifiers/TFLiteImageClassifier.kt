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

package smash.ks.com.oneshoot.classifiers

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import smash.ks.com.ext.const.DEFAULT_INT
import smash.ks.com.ext.const.DEFAULT_STR
import smash.ks.com.oneshoot.classifiers.Classifier.Recognition
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Float.compare
import java.lang.Math.min
import java.nio.ByteBuffer
import java.nio.ByteBuffer.allocateDirect
import java.nio.ByteOrder.nativeOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel.MapMode.READ_ONLY
import java.util.ArrayList
import java.util.PriorityQueue
import kotlin.experimental.and

/** A classifier specialized to label images using TensorFlow.  */
class TFLiteImageClassifier private constructor() : Classifier {
    companion object {
        private const val MASK_VALUE = 0xFF
        private const val FIRST_UNIT = 8
        private const val SECOND_UNIT = 16

        private const val MAX_RESULTS = 3
        private const val BATCH_SIZE = 1
        private const val PIXEL_SIZE = 3
        private const val THRESHOLD = 0.1f

        @Throws(IOException::class)
        fun create(assetManager: AssetManager, modelPath: String, labelPath: String, inputSize: Int) =
            TFLiteImageClassifier().apply {
                interpreter = Interpreter(loadModelFile(assetManager, modelPath))
                labelList = loadLabelList(assetManager, labelPath)
                this.inputSize = inputSize
            }
    }

    private var interpreter: Interpreter? = null
    private var inputSize = 0
    private var labelList: List<String>? = null

    override val statString = DEFAULT_STR

    override fun recognizeImage(bitmap: Bitmap): List<Recognition> {
        val byteBuffer = convertBitmapToByteBuffer(bitmap)
        val result = Array(1) { ByteArray(labelList?.size ?: DEFAULT_INT) }

        interpreter?.run(byteBuffer, result) ?: throw NullPointerException()

        return getSortedResult(result)
    }

    override fun close() {
        interpreter?.close()
        interpreter = null
    }

    @Throws(IOException::class)
    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength

        return fileChannel.map(READ_ONLY, startOffset, declaredLength)
    }

    @Throws(IOException::class)
    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        val labelList = ArrayList<String>()

        BufferedReader(InputStreamReader(assetManager.open(labelPath))).useLines { it.forEach { labelList.add(it) } }

        return labelList
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = allocateDirect(BATCH_SIZE * inputSize * inputSize * PIXEL_SIZE)
        byteBuffer.order(nativeOrder())

        val intValues = IntArray(inputSize * inputSize)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var pixel = 0

        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val value = intValues[pixel++]
                byteBuffer.put((value shr SECOND_UNIT and MASK_VALUE).toByte())
                byteBuffer.put((value shr FIRST_UNIT and MASK_VALUE).toByte())
                byteBuffer.put((value and MASK_VALUE).toByte())
            }
        }

        return byteBuffer
    }

    @SuppressLint("DefaultLocale")
    private fun getSortedResult(labelProbArray: Array<ByteArray>): List<Recognition> {
        val pq = PriorityQueue<Recognition>(MAX_RESULTS) { lhs, rhs ->
            compare(rhs.confidence!!, lhs.confidence!!)
        }

        labelList?.let {
            for (i in it.indices) {
                val confidence = (labelProbArray[0][i] and MASK_VALUE.toByte()) / MASK_VALUE.toFloat()

                if (THRESHOLD < confidence) {
                    pq.add(Recognition(i.toString(), if (i < it.size) it[i] else "unknown", confidence))
                }
            }
        }

        val recognitions = ArrayList<Recognition>()
        val recognitionsSize = min(pq.size, MAX_RESULTS)

        (0 until recognitionsSize).forEach { recognitions.add(pq.poll()) }

        return recognitions
    }

    override fun enableStatLogging(debug: Boolean) {}
}

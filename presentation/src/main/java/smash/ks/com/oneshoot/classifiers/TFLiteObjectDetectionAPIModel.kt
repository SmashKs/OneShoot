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

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.RectF
import android.os.Trace
import com.devrapid.kotlinknifer.logw
import org.tensorflow.lite.Interpreter
import smash.ks.com.data.local.ml.Classifier
import smash.ks.com.data.local.ml.Classifier.Recognition
import smash.ks.com.ext.const.DEFAULT_STR
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Float.compare
import java.lang.Float.parseFloat
import java.lang.Math.exp
import java.lang.Math.min
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.ArrayList
import java.util.Comparator
import java.util.PriorityQueue
import java.util.StringTokenizer
import java.util.Vector

/**
 * Wrapper for frozen detection models trained using the Tensorflow Object Detection API:
 * github.com/tensorflow/models/tree/master/research/object_detection
 */
class TFLiteObjectDetectionAPIModel private constructor() : Classifier {
    companion object {
        private const val TOP_CLASS_SCORE = -1000f

        private const val LIMITATION = 10

        private const val MINIMUM_THRESHOLD = 0.001f
        private const val MAXIMUM_RECTANGLE = 4
        private const val PRE_BUFFER = 3

        private const val MASK_VALUE = 0xFF
        private const val FIRST_UNIT = 8
        private const val SECOND_UNIT = 16

        private const val TOP_POINT_OF_RECT = 0
        private const val LEFT_POINT_OF_RECT = 1
        private const val BOTTOM_POINT_OF_RECT = 2
        private const val RIGHT_POINT_OF_RECT = 3

        // Only return this many results.
        private const val NUM_RESULTS = 1917
        private const val NUM_CLASSES = 91

        private const val Y_SCALE = 10.0f
        private const val X_SCALE = 10.0f
        private const val H_SCALE = 5.0f
        private const val W_SCALE = 5.0f

        /** Memory-map the model file in Assets.  */
        @Throws(IOException::class)
        private fun loadModelFile(assets: AssetManager, modelFilename: String): MappedByteBuffer {
            val fileDescriptor = assets.openFd(modelFilename)
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength

            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }

        /**
         * Initializes a native TensorFlow session for classifying images.
         *
         * @param assetManager  The asset manager to be used to load assets.
         * @param modelFilename The filepath of the model GraphDef protocol buffer.
         * @param labelFilename The filepath of label file for classes.
         */
        @Throws(IOException::class)
        fun create(
            assetManager: AssetManager,
            modelFilename: String,
            labelFilename: String,
            inputSize: Int
        ): Classifier = TFLiteObjectDetectionAPIModel().apply {
            loadCoderOptions(assetManager, "file:///android_asset/box_priors.txt", boxPriors)

            val labelsInput: InputStream?
            val actualFilename = labelFilename
                .split("file:///android_asset/".toRegex())
                .dropLastWhile(String::isEmpty)
                .toTypedArray()[1]

            labelsInput = assetManager.open(actualFilename)
            BufferedReader(InputStreamReader(labelsInput!!)).useLines { it.forEach { labels.add(it) } }
            this.inputSize = inputSize
            try {
                tfLite = Interpreter(loadModelFile(assetManager, modelFilename))
            }
            catch (e: IOException) {
                e.printStackTrace()
            }

            // Pre-allocate buffers.
            img = Array(1) { Array(inputSize) { Array(inputSize) { FloatArray(PRE_BUFFER) } } }
            intValues = IntArray(this.inputSize * this.inputSize)
            outputLocations = Array(1) { Array(NUM_RESULTS) { FloatArray(MAXIMUM_RECTANGLE) } }
            outputClasses = Array(1) { Array(NUM_RESULTS) { FloatArray(NUM_CLASSES) } }
        }
    }

    // Config values.
    private var inputSize = 0
    private val boxPriors = Array(MAXIMUM_RECTANGLE) { FloatArray(NUM_RESULTS) }

    // Pre-allocated buffers.
    private val labels = Vector<String>()
    private var tfLite: Interpreter? = null
    private lateinit var intValues: IntArray
    private lateinit var outputLocations: Array<Array<FloatArray>>
    private lateinit var outputClasses: Array<Array<FloatArray>>
    private lateinit var img: Array<Array<Array<FloatArray>>>

    override val statString = DEFAULT_STR

    private fun expit(x: Float) = (1.0 / (1.0 + exp((-x).toDouble()))).toFloat()

    @Throws(IOException::class)
    private fun loadCoderOptions(assetManager: AssetManager, locationFilename: String, boxPriors: Array<FloatArray>) {
        // Try to be intelligent about opening from assets or sdcard depending on prefix.
        val assetPrefix = "file:///android_asset/"
        val `is` = if (locationFilename.startsWith(assetPrefix)) {
            assetManager.open(locationFilename.split(assetPrefix.toRegex()).toTypedArray()[1])
        }
        else {
            FileInputStream(locationFilename)
        }

        BufferedReader(InputStreamReader(`is`)).useLines {
            it.forEachIndexed { lineNum, line ->
                val st = StringTokenizer(line, ", ")
                var priorIndex = 0

                while (st.hasMoreTokens()) {
                    val token = st.nextToken()
                    try {
                        val number = parseFloat(token)

                        boxPriors[lineNum][priorIndex++] = number
                    }
                    catch (e: NumberFormatException) {
                        // Silently ignore.
                    }

                }
                if (priorIndex != NUM_RESULTS) {
                    throw IllegalStateException("BoxPrior length mismatch: $priorIndex vs $NUM_RESULTS")
                }
            }
        }

        logw("Loaded box priors!")
    }

    private fun decodeCenterSizeBoxes(predictions: Array<Array<FloatArray>>) {
        for (i in 0 until NUM_RESULTS) {
            val yCenter = predictions[0][i][TOP_POINT_OF_RECT] /
                          Y_SCALE *
                          boxPriors[BOTTOM_POINT_OF_RECT][i] +
                          boxPriors[0][i]
            val xCenter = predictions[0][i][LEFT_POINT_OF_RECT] /
                          X_SCALE *
                          boxPriors[RIGHT_POINT_OF_RECT][i] +
                          boxPriors[1][i]
            val h = exp((predictions[0][i][BOTTOM_POINT_OF_RECT] / H_SCALE).toDouble()).toFloat() *
                    boxPriors[BOTTOM_POINT_OF_RECT][i]
            val w = exp((predictions[0][i][RIGHT_POINT_OF_RECT] / W_SCALE).toDouble()).toFloat() *
                    boxPriors[RIGHT_POINT_OF_RECT][i]

            val yMin = yCenter - h / 2f
            val xMin = xCenter - w / 2f
            val yMax = yCenter + h / 2f
            val xMax = xCenter + w / 2f

            predictions[0][i][TOP_POINT_OF_RECT] = yMin
            predictions[0][i][LEFT_POINT_OF_RECT] = xMin
            predictions[0][i][BOTTOM_POINT_OF_RECT] = yMax
            predictions[0][i][RIGHT_POINT_OF_RECT] = xMax
        }
    }

    override fun recognizeImage(bitmap: Bitmap): List<Recognition> {
        // Log this method so that it can be analyzed with systrace.
        Trace.beginSection("recognizeImage")
        Trace.beginSection("preprocessBitmap")
        // Preprocess the image data from 0-255 int to normalized float based
        // on the provided parameters.
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val pixel = intValues[j * inputSize + i]

                img[0][j][i][BOTTOM_POINT_OF_RECT] = (pixel and MASK_VALUE).toFloat() / (MASK_VALUE + 1f) - 1f
                img[0][j][i][LEFT_POINT_OF_RECT] =
                    (pixel shr FIRST_UNIT and MASK_VALUE).toFloat() / (MASK_VALUE + 1f) - 1f
                img[0][j][i][TOP_POINT_OF_RECT] =
                    (pixel shr SECOND_UNIT and MASK_VALUE).toFloat() / (MASK_VALUE + 1f) - 1f
            }
        }
        Trace.endSection()  // preprocessBitmap

        // Copy the input data into TensorFlow.
        Trace.beginSection("feed")
        outputLocations = Array(1) { Array(NUM_RESULTS) { FloatArray(MAXIMUM_RECTANGLE) } }
        outputClasses = Array(1) { Array(NUM_RESULTS) { FloatArray(NUM_CLASSES) } }
        Trace.endSection()  // feed

        // Run the inference call.
        Trace.beginSection("run")
        tfLite?.runForMultipleInputsOutputs(arrayOf<Any>(img),
                                            hashMapOf<Int, Any>(0 to outputLocations,
                                                                1 to outputClasses))  // inputArray, outputMap
        Trace.endSection()  // run

        decodeCenterSizeBoxes(outputLocations)
        // Find the best detections.
        val pq = PriorityQueue(1, Comparator<Recognition> { lhs, rhs ->
            // Intentionally reversed to put high confidence at the head of the queue.
            compare(rhs.confidence!!, lhs.confidence!!)
        })

        // Scale them back to the input size.
        for (i in 0 until NUM_RESULTS) {
            var topClassScore = TOP_CLASS_SCORE
            var topClassScoreIndex = -1

            // Skip the first catch-all class.
            for (j in 1 until NUM_CLASSES) {
                val score = expit(outputClasses[0][i][j])

                if (score > topClassScore) {
                    topClassScoreIndex = j
                    topClassScore = score
                }
            }

            if (topClassScore > MINIMUM_THRESHOLD) {
                val detection = RectF(outputLocations[0][i][LEFT_POINT_OF_RECT] * inputSize,
                                      outputLocations[0][i][TOP_POINT_OF_RECT] * inputSize,
                                      outputLocations[0][i][RIGHT_POINT_OF_RECT] * inputSize,
                                      outputLocations[0][i][BOTTOM_POINT_OF_RECT] * inputSize)

                pq.add(Recognition(i.toString(),
                                   labels[topClassScoreIndex],
                                   outputClasses[0][i][topClassScoreIndex],
                                   detection))
            }
        }

        val recognitions = ArrayList<Recognition>()

        for (i in 0 until min(pq.size, LIMITATION)) {
            val recog = pq.poll()

            recognitions.add(recog)
        }
        Trace.endSection()  // recognizeImage

        return recognitions
    }

    override fun enableStatLogging(logStats: Boolean) {}

    override fun close() {
        tfLite?.close()
        tfLite = null
    }
}

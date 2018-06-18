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

package smash.ks.com.oneshoot.classifiers;

import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.jetbrains.annotations.NotNull;
import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/** A classifier specialized to label images using TensorFlow. */
public class TFLiteImageClassifier implements Classifier {
    private static final int MAX_RESULTS = 3;
    private static final int BATCH_SIZE = 1;
    private static final int PIXEL_SIZE = 3;
    private static final float THRESHOLD = 0.1f;

    private Interpreter interpreter;
    private int inputSize;
    private List<String> labelList;

    private TFLiteImageClassifier() { }

    public static Classifier create(AssetManager assetManager, String modelPath, String labelPath, int inputSize)
            throws IOException {
        TFLiteImageClassifier classifier = new TFLiteImageClassifier();
        classifier.interpreter = new Interpreter(classifier.loadModelFile(assetManager, modelPath));
        classifier.labelList = classifier.loadLabelList(assetManager, labelPath);
        classifier.inputSize = inputSize;

        return classifier;
    }

    @Override
    public List<Recognition> recognizeImage(Bitmap bitmap) {
        ByteBuffer byteBuffer = convertBitmapToByteBuffer(bitmap);
        byte[][] result = new byte[1][labelList.size()];
        interpreter.run(byteBuffer, result);

        return getSortedResult(result);
    }

    @Override
    public void close() {
        interpreter.close();
        interpreter = null;
    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();

        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private List<String> loadLabelList(AssetManager assetManager, String labelPath) throws IOException {
        List<String> labelList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(labelPath)));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();

        return labelList;
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BATCH_SIZE * inputSize * inputSize * PIXEL_SIZE);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[inputSize * inputSize];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        for (int i = 0 ; i < inputSize ; ++i) {
            for (int j = 0 ; j < inputSize ; ++j) {
                final int val = intValues[pixel++];
                byteBuffer.put((byte) ((val >> 16) & 0xFF));
                byteBuffer.put((byte) ((val >> 8) & 0xFF));
                byteBuffer.put((byte) (val & 0xFF));
            }
        }

        return byteBuffer;
    }

    @SuppressLint("DefaultLocale")
    private List<Recognition> getSortedResult(byte[][] labelProbArray) {

        PriorityQueue<Recognition> pq = new PriorityQueue<>(MAX_RESULTS,
                                                            (lhs, rhs) -> Float.compare(rhs.getConfidence(),
                                                                                        lhs.getConfidence()));

        for (int i = 0 ; i < labelList.size() ; ++i) {
            float confidence = (labelProbArray[0][i] & 0xff) / 255.0f;
            if (confidence > THRESHOLD) {
                pq.add(new Recognition("" + i, labelList.size() > i ? labelList.get(i) : "unknown", confidence, null));
            }
        }

        final ArrayList<Recognition> recognitions = new ArrayList<>();
        int recognitionsSize = Math.min(pq.size(), MAX_RESULTS);
        for (int i = 0 ; i < recognitionsSize ; ++i) {
            recognitions.add(pq.poll());
        }

        return recognitions;
    }

    @NotNull
    @Override
    public String getStatString() {
        return null;
    }

    @Override
    public void enableStatLogging(boolean debug) { }
}

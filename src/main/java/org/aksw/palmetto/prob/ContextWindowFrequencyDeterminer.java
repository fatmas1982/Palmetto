/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International Public License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, a file
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aksw.palmetto.prob;

import java.util.Arrays;

import org.aksw.palmetto.corpus.SlidingWindowSupportingAdapter;
import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SubsetDefinition;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

public class ContextWindowFrequencyDeterminer implements SlidingWindowFrequencyDeterminer {

    private SlidingWindowSupportingAdapter corpusAdapter;
    private int windowSize;
    private long wordSetCountSums[];

    public ContextWindowFrequencyDeterminer(SlidingWindowSupportingAdapter corpusAdapter, int windowSize) {
        this.corpusAdapter = corpusAdapter;
        setWindowSize(windowSize);
    }

    @Override
    public CountedSubsets[] determineCounts(String[][] wordsets, SubsetDefinition[] definitions) {
        CountedSubsets countedSubsets[] = new CountedSubsets[definitions.length];
        for (int i = 0; i < definitions.length; ++i) {
            countedSubsets[i] = new CountedSubsets(definitions[i].segments,
                    definitions[i].conditions, determineCounts(wordsets[i]));
        }
        return countedSubsets;
    }

    private int[] determineCounts(String wordset[]) {
        int counts[] = new int[(1 << wordset.length)];
        IntArrayList positions[];
        IntIntOpenHashMap docLengths = new IntIntOpenHashMap();
        IntObjectOpenHashMap<IntArrayList[]> positionsInDocs = corpusAdapter.requestWordPositionsInDocuments(wordset,
                docLengths);
        for (int i = 0; i < positionsInDocs.keys.length; ++i) {
            if (positionsInDocs.allocated[i]) {
                positions = ((IntArrayList[]) ((Object[]) positionsInDocs.values)[i]);
                addCountsFromDocument(positions, counts, docLengths.get(positionsInDocs.keys[i]));
            }
        }
        return counts;
    }

    private void addCountsFromDocument(IntArrayList[] positions, int[] counts, int docLength) {
        int posInList[] = new int[positions.length];
        int nextWordId = 0, nextWordPos = Integer.MAX_VALUE;
        int wordCount = 0;
        // determine the first token which we should look at
        for (int i = 0; i < positions.length; ++i) {
            if (positions[i] != null) {
                Arrays.sort(positions[i].buffer, 0, positions[i].elementsCount);
                if (positions[i].buffer[0] < nextWordPos) {
                    nextWordPos = positions[i].buffer[0];
                    nextWordId = i;
                }
                wordCount += positions[i].elementsCount;
            }
        }
        // create two large arrays for this document
        int wordIds[] = new int[wordCount];
        int wordPositions[] = new int[wordCount];
        wordCount = 0;
        while (nextWordPos < docLength) {
            wordIds[wordCount] = nextWordId;
            wordPositions[wordCount] = nextWordPos;
            ++posInList[nextWordId];
            ++wordCount;
            nextWordPos = Integer.MAX_VALUE;
            for (int i = 0; i < positions.length; ++i) {
                if ((positions[i] != null) && (posInList[i] < positions[i].elementsCount)
                        && (positions[i].buffer[posInList[i]] < nextWordPos)) {
                    nextWordPos = positions[i].buffer[posInList[i]];
                    nextWordId = i;
                }
            }
        }

        int windowStartPos, windowEndPos/* , windowWordSet */; // start inclusive, end exclusive
        int currentWordBit;
        for (int i = 0; i < wordIds.length; ++i) {
            windowStartPos = i;
            windowEndPos = i + 1;
            // search the beginning of the window
            while ((windowStartPos > 0) && (wordPositions[windowStartPos - 1] >= (wordPositions[i] - windowSize))) {
                --windowStartPos;
            }
            // search the end of the window
            while ((windowEndPos < wordPositions.length)
                    && (wordPositions[windowEndPos] <= (wordPositions[i] + windowSize))) {
                ++windowEndPos;
            }
            // windowWordSet = 0;
            currentWordBit = 1 << wordIds[i];
            for (int j = windowStartPos; j < windowEndPos; ++j) {
                // windowWordSet |= wordIds[j];
                if (wordIds[i] < wordIds[j]) {
                    ++counts[currentWordBit | (1 << wordIds[j])];
                }
            }
            ++counts[currentWordBit];
        }
    }

    @Override
    public void setWindowSize(int windowSize) {
        if (windowSize > Long.SIZE) {
            throw new IllegalArgumentException("This class only supports a window size up to " + Long.SIZE + ".");
        }
        this.windowSize = windowSize;
        determineWordSetCountSum();
    }

    @Override
    public long[] getCooccurrenceCounts() {
        return wordSetCountSums;
    }

    @Override
    public String getSlidingWindowModelName() {
        return "P_cw" + windowSize;
    }

    protected void determineWordSetCountSum() {
        // For determining the sum of the counts we rely on a histogram of documents length and the window size
        wordSetCountSums = new long[this.windowSize];

        // Go through the histogram, count the number of windows
        int numberOfWindowsInDocs = 0;
        int histogram[][] = corpusAdapter.getDocumentSizeHistogram();
        for (int i = 0; i < histogram.length; ++i) {
            numberOfWindowsInDocs += histogram[i][1] * (histogram[i][0] - (this.windowSize - 1));
        }

        // Determine how many word sets would have been counted using the number of windows
        for (int i = 0; i < wordSetCountSums.length; ++i) {
            wordSetCountSums[i] = numberOfWindowsInDocs;
        }
    }

    @Override
    public int getWindowSize() {
        return windowSize;
    }

}
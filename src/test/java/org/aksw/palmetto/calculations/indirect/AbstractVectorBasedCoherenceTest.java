/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.aksw.palmetto.calculations.indirect;

import org.aksw.palmetto.aggregation.ArithmeticMean;
import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.data.SubsetVectors;
import org.aksw.palmetto.subsets.Segmentator;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractVectorBasedCoherenceTest {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    private VectorBasedConfirmationMeasure calculation;
    private Segmentator subsetCreator;
    private int wordsetSize;
    private double[] probabilities;
    private double vectors[][];
    private double expectedCoherence;

    public AbstractVectorBasedCoherenceTest(VectorBasedConfirmationMeasure calculation, Segmentator subsetCreator,
            int wordsetSize, double[] probabilities, double[][] vectors, double expectedCoherence) {
        this.calculation = calculation;
        this.probabilities = probabilities;
        this.vectors = vectors;
        this.wordsetSize = wordsetSize;
        this.subsetCreator = subsetCreator;
        this.expectedCoherence = expectedCoherence;
    }

    @Test
    public void test() {
        SegmentationDefinition subsets = subsetCreator.getSubsetDefinition(wordsetSize);
        SubsetVectors subsetVectors = new SubsetVectors(subsets.segments, subsets.conditions, vectors, probabilities);
        Assert.assertEquals(
                expectedCoherence,
                (new ArithmeticMean()).summarize(calculation.calculateConfirmationValues(subsetVectors)),
                DOUBLE_PRECISION_DELTA);
    }
}

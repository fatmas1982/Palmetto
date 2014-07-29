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
package org.aksw.palmetto.vector;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.calculations.direct.LogBasedCalculation;
import org.aksw.palmetto.calculations.direct.LogLikelihoodConfirmationMeasure;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LogLikelihoodCalculationBasedCreatorTest extends AbstractProbCalcBasedVectorCreatorTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        /*
                         * word1 1 1 1
                         * 
                         * word2 0 1 1
                         * 
                         * word3 0 1 1
                         * 
                         * vector1 0 1 1
                         * 
                         * vector2 0 log(1/eps) log(1/eps)
                         * 
                         * vector3 0 log(1/eps) log(1/eps)
                         * 
                         * word1 has P(w_1)=1 which is a problem for likelihood
                         */
                        {
                                3,
                                new double[][] { { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0,
                                        2.0 / 3.0 } },
                                new double[][][] { {
                                        { 0, 0, 0 },
                                        { 0, Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                Math.log(1.0 / LogBasedCalculation.EPSILON) },
                                        { 0, Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                Math.log(1.0 / LogBasedCalculation.EPSILON) } } }, "V_ll(1)", 1 },

                        /*
                         * word1 0 1 1
                         * 
                         * word2 1 0 1
                         * 
                         * word3 1 1 0
                         * 
                         * vector1 log(1/eps) log(0.5) log(0.5)
                         * 
                         * vector2 log(0.5) log(1/eps) log(0.5)
                         * 
                         * vector3 log(0.5) log(0.5) log(1/eps)
                         */{
                                3,
                                new double[][] { { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0,
                                        0 } },
                                new double[][][] { {
                                        { Math.log(1.0 / LogBasedCalculation.EPSILON), Math.log(0.5), Math.log(0.5) },
                                        { Math.log(0.5), Math.log(1.0 / LogBasedCalculation.EPSILON), Math.log(0.5) },
                                        { Math.log(0.5), Math.log(0.5), Math.log(1.0 / LogBasedCalculation.EPSILON) } } },
                                "V_ll(1)", 1 },
                        /*
                         * word1 0 0 0 1
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 log(4) log(2) log(2)
                         * 
                         * vector2 log(2) log(2) log(1)
                         * 
                         * vector3 log(2) log(1) log(2)
                         */
                        {
                                3,
                                new double[][] { { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 } },
                                new double[][][] { {
                                        { Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                Math.log(0.5 / LogBasedCalculation.EPSILON),
                                                Math.log(0.5 / LogBasedCalculation.EPSILON) },
                                        { Math.log(3.0), Math.log(1.0 / LogBasedCalculation.EPSILON), 0 },
                                        { Math.log(3.0), 0, Math.log(1.0 / LogBasedCalculation.EPSILON) } } },
                                "V_ll(1)", 1 },
                        /*
                         * word1 1 0 0 0
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 log(4) log(8 * eps) log(8 * eps)
                         * 
                         * vector2 log(8 * eps) log(2) log(1)
                         * 
                         * vector3 log(8 * eps) log(1) log(2)
                         */
                        {
                                3,
                                new double[][] { { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] { {
                                        { Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                Math.log(LogBasedCalculation.EPSILON / 0.5),
                                                Math.log(LogBasedCalculation.EPSILON / 0.5) },
                                        { Math.log(LogBasedCalculation.EPSILON * 3.0 / 2.0),
                                                Math.log(1.0 / LogBasedCalculation.EPSILON), 0 },
                                        { Math.log(LogBasedCalculation.EPSILON * 3.0 / 2.0), 0,
                                                Math.log(1.0 / LogBasedCalculation.EPSILON) } } }, "V_ll(1)", 1 },
                        // all together
                        {
                                3,
                                new double[][] {
                                        { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                        { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                                        { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                                        { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] {
                                        {
                                                { 0, 0, 0 },
                                                { 0, Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                        Math.log(1.0 / LogBasedCalculation.EPSILON) },
                                                { 0, Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                        Math.log(1.0 / LogBasedCalculation.EPSILON) } },
                                        {
                                                { Math.log(1.0 / LogBasedCalculation.EPSILON), Math.log(0.5),
                                                        Math.log(0.5) },
                                                { Math.log(0.5), Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                        Math.log(0.5) },
                                                { Math.log(0.5), Math.log(0.5),
                                                        Math.log(1.0 / LogBasedCalculation.EPSILON) } },
                                        {
                                                { Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                        Math.log(0.5 / LogBasedCalculation.EPSILON),
                                                        Math.log(0.5 / LogBasedCalculation.EPSILON) },
                                                { Math.log(3.0), Math.log(1.0 / LogBasedCalculation.EPSILON), 0 },
                                                { Math.log(3.0), 0, Math.log(1.0 / LogBasedCalculation.EPSILON) } },
                                        {
                                                { Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                        Math.log(LogBasedCalculation.EPSILON / 0.5),
                                                        Math.log(LogBasedCalculation.EPSILON / 0.5) },
                                                { Math.log(LogBasedCalculation.EPSILON * 3.0 / 2.0),
                                                        Math.log(1.0 / LogBasedCalculation.EPSILON), 0 },
                                                { Math.log(LogBasedCalculation.EPSILON * 3.0 / 2.0), 0,
                                                        Math.log(1.0 / LogBasedCalculation.EPSILON) } } }, "V_ll(1)", 1 },
                        /*
                         * word1 1 1 1
                         * 
                         * word2 0 1 1
                         * 
                         * word3 0 1 1
                         * 
                         * vector1 log(1) log(1) log(1)
                         * 
                         * vector2 log(1) log(3/2) log(3/2)
                         * 
                         * vector3 log(1) log(3/2) log(3/2)
                         */
                        {
                                3,
                                new double[][] { { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0,
                                        2.0 / 3.0 } },
                                new double[][][] { {
                                        { 0, 0, 0 },
                                        { 0, Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) },
                                        { 0, Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) } } },
                                "V_ll(2)", 2 },

                        /*
                         * word1 0 1 1
                         * 
                         * word2 1 0 1
                         * 
                         * word3 1 1 0
                         * 
                         * vector1 log(3/2) log(3/4) log(3/4)
                         * 
                         * vector2 log(3/4) log(3/2) log(3/4)
                         * 
                         * vector3 log(3/4) log(3/4) log(3/2)
                         */{
                                3,
                                new double[][] { { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0,
                                        0 } },
                                new double[][][] { {
                                        { Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                Math.pow(Math.log(0.5), 2), Math.pow(Math.log(0.5), 2) },
                                        { Math.pow(Math.log(0.5), 2),
                                                Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                Math.pow(Math.log(0.5), 2) },
                                        { Math.pow(Math.log(0.5), 2), Math.pow(Math.log(0.5), 2),
                                                Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) } } },
                                "V_ll(2)", 2 },
                        /*
                         * word1 0 0 0 1
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 log(4) log(2) log(2)
                         * 
                         * vector2 log(2) log(2) log(1)
                         * 
                         * vector3 log(2) log(1) log(2)
                         */
                        {
                                3,
                                new double[][] { { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 } },
                                new double[][][] { {
                                        { Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                Math.pow(Math.log(0.5 / LogBasedCalculation.EPSILON), 2),
                                                Math.pow(Math.log(0.5 / LogBasedCalculation.EPSILON), 2) },
                                        { Math.pow(Math.log(3.0), 2),
                                                Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2), 0 },
                                        { Math.pow(Math.log(3.0), 2), 0,
                                                Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) } } },
                                "V_ll(2)", 2 },
                        /*
                         * word1 1 0 0 0
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 log(4) log(8 * eps) log(8 * eps)
                         * 
                         * vector2 log(8 * eps) log(2) log(1)
                         * 
                         * vector3 log(8 * eps) log(1) log(2)
                         */
                        {
                                3,
                                new double[][] { { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] { {
                                        { Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                Math.pow(Math.log(LogBasedCalculation.EPSILON / 0.5), 2),
                                                Math.pow(Math.log(LogBasedCalculation.EPSILON / 0.5), 2) },
                                        { Math.pow(Math.log(LogBasedCalculation.EPSILON * 3.0 / 2.0), 2),
                                                Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2), 0 },
                                        { Math.pow(Math.log(LogBasedCalculation.EPSILON * 3.0 / 2.0), 2), 0,
                                                Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) } } },
                                "V_ll(2)", 2 },
                        // all together
                        {
                                3,
                                new double[][] {
                                        { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                        { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                                        { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                                        { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] {
                                        {
                                                { 0, 0, 0 },
                                                { 0, Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                        Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) },
                                                { 0, Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                        Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) } },
                                        {
                                                { Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                        Math.pow(Math.log(0.5), 2), Math.pow(Math.log(0.5), 2) },
                                                { Math.pow(Math.log(0.5), 2),
                                                        Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                        Math.pow(Math.log(0.5), 2) },
                                                { Math.pow(Math.log(0.5), 2), Math.pow(Math.log(0.5), 2),
                                                        Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) } },
                                        {
                                                { Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                        Math.pow(Math.log(0.5 / LogBasedCalculation.EPSILON), 2),
                                                        Math.pow(Math.log(0.5 / LogBasedCalculation.EPSILON), 2) },
                                                { Math.pow(Math.log(3.0), 2),
                                                        Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2), 0 },
                                                { Math.pow(Math.log(3.0), 2), 0,
                                                        Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) } },
                                        {
                                                { Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                        Math.pow(Math.log(LogBasedCalculation.EPSILON / 0.5), 2),
                                                        Math.pow(Math.log(LogBasedCalculation.EPSILON / 0.5), 2) },
                                                { Math.pow(Math.log(LogBasedCalculation.EPSILON * 3.0 / 2.0), 2),
                                                        Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2), 0 },
                                                { Math.pow(Math.log(LogBasedCalculation.EPSILON * 3.0 / 2.0), 2), 0,
                                                        Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) } } },
                                "V_ll(2)", 2 } });
    }

    public LogLikelihoodCalculationBasedCreatorTest(int wordsetSize, double[][] probabilities,
            double[][][] expectedVectors, String expectedCreatorName, double gamma) {
        super(new LogLikelihoodConfirmationMeasure(), wordsetSize, probabilities, expectedVectors,
                expectedCreatorName, gamma);
    }
}

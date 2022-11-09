/*
 * Copyright (c) 2022, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.graalvm.compiler.hotspot.jdk19.test;

import static org.junit.Assume.assumeTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.graalvm.compiler.api.test.Graal;
import org.graalvm.compiler.jtt.JTTTest;
import org.graalvm.compiler.runtime.RuntimeProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import jdk.vm.ci.amd64.AMD64;
import jdk.vm.ci.code.Architecture;

@RunWith(Parameterized.class)
public class ShuffleBitsTest extends JTTTest {

    @Parameterized.Parameters(name = "{0}, {1}")
    public static Collection<Object[]> testData() {
        long[] inputs = {0, 1, -1, Integer.MIN_VALUE, Integer.MAX_VALUE, Long.MIN_VALUE, Long.MAX_VALUE, 0xCAFEBABE, 0xFF00FFF0, 0x0000CABAB, 0xCAFEBABECAFEBABEL, 0xFF00FFF0FF00FFF0L, 0x0000CABAB0000CABABL};

        List<Object[]> testParameters = new ArrayList<>();
        for (long a : inputs) {
            for (long b : inputs) {
                testParameters.add(new Object[]{a, b});
            }
        }
        return testParameters;
    }

    @Parameterized.Parameter(value = 0) public long input0;
    @Parameterized.Parameter(value = 1) public long input1;

    @Before
    public void checkPreview() {
        Architecture arch = Graal.getRequiredCapability(RuntimeProvider.class).getHostBackend().getTarget().arch;
        assumeTrue("skipping AMD64 specific test", arch instanceof AMD64);
        assumeTrue("bmi2 not supported", ((AMD64) arch).getFeatures().contains(AMD64.CPUFeature.BMI2));
    }

    public static int iCompress(int i, int mask) {
        return Integer.compress(i, mask);
    }

    public static int iExpand(int i, int mask) {
        return Integer.expand(i, mask);
    }

    public static long lCompress(long i, long mask) {
        return Long.compress(i, mask);
    }

    public static long lExpand(long i, long mask) {
        return Long.expand(i, mask);
    }

    @Test
    public void testICompress() {
        runTest("iCompress", (int) input0, (int) input1);
    }

    @Test
    public void testIExpand() {
        runTest("iExpand", (int) input0, (int) input1);
    }

    @Test
    public void testLCompress() {
        runTest("lCompress", input0, input1);
    }

    @Test
    public void testLExpand() {
        runTest("lExpand", input0, input1);
    }
}

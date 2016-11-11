/*
 * Copyright (c) 2011, 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
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
package com.oracle.graal.nodes.memory;

import com.oracle.graal.compiler.common.LocationIdentity;
import com.oracle.graal.graph.Node;
import com.oracle.graal.nodes.FixedNode;
import com.oracle.graal.nodes.FixedNodeInterface;

/**
 * This interface marks subclasses of {@link FixedNode} that kill a set of memory locations
 * represented by location identities (i.e. change a value at one or more locations that belong to
 * these location identities).
 */
public interface MemoryCheckpoint extends MemoryNode, FixedNodeInterface {

    interface Single extends MemoryCheckpoint {

        /**
         * This method is used to determine which memory location is killed by this node. Returning
         * the special value {@link LocationIdentity#any()} will kill all memory locations.
         *
         * @return the identity of the location killed by this node.
         */
        LocationIdentity getLocationIdentity();

    }

    interface Multi extends MemoryCheckpoint {

        /**
         * This method is used to determine which set of memory locations is killed by this node.
         * Returning the special value {@link LocationIdentity#any()} will kill all memory
         * locations.
         *
         * @return the identities of all locations killed by this node.
         */
        LocationIdentity[] getLocationIdentities();

    }

    class TypeAssertion {

        public static boolean correctType(Node node) {
            return !(node instanceof MemoryCheckpoint) || (node instanceof MemoryCheckpoint.Single ^ node instanceof MemoryCheckpoint.Multi);
        }
    }
}

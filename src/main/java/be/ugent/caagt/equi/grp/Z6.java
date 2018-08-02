/* Z5.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright Ⓒ 2015 Universiteit Gent
 * 
 * This file is part of the Equi application
 * 
 * Corresponding author (see also file AUTHORS)
 * 
 * Kris Coolsaet
 * Department of Applied Mathematics, Computer Science and Statistics
 * Ghent University 
 * Krijgslaan 281-S9
 * B-9000 GENT Belgium
 * 
 * The Equi application is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * The Equi Application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with the Equi Application (file LICENSE in the distribution).  If not,
 * see http://www.gnu.org/licenses/.
 */

package be.ugent.caagt.equi.grp;

import be.ugent.caagt.perm.Perm;

import java.util.Arrays;
import java.util.Collections;

/**
 * Cyclic group of order 6
 */
public class Z6 extends AbstractCombinatorialGroup {

    private Perm g3minus;

    public Z6(int degree, Perm g3minus) {
        super(6, degree);
        this.g3minus = g3minus;
    }

    @Override
    public String toString() {
        return "Z(6)";
    }

    @Override
    public Iterable<CombinatorialGroup> getSubgroups() {
        return Arrays.asList(
                this
        );
    }

    @Override
    public Iterable<CombinedGroup> getPointGroups() {
        return Arrays.asList(
                new CombinedGroup("S6", order, degree, Arrays.asList(
                        new ExtendedPerm(g3minus, PointGroupElement.ROT_3.minus())
                )),
                new CombinedGroup("[C6]", order, degree,
                        Collections.singleton(ExtendedPerm.rotation(g3minus, 1))
                ),
                new CombinedGroup("[C3h]", order, degree,
                        Collections.singleton(ExtendedPerm.rotoreflection(g3minus, 2))
                )
        );
    }
}

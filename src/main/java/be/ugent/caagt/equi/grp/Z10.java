/* Z10.java
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
 * Cyclic group of order 10 with point group realizations that use a special rotation axis of order 5
 */
public class Z10 extends AbstractCombinatorialGroup {

    private Perm g5i;

    public Z10(int degree, Perm g5i) {
        super(10, degree);
        this.g5i = g5i;
    }

    @Override
    public String toString() {
        return "Z(10)";
    }

    @Override
    public Iterable<CombinatorialGroup> getSubgroups() {
        return Arrays.asList(
                this,
                new Z5(degree, g5i.mul(g5i))
        );
    }

    @Override
    public Iterable<CombinedGroup> getPointGroups() {
        return Arrays.asList(
                new CombinedGroup("S10", order, degree, Arrays.asList(
                        new ExtendedPerm(g5i, PointGroupElement.ROT_5.minus())
                )),
                new CombinedGroup("S10/3", order, degree, Arrays.asList(
                        new ExtendedPerm(g5i, PointGroupElement.ROT_5_STAR.minus())
                )),
                new CombinedGroup("[C10]", order, degree,
                        Collections.singleton(ExtendedPerm.rotation(g5i, 1))
                ),
                new CombinedGroup("[C10/3]", order, degree,
                        Collections.singleton(ExtendedPerm.rotation(g5i, 3))
                ),
                new CombinedGroup("[C5h]", order, degree,
                        Collections.singleton(ExtendedPerm.rotoreflection(g5i, 2))
                ),
                new CombinedGroup("[C5/2h]", order, degree,
                        Collections.singleton(ExtendedPerm.rotoreflection(g5i, 4))
                )
        );
    }
}

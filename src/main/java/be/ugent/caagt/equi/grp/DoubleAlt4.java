/* DoubleAlt4.java
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
 * Doubled alternating group of order 4.
 */
public class DoubleAlt4 extends AbstractCombinatorialGroup {

    private Perm g3i;

    private Perm g2;

    /**
     * Alternating group of order 4 generated by 'canonical' elements -g3,g2.
     */
    public DoubleAlt4(int degree, Perm g3i, Perm g2) {
        super(24, degree);
        this.g3i = g3i;
        this.g2 = g2;
    }

    @Override
    public String toString() {
        return "2.Alt(4)";
    }

    @Override
    public Iterable<CombinatorialGroup> getSubgroups() {
        return Collections.emptyList(); // TODO
    }

    @Override
    public Iterable<CombinedGroup> getPointGroups() {
        return Collections.singleton(
                new CombinedGroup("Th", order, degree, Arrays.asList(
                        new ExtendedPerm(g3i, PointGroupElement.ROT_3.minus()),
                        new ExtendedPerm(g2, PointGroupElement.ROT_G2 )
                ))
        );
    }

}

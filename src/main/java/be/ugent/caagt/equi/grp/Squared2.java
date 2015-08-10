/* Cubed2.java
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Klein 4-group in the most general setting
 */
public class Squared2 extends AbstractCombinatorialGroup {

    private Perm g;

    private Perm h;

    public Squared2(int degree, Perm g, Perm h) {
        super(4, degree);
        this.g = g;
        this.h = h;
    }


    @Override
    public String toString() {
        return "2Z(2)";
    }

    @Override
    public Iterable<CombinatorialGroup> getSubgroups() {
        return Arrays.asList(
                this,
                new Cyclic(degree, g),
                new Cyclic(degree, h),
                new Cyclic(degree, g.mul(h))
        );
    }

    @Override
    public Iterable<CombinedGroup> getPointGroups() {
        Collection<CombinedGroup> list = new ArrayList<>();
        list.add (new CombinedGroup("D2", order, degree,
                Arrays.asList(new ExtendedPerm(g, PointGroupElement.ROT_G2),
                        new ExtendedPerm(h, PointGroupElement.ROT_G2STAR)))
        );
        // TODO add 3x C2v
        // TODO add 6x C2h

        return list;
    }
}

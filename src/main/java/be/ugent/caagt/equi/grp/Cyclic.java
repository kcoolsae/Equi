/* Cyclic.java
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
 * The Degage Web Application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with the Degage Web Application (file LICENSE in the
 * distribution).  If not, see http://www.gnu.org/licenses/.
 */

package be.ugent.caagt.equi.grp;

import be.ugent.caagt.perm.Perm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Cyclic permutation group
 */
public class Cyclic extends AbstractCombinatorialGroup {

    private Perm gen;

    public Cyclic(int degree, Perm gen) {
        super(gen.order(), degree);
        this.gen = gen;
    }

    @Override
    public String toString() {
        return "Z(" + order + ")";
    }

    @Override
    public Iterable<CombinedGroup> getPointGroups() {
        Collection<CombinedGroup> list = new ArrayList<>();
        for (int d : getDivisors(order)) {
            list.add(new CombinedGroup("C" + num(order, d), order, degree,
                    Collections.singleton(ExtendedPerm.rotation(gen, d))
            ));
        }
        if (order % 2 == 0) {
            for (int d : getDivisors(order)) {
                list.add(new CombinedGroup("S" + num(order, d), order, degree,
                        Collections.singleton(ExtendedPerm.rotoreflection(gen, d))
                ));
            }
        }
        if (order % 4 == 2) {
            for (int d : getDivisors(order / 2)) {
                list.add(new CombinedGroup("C" + order / 2 + "h", order, degree,
                        Collections.singleton(ExtendedPerm.rotoreflection(gen, 2 * d))
                ));
            }
        }
        return list;
    }
}

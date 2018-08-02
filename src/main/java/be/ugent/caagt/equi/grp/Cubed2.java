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
import java.util.List;

/**
 * Elementary Abelian group of order 8
 */
public class Cubed2 extends AbstractCombinatorialGroup {

    private Perm a;

    private Perm b;

    private Perm c;

    public Cubed2(int degree, Perm a, Perm b, Perm c) {
        super(8, degree);
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public String toString() {
        return "2Dih(2)";
    }

    @Override
    public Iterable<CombinatorialGroup> getSubgroups() {
        return Arrays.asList(
                this // TODO
        );
    }

    private void addD2h(Collection<CombinedGroup> list, Perm a, Perm b, Perm c) {
        list.add(new CombinedGroup("D2h", order, degree, Arrays.asList(
                new ExtendedPerm(a, PointGroupElement.ROT_G2),
                new ExtendedPerm(b, PointGroupElement.ROT_G2STAR),
                new ExtendedPerm(c, PointGroupElement.MINUS_ONE)
        )));
    }

    @Override
    public Iterable<CombinedGroup> getPointGroups() {
        Collection<CombinedGroup> list = new ArrayList<>();
        Perm ab = a.mul(b);
        Perm ca = c.mul(a);
        Perm bc = b.mul(c);
        Perm abc = ab.mul(c);

        addD2h(list, a, b, c); // 22i
        addD2h(list, b, c, a);
        addD2h(list, c, a, b);

        addD2h(list, a, b, ca); // 22s
        addD2h(list, b, c, ab);
        addD2h(list, c, a, bc);

        addD2h(list, ca, bc, c); // ssi
        addD2h(list, ab, ca, a);
        addD2h(list, bc, ab, b);

        addD2h(list, c,abc, ca); // ss2
        addD2h(list, a,abc, ab);
        addD2h(list, b,abc, bc);

        addD2h(list, a, bc, c); // 2si
        addD2h(list, b, ca, a);
        addD2h(list, c, ab, b);
        addD2h(list, c, bc, a);
        addD2h(list, a, ca, b);
        addD2h(list, b, ab, c);

        addD2h(list, ca, bc, abc); // sss
        return list;

    }
}

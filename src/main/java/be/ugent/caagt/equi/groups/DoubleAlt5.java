/* DoubleAlt5.java
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

package be.ugent.caagt.equi.groups;

import be.ugent.caagt.equi.groups.Generator;
import be.ugent.caagt.equi.groups.PointGroup3D;
import be.ugent.caagt.perm.Perm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 */
public class DoubleAlt5 extends AbstractCombinatorialGroup {

    public DoubleAlt5() {
        super(120);
    }


    @Override
    public String toString() {
        return "2.Alt(5)";
    }

    @Override
    public Realization findRealization(Perm[] perms, int degree) {
        Realization rel = new Realization(this, degree);

        Perm g5i = RealizationUtils.elementOfOrder(10, perms);
        Perm i = g5i.pow(5);
        Perm g5 = g5i.mul(i);
        Perm g3 = RealizationUtils.elementOfOrder(3, g5, 2, perms);
        Perm g3inv = g3.inv();
        Perm g2 = g3inv.mul(g5.pow(3)).mul(g3).mul(g5.pow(2)).mul(g3inv);

        rel.setGenerator(Generator.G5I, g5i);
        rel.setGenerator(Generator.G5, g5);
        rel.setGenerator(Generator.G3I, g3.mul(i));
        rel.setGenerator(Generator.G3, g3);
        rel.setGenerator(Generator.G2, g2);
        rel.setGenerator(Generator.G2I, g2.mul(i));
        rel.setGenerator(Generator.I, i);
        rel.setGenerator(Generator.G2star, g2.conj(g3));
        rel.setGenerator(Generator.G2prime, g2.conj(g5.inv()));

        return rel;
    }

    @Override
    public Iterable<PointGroup3D> getPointGroups() {
        return Arrays.asList(PointGroup3D.Ih, PointGroup3D.Ihstar);
    }


    @Override
    public Iterable<Realization> getSubgroupRealizations(Realization base) {
        Collection<Realization> result = new ArrayList<>();
        result.add(base);

        result.add(new Alt5().generateRealization(base.getDegree(),
                base.getGenerator(Generator.G5), base.getGenerator(Generator.G3), base.getGenerator(Generator.G2)
        ));
        result.add(new DoubleAlt4().generateRealization(base.getDegree(),
                base.getGenerator(Generator.G3I), base.getGenerator(Generator.G2)
        ));
        result.add(new Alt4().generateRealization(base.getDegree(),
                base.getGenerator(Generator.G3), base.getGenerator(Generator.G2)
        ));
        result.add(new DoubleDih5().generateRealization(base.getDegree(),
              base.getGenerator(Generator.G5I), base.getGenerator(Generator.G2)
        ));
        result.add(new DoubleSym3().generateRealization(base.getDegree(),
                base.getGenerator(Generator.G3I), base.getGenerator(Generator.G2prime)
        ));
        result.add(new Cyclic(10).generateRealization(base.getDegree(),
                base.getGenerator(Generator.G5I)
        ));
        result.add(new Cubed2().generateRealization(base.getDegree(),
                base.getGenerator(Generator.G2),
                base.getGenerator(Generator.G2star),
                base.getGenerator(Generator.I)
        ));
        result.add(new Cyclic(6).generateRealization(base.getDegree(),
                base.getGenerator(Generator.G3I)
        ));
        result.add(new Cyclic(5).generateRealization(base.getDegree(),
                base.getGenerator(Generator.G5)
        ));

        return result;
    }
}

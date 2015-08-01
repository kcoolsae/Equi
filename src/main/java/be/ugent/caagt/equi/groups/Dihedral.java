/* Dihedral.java
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
import java.util.Collection;

/**
 * Isomorphism class of dihedral groups
 */
public class Dihedral extends AbstractCombinatorialGroup {

    private int par;

    public Dihedral(int par) {
        super (2*par);
        this.par = par;
    }

    @Override
    public String toString() {
        return "Dih(" + par + ")";
    }

    /**
     * Return the dihedral group with the given order and signature, or null
     * if this is not the signature of a dihedral group of that order
     */
    public static Dihedral fromSignature (int order, int[] signature) {
        if (signature[signature.length-2] == order/2 && signature[3] == order/2+1) {
            return new Dihedral(order/2);
        } else {
            return null;
        }
    }

    public Realization generateRealization(int degree, Perm g, Perm g2) {
        Realization rel = new Realization(this, degree);
        rel.setGenerator(Generator.C, g);
        rel.setGenerator(Generator.D, g2);
        return rel;
    }

    @Override
    public Iterable<PointGroup3D> getPointGroups() {
        Collection<PointGroup3D> list = new ArrayList<>();
        list.add (PointGroup3D.D(par));
        // TODO: more cases?
        if (par == 5) {
           list.add (PointGroup3D.D(par, 2));
        }
        list.add (PointGroup3D.Cv(par));
        if (par == 5) {
           list.add (PointGroup3D.Cv(par, 2));
        }
        if (par % 2 == 0) {
            list.add (PointGroup3D.Dd(par));
            if (par == 10 ) {
                list.add(PointGroup3D.Dd(par,3));
            }
        }
        return list;
    }
}

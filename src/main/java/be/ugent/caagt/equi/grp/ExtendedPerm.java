/* ExtendedPerm.java
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

/**
 * Permutation extended with the corresponding point group element
 */
public class ExtendedPerm {

    public Perm perm;

    public Perm invPerm;

    public PointGroupElement mat;

    private ExtendedPerm(Perm perm, Perm invPerm, PointGroupElement mat) {
        this.perm = perm;
        this.invPerm = invPerm;
        this.mat = mat;
    }

    public ExtendedPerm(Perm perm, PointGroupElement mat) {
        this (perm, perm.inv(), mat);
    }

    public ExtendedPerm() {
        this.perm = Perm.ONE;
        this.invPerm = Perm.ONE;
        this.mat = PointGroupElement.ONE;
    }

    public ExtendedPerm mul (ExtendedPerm other) {
        return new ExtendedPerm(perm.mul(other.perm), other.invPerm.mul(invPerm), mat.mul(other.mat));
    }

    public boolean sameAs (ExtendedPerm other) {
        return perm.equals(other.perm);
    }

    public static ExtendedPerm rotation (Perm gen, int divisor) {
        return new ExtendedPerm(gen, PointGroupElement.rotation((double)gen.order() / divisor));
    }
    public static ExtendedPerm rotoreflection (Perm gen, int divisor) {
        return new ExtendedPerm(gen, PointGroupElement.rotoreflection((double)gen.order() / divisor));
    }
}

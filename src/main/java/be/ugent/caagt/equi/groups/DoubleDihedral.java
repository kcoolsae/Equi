/* DoubleDihedral.java
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

/**
 * Isomorphism class of even cyclic groups multiplied by 2
 */
public class DoubleDihedral extends AbstractCombinatorialGroup {

    private int par;

    public DoubleDihedral(int par) {
        super(4*par);
        this.par = par;
    }

    @Override
    public String toString() {
        return "2.Dih(" + par + ")";
    }

    /**
     * Return the double dihedral group with the given order and signature, or null
     * if this is not the signature of a double dihedral group of that order
     */
    public static DoubleDihedral fromSignature (int order, int[] signature) {
        if (order % 4 != 0) {
            return null; // dihedral should not be recognized as double dihedral
        }
        if (signature[signature.length-2] == order/4 && signature[3] == 3+order/2) {
            return new DoubleDihedral(order/4);
        } else {
            return null;
        }
    }
}

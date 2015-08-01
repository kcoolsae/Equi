/* RealizationUtils.java
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

import be.ugent.caagt.perm.Perm;

/**
 * Utilities for finding realizations.
 */
class RealizationUtils {

    /** Find an element of given order among the given permutations. */
    public static Perm elementOfOrder (int order, Perm[] perms) {
        for (Perm perm : perms) {
            if (perm.order() == order) {
                return perm;
            }
        }
        return null;
    }

    /**
     * Find an element p of order n such that q.p has order m
     */
    public static Perm elementOfOrder (int n, Perm q, int m, Perm[] perms) {
        for (Perm p : perms) {
            if (p.order() == n && q.mul(p).order() == m) {
                return p;
            }
        }
        return null;
    }
}

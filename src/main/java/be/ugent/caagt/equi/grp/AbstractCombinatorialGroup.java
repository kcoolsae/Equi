/* AbstractCombinatorialGroup.java
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

/**
 * Common super class of combinatorial group
 */
public abstract class AbstractCombinatorialGroup implements CombinatorialGroup {

    protected int order;

    protected int degree;

    public AbstractCombinatorialGroup(int order, int degree) {
        this.order = order;
        this.degree = degree;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public int getDegree() {
        return 0;
    }

    private static final int[][] DIVISORS = {
                {1}, {1}, {1}, {1}, {1}, // 0- 4
                {1, 2}, {1}, {1, 2}, {1, 3}, {1, 2, 4}, // 5-9
                {1, 3}, {1, 2, 3, 4, 5}, {1, 5}, // 10-12
            };

    protected static int[] getDivisors(int order) {
        if (order >= DIVISORS.length) {
            return DIVISORS[0];
        } else {
            return DIVISORS[order];
        }
    }

    protected static String num(int order, int divisor) {
        if (divisor == 1) {
            return Integer.toString(order);
        } else {
            return order + "/" + divisor;
        }
    }
}

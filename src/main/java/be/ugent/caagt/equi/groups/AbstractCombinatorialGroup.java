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

import be.ugent.caagt.equi.groups.PointGroup3D;
import be.ugent.caagt.perm.Perm;

import java.util.Collections;

/**
 * Common super class of all combinatorial groups
 */
public abstract class AbstractCombinatorialGroup implements CombinatorialGroup {

    protected int order;

    public int order() {
        return order;
    }

    protected AbstractCombinatorialGroup (int order) {
        this.order = order;
    }

    @Override
    public Realization findRealization(Perm[] perms, int degree) {
        return null;
    }

    @Override
    public Iterable<PointGroup3D> getPointGroups() {
        return Collections.emptyList();
    }

    @Override
    public Iterable<Realization> getSubgroupRealizations(Realization base) {
        return Collections.singletonList(base);
    }
}

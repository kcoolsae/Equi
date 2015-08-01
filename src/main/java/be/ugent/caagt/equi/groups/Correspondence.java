/* Correspondence.java
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
import be.ugent.caagt.equi.groups.ExtendedGroup;
import be.ugent.caagt.equi.groups.ExtendedPerm;
import be.ugent.caagt.perm.Perm;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Encodes a correspondence between an abstract group and a point group.
 */
public class Correspondence {

    private Map<Generator, double[][]> map = new EnumMap<>(Generator.class);

    public void setGenerator(Generator g, double[][] p) {
        map.put(g, p);
    }

    public double[][] getGenerator(Generator g) {
        return map.get(g);
    }

    private static final double PHI = 0.5 * (1.0 + Math.sqrt(5.0));

    /**
     * The standard correspondence used for icosahedral or octahedral symmetries
     */
    public static final Correspondence STANDARD;

    /**
     * A second correspondence for icosahedral symmetries, algebraically conjugate to the
     * standard conjugate
     */
    public static final Correspondence CONJUGATE;

    /**
     * A correspondence for the groups related to cyclic group of order 5
     */

    static {
        STANDARD = new Correspondence();
        STANDARD.setGenerator(Generator.G5, new double[][]{
                {0.5, 0.5 / PHI, -0.5 * PHI},
                {-0.5 / PHI, -0.5 * PHI, -0.5},
                {-0.5 * PHI, 0.5, -0.5 / PHI}
        });
        STANDARD.setGenerator(Generator.G5I, new double[][]{
                {-0.5, -0.5 / PHI, 0.5 * PHI},
                {0.5 / PHI, 0.5 * PHI, 0.5},
                {0.5 * PHI, -0.5, 0.5 / PHI}
        });
        STANDARD.setGenerator(Generator.G2prime, new double[][]{
                {-0.5 * PHI, -0.5, 0.5 / PHI},
                {-0.5, 0.5 / PHI, -0.5 * PHI},
                {0.5 / PHI, -0.5 * PHI, -0.5}
        });
        STANDARD.setGenerator(Generator.G4, new double[][]{{0, 0, 1}, {0, 1, 0}, {-1, 0, 0}});
        STANDARD.setGenerator(Generator.G4I, new double[][]{{0, 0, -1}, {0, -1, 0}, {1, 0, 0}});
        STANDARD.setGenerator(Generator.G3, new double[][]{{0, 0, 1}, {1, 0, 0}, {0, 1, 0}});
        STANDARD.setGenerator(Generator.G3I, new double[][]{{0, 0, -1}, {-1, 0, 0}, {0, -1, 0}});
        STANDARD.setGenerator(Generator.G2, new double[][]{{-1, 0, 0}, {0, 1, 0}, {0, 0, -1}});
        STANDARD.setGenerator(Generator.G2I, new double[][]{{1, 0, 0}, {0, -1, 0}, {0, 0, 1}});
        STANDARD.setGenerator(Generator.G2star, new double[][]{{1, 0, 0}, {0, -1, 0}, {0, 0, -1}});

        STANDARD.setGenerator(Generator.R, new double[][]{{0, 0, 1}, {0, 1, 0}, {1, 0, 0}});
        STANDARD.setGenerator(Generator.RI, new double[][]{{0, 0, -1}, {0, -1, 0}, {-1, 0, 0}});
        STANDARD.setGenerator(Generator.I, new double[][]{{-1, 0, 0}, {0, -1, 0}, {0, 0, -1}});


        CONJUGATE = new Correspondence();
        CONJUGATE.map = new EnumMap<>(STANDARD.map);
        CONJUGATE.setGenerator(Generator.G5, new double[][]{
                {0.5, -0.5 * PHI, 0.5 / PHI},
                {0.5 * PHI, 0.5 / PHI, -0.5},
                {0.5 / PHI, 0.5, 0.5 * PHI}
        });
        CONJUGATE.setGenerator(Generator.G5I, new double[][]{
                {-0.5, 0.5 * PHI, -0.5 / PHI},
                {-0.5 * PHI, -0.5 / PHI, 0.5},
                {-0.5 / PHI, -0.5, -0.5 * PHI}
        });
        CONJUGATE.setGenerator(Generator.G2prime, new double[][]{
                {0.5 / PHI, -0.5, -0.5 * PHI},
                {-0.5, -0.5 * PHI, 0.5 / PHI},
                {-0.5 * PHI, 0.5 / PHI, -0.5}
        });
    }

    /**
     * Correspondence used for (general) cyclic groups. The angle
     * given should be a (prime) multiple of 2*pi/order. Installs generator
     * C (cyclic)
     */
    public static Correspondence C(double angle) {
        Correspondence c = new Correspondence();
        c.setGenerator(Generator.C, new double[][]{
                {Math.cos(angle), 0, Math.sin(angle)},
                {0, 1, 0},
                {-Math.sin(angle), 0, Math.cos(angle)},
        });
        return c;
    }

    public static Correspondence S(double angle) {
        Correspondence c = new Correspondence();
        c.setGenerator(Generator.C, new double[][]{
                {-Math.cos(angle), 0, -Math.sin(angle)},
                {0, -1, 0},
                {Math.sin(angle), 0, -Math.cos(angle)},
        });
        return c;
    }

    public static Correspondence Chodd(double angle) {
        Correspondence c = new Correspondence();
        c.setGenerator(Generator.C, new double[][]{
                {Math.cos(angle), 0, Math.sin(angle)},
                {0, -1, 0},
                {-Math.sin(angle), 0, Math.cos(angle)},
        });
        return c;
    }

    public static Correspondence D(double angle) {
        Correspondence c = new Correspondence();
        c.setGenerator(Generator.C, new double[][]{
                {Math.cos(angle), 0, Math.sin(angle)},
                {0, 1, 0},
                {-Math.sin(angle), 0, Math.cos(angle)},
        });
        c.setGenerator(Generator.D, new double[][]{{0,0,1},{0,-1,0},{1,0,0}});
        return c;
    }

    public static Correspondence Cv(double angle) {
        Correspondence c = new Correspondence();
        c.setGenerator(Generator.C, new double[][]{
                {Math.cos(angle), 0, Math.sin(angle)},
                {0, 1, 0},
                {-Math.sin(angle), 0, Math.cos(angle)},
        });
        c.setGenerator(Generator.D, new double[][]{{0, 0, 1}, {0, 1, 0}, {1, 0, 0}});
        return c;
    }

    public static Correspondence Dd(double angle) {
        Correspondence c = new Correspondence();
        c.setGenerator(Generator.C, new double[][]{
                {-Math.cos(angle/2), 0, -Math.sin(angle/2)},
                {0, -1, 0},
                {Math.sin(angle/2), 0, -Math.cos(angle/2)},
        });
        c.setGenerator(Generator.D, new double[][]{{0, 0, 1}, {0, 1, 0}, {1, 0, 0}});
        return c;
    }

    /**
     * Combine this correspondence with a realization to obtain an extended
     * permutation group.
     */
    public ExtendedGroup createExtendedGroup(Realization rel) {
        List<ExtendedPerm> generators = new ArrayList<>();
        for (Map.Entry<Generator, double[][]> entry : map.entrySet()) {
            Perm perm = rel.getGenerator(entry.getKey());
            if (perm != null) {
                generators.add(new ExtendedPerm(perm, perm.inv(), entry.getValue()));
            }
        }
        int order = rel.getGroup().order();
        return new ExtendedGroup(order, rel.getDegree(), generators);
    }

}

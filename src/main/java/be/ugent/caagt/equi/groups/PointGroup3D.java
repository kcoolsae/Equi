/* PointGroup3D.java
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
 * Represents an (abstract) point group in 3 dimensions.
 */
public class PointGroup3D {

    private String caption;

    private Correspondence correspondence;

    public String getCaption() {
        return caption;
    }

    public Correspondence getCorrespondence() {
        return correspondence;
    }

    private PointGroup3D(String caption, Correspondence correspondence) {
        this.caption = caption;
        this.correspondence = correspondence;
    }

    public static PointGroup3D Ih = new PointGroup3D("Ih", Correspondence.STANDARD);
    public static PointGroup3D Ihstar = new PointGroup3D("Ih*", Correspondence.CONJUGATE);
    public static PointGroup3D I = new PointGroup3D("I", Correspondence.STANDARD);
    public static PointGroup3D Istar = new PointGroup3D("I*", Correspondence.CONJUGATE);
    public static PointGroup3D Th = new PointGroup3D("Th", Correspondence.STANDARD);
    public static PointGroup3D Td = new PointGroup3D("Td", Correspondence.STANDARD);
    public static PointGroup3D T = new PointGroup3D("T", Correspondence.STANDARD);
    public static PointGroup3D D2h = new PointGroup3D("D2h", Correspondence.STANDARD);
    public static PointGroup3D D3d = new PointGroup3D("D3d", Correspondence.STANDARD);
    public static PointGroup3D D5d = new PointGroup3D("D5d", Correspondence.STANDARD);
    public static PointGroup3D D5dstar = new PointGroup3D("D5/2d", Correspondence.CONJUGATE);

    public static PointGroup3D C(int order) {
        return new PointGroup3D(String.format("C%d", order), Correspondence.C(Math.PI*2.0/order));
    }

    public static PointGroup3D D(int par) {
        return new PointGroup3D(String.format("D%d", par), Correspondence.D(Math.PI*2.0/par));
    }

    public static PointGroup3D D(int par, int factor) {
        return new PointGroup3D(String.format("D%d/%d", par, factor), Correspondence.D(Math.PI*2.0/par*factor));
    }

    public static PointGroup3D Cv(int par) {
        return new PointGroup3D(String.format("C%dv", par), Correspondence.Cv(Math.PI*2.0/par));
    }

    public static PointGroup3D Cv(int par, int factor) {
        return new PointGroup3D(String.format("C%d/%dv", par, factor), Correspondence.Cv(Math.PI*2.0/par*factor));
    }

    public static PointGroup3D Dd(int par) {
        return new PointGroup3D(String.format("D%dd", par), Correspondence.Dd(Math.PI/par));
    }

    public static PointGroup3D Dd(int par, int factor) {
        return new PointGroup3D(String.format("D%d/%dd", par, factor), Correspondence.Dd(Math.PI/par*factor));
    }

    public static PointGroup3D Ch(int order) {
        return new PointGroup3D(String.format("C%dh", order),
                order % 2 == 0 ? Correspondence.C(Math.PI*2.0/order)
                        : Correspondence.Chodd(Math.PI*2.0/order)
        );
    }

    public static PointGroup3D Ch(int order, int factor) {
        return new PointGroup3D(String.format("C%d/%dh", order, factor),
                order % 2 == 0 ? Correspondence.C(Math.PI*2.0/order*factor)
                        : Correspondence.Chodd(Math.PI*2.0/order*factor)
        );
    }

    public static PointGroup3D C(int order, int factor) {
        return new PointGroup3D(String.format("C%d/%d", order, factor), Correspondence.C(Math.PI*2.0/order*factor));
    }

    public static PointGroup3D S(int order) {
        return new PointGroup3D(String.format("S%d", order), Correspondence.S (Math.PI*2.0/(order/2)));
    }

    public static PointGroup3D S(int order, int factor) {
        return new PointGroup3D(String.format("S%d/%d", order, factor), Correspondence.S (Math.PI*2.0/(order/2)*factor));
    }
}

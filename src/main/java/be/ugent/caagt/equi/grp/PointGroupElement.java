/* PointGroupElement.java
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
 * Represents a single point group element. Also provides some constant elements which are used a lot.
 */
public class PointGroupElement {

    private double[][] mat;

    public PointGroupElement(double[][] mat) {
        this.mat = mat;
    }

    public double[][] getMatrix() {
        return mat;
    }

    public double[] image(double[] orig) {
        double[] result = new double[3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[j] += orig[i] * mat[i][j];
            }
        }
        return result;
    }

    public PointGroupElement minus() {
        double[][] result = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = -mat[i][j];
            }
        }
        return new PointGroupElement(result);
    }

    public PointGroupElement mul(PointGroupElement other) {
        double[][] result = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    result[i][k] += mat[i][j] * other.mat[j][k];
                }
            }
        }
        return new PointGroupElement(result);
    }

    /**
     * Identity
     */
    public static final PointGroupElement ONE =
            new PointGroupElement(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});

    /**
     * Inversion
     */
    public static final PointGroupElement MINUS_ONE = ONE.minus();

    /**
     * Rotation of the given order around the Y-axis.
     */
    public static PointGroupElement rotation(double order) {
        double angle = 2.0 * Math.PI / order;
        return new PointGroupElement(new double[][]{
                {Math.cos(angle), 0, Math.sin(angle)},
                {0, 1, 0},
                {-Math.sin(angle), 0, Math.cos(angle)},
        });
    }

    /**
     * Rotoreflection of the given order around the Y-axis.
     */
    public static PointGroupElement rotoreflection(double order) {
        double angle = 2.0 * Math.PI / order;
        return new PointGroupElement(new double[][]{
                {Math.cos(angle), 0, Math.sin(angle)},
                {0, -1, 0},
                {-Math.sin(angle), 0, Math.cos(angle)},
        });
    }

    /**
     * Rotation of 180° around the axis (1,0,1), perpendicular to the Y-axis
     */
    public static final PointGroupElement ROT_D =
            new PointGroupElement(new double[][]{{0, 0, 1}, {0, -1, 0}, {1, 0, 0}});

    /**
     * Reflection with respect to the plane X=Z
     */
    public static final PointGroupElement REFLECT_V =
            new PointGroupElement(new double[][]{{0, 0, 1}, {0, 1, 0}, {1, 0, 0}});

    private static final double PHI = 0.5 * (1.0 + Math.sqrt(5.0));

    /**
     * Rotation of order 5 in standard orientation for the icosahedron
     */
    public static final PointGroupElement ROT_5 =
            new PointGroupElement(new double[][]{
                    {0.5, 0.5 / PHI, -0.5 * PHI},
                    {-0.5 / PHI, -0.5 * PHI, -0.5},
                    {-0.5 * PHI, 0.5, -0.5 / PHI}
            });

    /**
     * Reflection used as part of the standard icosahedral group
     */
    public static final PointGroupElement REFLECT_PHI =
            new PointGroupElement(new double[][]{
                    {-0.5 * PHI, -0.5, 0.5 / PHI},
                    {-0.5, 0.5 / PHI, -0.5 * PHI},
                    {0.5 / PHI, -0.5 * PHI, -0.5}
            });

    /**
     * Conjugate of {@link #ROT_5}
     */
    public static final PointGroupElement ROT_5_STAR =
            new PointGroupElement(new double[][]{
                {0.5, -0.5 * PHI, 0.5 / PHI},
                {0.5 * PHI, 0.5 / PHI, -0.5},
                {0.5 / PHI, 0.5, 0.5 * PHI}
            });

    /**
     * Rotation of the three coordinates
     */
    public static final PointGroupElement ROT_3 =
            new PointGroupElement(new double[][]{{0, 0, 1}, {1, 0, 0}, {0, 1, 0}});

    /**
     * Rotation of 180° around the Y-axis
     */
    public static final PointGroupElement ROT_G2 =
            new PointGroupElement(new double[][]{{-1, 0, 0}, {0, 1, 0}, {0, 0, -1}});

    /**
     * Rotation of 180° around the X-axis
     */
    public static final PointGroupElement ROT_G2STAR =
            new PointGroupElement(new double[][]{{1, 0, 0}, {0, -1, 0}, {0, 0, -1}});


}

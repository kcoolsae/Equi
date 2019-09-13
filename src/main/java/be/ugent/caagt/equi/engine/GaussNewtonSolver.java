/* GaussNewtonSolver.java
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

package be.ugent.caagt.equi.engine;

import be.ugent.caagt.equi.PlanarGraph;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Sets up the equations for a particular planar graph and allows them to be solved. Keeps
 * track of the values of the variables.
 */
public class GaussNewtonSolver {

    /**
     * List of pairs of vertex numbers corresponding to edges. Vertex numbers are already multiplied
     * by 3 in order to correspond more directly to variables.
     */
    private List<int[]> edges;

    /**
     * List of quadruples of vertex numbers corresponding to coplanar vertices.   Vertex numbers are already multiplied
     * by 3 in order to correspond more directly to variables.
     */
    private List<int[]> quads;

    /**
     * Register all quads arising from the given face,
     */
    private void addFace(int[] face) {
        int l = face.length;
        // all faces but three
        for (int i = 0; i < l - 3; i++) {
            int[] quad = new int[]{3 * face[i], 3 * face[i + 1], 3 * face[i + 2], 3 * face[i + 3]};
            quads.add(quad);
        }
        // remaining faces (for pentagons and larger)
        if (l >= 5) {
            quads.add(new int[]{3 * face[l - 1], 3 * face[0], 3 * face[1], 3 * face[2]});
            quads.add(new int[]{3 * face[l - 2], 3 * face[l - 1], 3 * face[0], 3 * face[1]});
            quads.add(new int[]{3 * face[l - 3], 3 * face[l - 2], 3 * face[l - 1], 3 * face[0]});
        }
    }

    /**
     * Setup a solver for the given graph. Keeps track of the edges and faces of that graph.
     */
    public GaussNewtonSolver(PlanarGraph graph) {
        this.edges = new ArrayList<>();
        graph.sweepEdges(edge -> edges.add(new int[]{3 * edge[0], 3 * edge[1]}));

        this.quads = new ArrayList<>();
        graph.sweepFaces(this::addFace);

        this.nrOfEquations = edges.size() + quads.size() + 6;
        this.nrOfVariables = 3 * graph.getOrder();

        this.values = new double[nrOfEquations];
        this.jacobean = new double[nrOfEquations][nrOfVariables];
    }

    private int nrOfEquations;
    private int nrOfVariables;

    /**
     * Values of the equations
     */
    private double[] values;

    /**
     * Values of the Jacobean
     */
    private double[][] jacobean;

    private double det3x3(double a11, double a12, double a13,
                          double a21, double a22, double a23,
                          double a31, double a32, double a33) {
        return a11 * a22 * a33 + a12 * a23 * a31 + a13 * a21 * a32
                - a11 * a23 * a32 - a12 * a21 * a33 - a13 * a22 * a31;
    }


    private double computeValueForEdge(double[] variables, int co0, int co1) {
        double total = -1.0;
        for (int i = 0; i < 3; i++) {
            total += (variables[co0 + i] - variables[co1 + i]) * (variables[co0 + i] - variables[co1 + i]);
        }
        return total;
    }

    private void computePartialsForEdge(double[] result, double[] variables, int co0, int co1) {

        result[co0] = 2.0 * (variables[co0] - variables[co1]);
        result[co1] = -result[co0];

        result[co0 + 1] = 2.0 * (variables[co0 + 1] - variables[co1 + 1]);
        result[co1 + 1] = -result[co0 + 1];

        result[co0 + 2] = 2.0 * (variables[co0 + 2] - variables[co1 + 2]);
        result[co1 + 2] = -result[co0 + 2];
    }

    private double[][] faceMatrix(double[] variables, int[] quad) {
        double[][] result = new double[4][4];
        for (int i = 0; i < 4; i++) {
            result[i][0] = variables[quad[i]];
            result[i][1] = variables[quad[i] + 1];
            result[i][2] = variables[quad[i] + 2];
            result[i][3] = 1.0;
        }
        return result;
    }

    private double computeValueForQuad(double[][] mat) {
        return det3x3(
                mat[0][0] - mat[3][0], mat[0][1] - mat[3][1], mat[0][2] - mat[3][2],
                mat[1][0] - mat[3][0], mat[1][1] - mat[3][1], mat[1][2] - mat[3][2],
                mat[2][0] - mat[3][0], mat[2][1] - mat[3][1], mat[2][2] - mat[3][2]
        );
    }

    private void computePartialsForQuad(double[] result, double[][] mat, int[] quad) {
        for (int r = 0; r < 4; r++) {
            int r1 = (r + 1) % 4;
            int r2 = (r + 2) % 4;
            int r3 = (r + 3) % 4;
            for (int c = 0; c < 3; c++) {
                int c1 = (c + 1) % 4;
                int c2 = (c + 2) % 4;
                int c3 = (c + 3) % 4;
                double d = det3x3(mat[r1][c1], mat[r1][c2], mat[r1][c3],
                        mat[r2][c1], mat[r2][c2], mat[r2][c3],
                        mat[r3][c1], mat[r3][c2], mat[r3][c3]);
                result[quad[r] + c] = (c + r) % 2 == 0 ? d : -d;
            }
        }
    }

    /**
     * Compute the values of the equations and the Jacobean, using the current values of the variables
     */
    private void computeValuesAndJacobean(double[] variables) {

        // Initialization
        for (double[] row : jacobean) {
            Arrays.fill(row, 0);
        }

        // FIXED POSITION IN SPACE
        //////////////////////////

        Arrays.fill(values, 0, 6, 0.0);

        // first point has fixed coordinates
        jacobean[0][0] = 1.0;
        jacobean[1][1] = 1.0;
        jacobean[2][2] = 1.0;

        // second point lies in a fixed direction with respect to the first
        jacobean[3][3] = variables[1] - variables[4];
        jacobean[3][4] = variables[3] - variables[0];
        jacobean[4][3] = variables[2] - variables[5];
        jacobean[4][5] = variables[3] - variables[0];

        // third point lies in a fixed plane with respect to the first two
        jacobean[5][6] = det3x3(
                variables[1], variables[2], 1.0,
                variables[4], variables[5], 1.0,
                variables[7], variables[8], 1.0
        );
        jacobean[5][7] = det3x3(
                variables[2], variables[0], 1.0,
                variables[5], variables[3], 1.0,
                variables[8], variables[6], 1.0
        );
        jacobean[5][8] = det3x3(
                variables[0], variables[1], 1.0,
                variables[3], variables[4], 1.0,
                variables[6], variables[7], 1.0
        );
        int c = 6;

        // EDGES MUST HAVE UNIT LENGTH
        //////////////////////////////
        for (int[] edge : edges) {
            values[c] = computeValueForEdge(variables, edge[0], edge[1]);
            computePartialsForEdge(jacobean[c], variables, edge[0], edge[1]);
            c++;
        }

        // QUADS MUST BE PLANAR
        ////////////////////////
        for (int[] quad : quads) {
            double[][] mat = faceMatrix(variables, quad);
            values[c] = computeValueForQuad(mat);
            computePartialsForQuad(jacobean[c], mat, quad);
            c++;
        }
    }

    /**
     * Compute the values of the equations only!
     */
    private void computeValuesOnly(double[] variables) {

        // FIXED POSITION IN SPACE
        //////////////////////////

        Arrays.fill(values, 0, 6, 0.0);
        int c = 6;

        // EDGES MUST HAVE UNIT LENGTH
        //////////////////////////////
        for (int[] edge : edges) {
            values[c] = computeValueForEdge(variables, edge[0], edge[1]);
            c++;
        }

        // QUADS MUST BE PLANAR
        ////////////////////////
        for (int[] quad : quads) {
            values[c] = computeValueForQuad(faceMatrix(variables, quad));
            c++;
        }
    }

    public double getAccuracy() {
        double total = 0.0;
        // orientation and position not important for line search
        for (int i = 6; i < nrOfEquations; i++) {
            total += values[i] * values[i];
        }
        return total;
    }

    public double computeAccuracy(double[] variables) {
        computeValuesOnly(variables);
        return getAccuracy();
    }

    /**
     * Compute the direction of optimization from the value vector and the Jacobean. This is where
     * Gauss Newton really happens.
     */
    private double[] computeDirection() {
        DMatrixRMaj jacoMat = new DMatrixRMaj(jacobean);

        // J^T J (nrOfCo x nrOfCo)
        DMatrixRMaj jacoSquare = new DMatrixRMaj(nrOfVariables, nrOfVariables);
        CommonOps_DDRM.multInner(jacoMat, jacoSquare);


        DMatrixRMaj fMat = new DMatrixRMaj(nrOfEquations, 1, true, values);
        // J^T value (nrOfCo x 1)
        DMatrixRMaj rhs = new DMatrixRMaj(nrOfVariables, 1);
        CommonOps_DDRM.multTransA(jacoMat, fMat, rhs);

        // result = (J^TJ)^-1 J^T value (nrOfCo x 1)
        // J^TJ result = J^T value
        DMatrixRMaj result = new DMatrixRMaj(nrOfVariables, 1);
        if (CommonOps_DDRM.solve(jacoSquare, rhs, result)) {
            return result.getData();
        } else {
            return null;
        }
    }

    /**
     * Get an estimate for the new accuracy by following the direction for a distance lambda
     * along the old vector
     */
    private double getEstimate(double lambda, double[] oldVariables, double[] direction) {
        double[] variables = new double[nrOfVariables];
        for (int i = 0; i < nrOfVariables; i++) {
            variables[i] = oldVariables[i] - lambda * direction[i];
        }
        return computeAccuracy(variables);
    }

    private static final double GR = (Math.sqrt(5.0) - 1.0) / 2.0;

    private static final double EPSILON = 1.0E-3;

    private double[]  goldenRatioSearch(double[] variables, double[] direction) {
        double[] oldVariables = variables.clone();

        double lambda1 = 0.2;
        double lambda4 = 2.0;
        double lambda3 = lambda1 + GR * (lambda4 - lambda1);
        double lambda2 = lambda4 - GR * (lambda4 - lambda1);

        double f2 = getEstimate(lambda2, oldVariables, direction);
        double f3 = getEstimate(lambda3, oldVariables, direction);

        while (lambda3 - lambda2 > EPSILON) {
            if (f2 < f3) {
                lambda4 = lambda3;
                lambda3 = lambda2;
                f3 = f2;
                lambda2 = lambda4 - GR * (lambda4 - lambda1);
                f2 = getEstimate(lambda2, oldVariables, direction);
            } else {
                lambda1 = lambda2;
                lambda2 = lambda3;
                f2 = f3;
                lambda3 = lambda1 + GR * (lambda4 - lambda1);
                f3 = getEstimate(lambda3, oldVariables, direction);
            }
        }
        double[] result = new double[nrOfVariables];
        for (int i = 0; i < nrOfVariables; i++) {
            result[i] = oldVariables[i] - (lambda2 + lambda3) / 2 * direction[i];
        }
        return result;
    }

    /**
     * Perform a single step for this solver. Updates the values of the variables array.
     */
    public double[] step(double[] variables) {
        computeValuesAndJacobean(variables);
        double[] direction = computeDirection();
        if (direction != null) {
            return goldenRatioSearch(variables, direction);
        } else {
            return variables;
        }
    }


}

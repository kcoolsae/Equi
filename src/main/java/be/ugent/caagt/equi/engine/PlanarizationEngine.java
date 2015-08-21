/* PlanarizationEngine.java
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

import be.ugent.caagt.equi.EmbeddedPlanarGraph;
import be.ugent.caagt.equi.PlanarGraph;
import be.ugent.caagt.equi.grp.CombinedGroup;

import java.util.Random;

/**
 * Main planarization code
 */
public class PlanarizationEngine {

    private GaussNewtonSolver solver;

    /**
     * Coordinates of the vertices of the current polyhedron. In the order x0, y0, z0, x1, y1, z1,...
     */
    private double[] variables;

    private int[][] edges;

    private CombinedGroup group;

    public static final Random RG = new Random(4287649098235098324L);

    public void initRandomCoordinates() {
        for (int i = 0; i < variables.length; i++) {
            variables[i] = 3.0 * RG.nextDouble() - 1.5;
        }
    }

    private void initCoordinatesFromGraph(EmbeddedPlanarGraph graph) {
        if (graph.getDimension() == 3) {
            for (int i = 0; i < graph.getOrder(); i++) {
                double[] co = graph.getCoordinates(i);
                System.arraycopy(co, 0, variables, 3 * i, 3);
            }
        } else {
            initRandomCoordinates();
        }
    }


    public void setGroup(CombinedGroup group) {
        this.group = group;
    }

    public PlanarizationEngine(PlanarGraph graph) {
        this.solver = new GaussNewtonSolver(graph);

        int s = graph.getSize();
        edges = new int[s][];
        for (int i = 0; i < s; i++) {
            edges[i] = graph.getEdge(i);
        }

        this.variables = new double[3 * graph.getOrder()];
        if (graph instanceof EmbeddedPlanarGraph) {
            initCoordinatesFromGraph((EmbeddedPlanarGraph) graph);
        } else {
            initRandomCoordinates();
        }
    }

    private double averageEdgeLength() {
        double sum = 0.0;
        for (int[] edge : edges) {
            int co0 = edge[0];
            int co1 = edge[1];
            double total = 0.0;
            for (int i = 0; i < 3; i++) {
                total += (variables[3*co0 + i] - variables[3*co1 + i]) * (variables[3*co0 + i] - variables[3*co1 + i]);
            }
            sum += Math.sqrt(total);
        }
        return sum / edges.length;
    }

    public void rescale(double factor) {
        for (int i = 0; i < variables.length; i++) {
            variables[i] *= factor;
        }
    }

    public void randomPerturbation(double step) {
        for (int i = 0; i < variables.length; i++) {
            double orig = variables[i];
            if (Double.isNaN(orig)) {
                orig = 0.0;
            }
            variables[i] = orig + (RG.nextDouble() - 0.5) * step;
        }
    }

    public void skew() {
        double xFactor = RG.nextDouble() * 1.5 + 0.5;
        double yFactor = RG.nextDouble() * 1.5 + 0.5;
        double zFactor = RG.nextDouble() * 1.5 + 0.5;
        for (int i = 0; i < variables.length; i += 3) {
            variables[i] *= xFactor;
            variables[i + 1] *= yFactor;
            variables[i + 2] *= zFactor;
        }
    }

    public void onSphere() {
        for (int i = 0; i < variables.length; i += 3) {
            double norm = variables[i] * variables[i] +
                    variables[i + 1] * variables[i + 1] +
                    variables[i + 2] * variables[i + 2];
            double factor = Math.sqrt(norm) / 2.5;
            variables[i] /= factor;
            variables[i + 1] /= factor;
            variables[i + 2] /= factor;
        }
    }

    public void center() {
        for (int j = 0; j < 3; j++) {
            double sum = 0.0;
            for (int i = j; i < variables.length; i += 3) {
                sum += variables[i];
            }
            sum /= (variables.length / 3);
            for (int i = j; i < variables.length; i += 3) {
                variables[i] -= sum;
            }
        }
    }

    public double[][] getCoordinates() {
        double[][] coordinates = new double[variables.length / 3][3];
        for (int i = 0; i < coordinates.length; i++) {
            System.arraycopy(variables, 3 * i, coordinates[i], 0, 3);
        }
        return coordinates;
    }

    public void singleStep(StepListener sl) {
        variables = solver.step(variables);
        group.symmetrize(variables);
        sl.step(1, solver.getAccuracy()); //
        center();
    }

    public void timedStep(long milliseconds, StepListener sl) {
        long time = System.currentTimeMillis();
        double initialAccuracy = solver.computeAccuracy(variables);
        variables = solver.step(variables);
        group.symmetrize(variables);
        double accuracy = solver.getAccuracy();
        sl.step(1, accuracy);
        int count = 1;
        while (accuracy > initialAccuracy * 1.0e-8 && System.currentTimeMillis() < time + milliseconds) {
            variables = solver.step(variables);
            group.symmetrize(variables);
            count ++;
            accuracy = solver.getAccuracy();
            sl.step(count, accuracy);
        }
        center();
    }

    public void multipleSteps(int count, StepListener sl) {
        for (int i = 0; i < count; i++) {
            variables = solver.step(variables);
            group.symmetrize(variables);
            sl.step(i+1, solver.getAccuracy());
        }
        center();
    }

    public void symmetrize() {
        group.symmetrize(variables);
        rescale(1.0 / averageEdgeLength());
    }

    public double computeAccuracy() {
        return solver.computeAccuracy(variables);
    }

}

/* TransformMatrix.java
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

package be.ugent.caagt.equi.fx;

import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

/**
 * Matrix form of a JavaFX transformation.
 */
class TransformMatrix {

    private double[][] matrix;

    /**
     * Identity transformation
     */
    public TransformMatrix() {
        matrix = new double[][]{
                {1.0, 0.0, 0.0, 0.0},
                {0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 1.0},
        };
    }

    private TransformMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public TransformMatrix(Transform transform) {
        matrix = new double[][]{
                {transform.getMxx(), transform.getMxy(), transform.getMxz(), transform.getTx()},
                {transform.getMyx(), transform.getMyy(), transform.getMyz(), transform.getTy()},
                {transform.getMzx(), transform.getMzy(), transform.getMzz(), transform.getTz()},
                {0.0, 0.0, 0.0, 1}
        };
    }

    /**
     * Simple scaling transform
     */
    public TransformMatrix(double scaleFactor) {
        matrix = new double[][]{
                {scaleFactor, 0, 0, 0}, {0, scaleFactor, 0, 0}, {0, 0, scaleFactor, 0}, {0, 0, 0, 1}
        };
    }

    public Transform toTransform() {

        return new Affine(
                matrix[0][0], matrix[0][1], matrix[0][2], matrix[0][3],
                matrix[1][0], matrix[1][1], matrix[1][2], matrix[1][3],
                matrix[2][0], matrix[2][1], matrix[2][2], matrix[2][3]
        );
    }

    /**
     * Matrix obtained by applying this transformation before the given transformation
     */
    public TransformMatrix before(TransformMatrix other) {
        double[][] product = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double sum = 0.0;
                for (int k = 0; k < 4; k++) {
                    sum += other.matrix[i][k] * matrix[k][j];
                }
                product[i][j] = sum;
            }
        }
        return new TransformMatrix(product);
    }

    /**
     * Matrix obtained by applying this transformation after the given transformation
     */
    public TransformMatrix after(TransformMatrix other) {
        double[][] product = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double sum = 0.0;
                for (int k = 0; k < 4; k++) {
                    sum += matrix[i][k] * other.matrix[k][j];
                }
                product[i][j] = sum;
            }
        }
        return new TransformMatrix(product);
    }

}

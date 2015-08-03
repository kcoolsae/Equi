/* Node3D.java
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

import javafx.scene.Group;
import javafx.scene.paint.Material;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

/**
 * Example 3d node for JavaFX
 */
class Node3D extends Group {

    private static final double SCALE_FACTOR = 80.0;
    private static final double RADIUS = 8.0;
    private static final double CYLINDER_RADIUS = 3.0;

    private static final double EPSILON = 0.0001; // any length smaller is considered zero


    private TransformMatrix staticTransformMatrix = new TransformMatrix();

    public void setDynamicTransform(TransformMatrix tm) {
        getTransforms().clear();
        getTransforms().add(staticTransformMatrix.before(tm).toTransform());
    }

    public void adjustStaticTransform(TransformMatrix tm) {
        staticTransformMatrix = staticTransformMatrix.before(tm);
        getTransforms().clear();
        getTransforms().add(staticTransformMatrix.toTransform());
    }

   public void addSphere(double x, double y, double z, Material material) {
        Sphere sphere = new Sphere(RADIUS);
        sphere.setTranslateX(x * SCALE_FACTOR);
        sphere.setTranslateY(y * SCALE_FACTOR);
        sphere.setTranslateZ(z * SCALE_FACTOR);
        sphere.setMaterial(material);
        getChildren().add(sphere);
    }

    public void addCylinder(double x1, double y1, double z1, double x2, double y2, double z2, Material material) {

        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = z2 - z1;
        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);

        double factor1 = Math.sqrt(dx * dx + dy * dy);

        Cylinder cylinder = new Cylinder(CYLINDER_RADIUS, SCALE_FACTOR * length - 0.8*RADIUS);

        Affine affine;
        if (factor1 > EPSILON) {
            affine = new Affine(
                    dy / factor1, dx / length , -dx * dz / factor1 / length, 0.5 * SCALE_FACTOR * (x1 + x2),
                    -dx / factor1, dy / length, -dy * dz / factor1 / length, 0.5 * SCALE_FACTOR *(y1 + y2),
                    0.0, dz / length, factor1 / length, 0.5 * SCALE_FACTOR *(z1 + z2)
            );
        } else {
            affine = new Affine(
                    1.0, 0.0, 0.0, 0.5 * SCALE_FACTOR * (x1 + x2),
                    0.0, 0.0, 1.0, 0.5 * SCALE_FACTOR * (y1 + y2),
                    0.0, 1.0, 0.0, 0.5 * SCALE_FACTOR * (z1 + z2)
            );
        }
        cylinder.getTransforms().add(affine);
        cylinder.setMaterial(material);
        getChildren().add(cylinder);
    }

    public Node3D() {


        Rotate rot1 = new Rotate(45.0, Rotate.X_AXIS);
        Rotate rot2 = new Rotate(45.0, Rotate.Y_AXIS);

        adjustStaticTransform(new TransformMatrix(rot1).before(new TransformMatrix(rot2)));

    }
}

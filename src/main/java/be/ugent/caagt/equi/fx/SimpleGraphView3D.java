/* SimpleGraphView3D.java
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

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;

import java.util.HashMap;
import java.util.Map;

/**
 * Shows a simple view of a polyhedron as a graph with spherical nodes and cylindrical edges.
 * Allows mouse interaction for 3D rotation and scroll wheel for zooming.
 */
public class SimpleGraphView3D extends SubScene {

    private Node3D node3D;

    private Map<Color, Material> materials = new HashMap<>();

    private Material getMaterial(Color color) {
        return materials.computeIfAbsent(color, PhongMaterial::new);
    }

    public void addVertex(Point3D point, Color color) {
        node3D.addSphere(point.getX(), point.getY(), point.getZ(), getMaterial(color));
    }

    public void addEdge(Point3D start, Point3D end, Color color) {
        node3D.addCylinder(
                start.getX(), start.getY(), start.getZ(),
                end.getX(), end.getY(), end.getZ(),
                getMaterial(color)
        );
    }

    public void add (Polyhedron poly) {
        poly.placeOnView(this);
    }

    public void reset() {
        node3D.getChildren().clear();
    }

    public void rescale(double factor) {
        node3D.adjustStaticTransform(new TransformMatrix(factor));
    }

    public SimpleGraphView3D() {
        this(false);
    }

    public SimpleGraphView3D(boolean perspective) {
        super(new Group(), 800, 800, true, SceneAntialiasing.BALANCED);
        // root is dummy
        node3D = new Node3D();
        node3D.setTranslateX(400);
        node3D.setTranslateY(400);
        setRoot(node3D);
        setFill(Color.gray(0.95));
        if (perspective) {
            setCamera(new PerspectiveCamera());
        }

        MouseHandler3D handler = new MouseHandler3D(node3D);
        setOnMousePressed(handler);
        setOnMouseDragged(handler);
        setOnMouseReleased(handler);

        setOnScroll(new ZoomHandler3D(node3D));
    }

}

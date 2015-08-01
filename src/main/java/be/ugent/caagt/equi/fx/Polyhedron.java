/* Polyhedron.java
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

package be.ugent.caagt.equi.fx;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a visual representation of a polyhedron. A polyhedron has vertices and edges which can be given a color.
 */
public class Polyhedron {

    public static final Color DEFAULT_VERTEX_COLOR = Color.LIGHTBLUE;

    public static final Color DEFAULT_EDGE_COLOR = Color.WHITE;

    static class PointWithColor {
        public Point3D point;
        public Color color;

        public PointWithColor(Point3D point, Color color) {
            this.point = point;
            this.color = color;
        }

    }

    static class EdgeWithColor {
        public Point3D start;
        public Point3D end;
        public Color color;

        public EdgeWithColor(Point3D start, Point3D end, Color color) {
            this.start = start;
            this.end = end;
            this.color = color;
        }
    }

    private Collection<PointWithColor> vertices;

    private Collection<EdgeWithColor> edges;

    public Polyhedron() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public void addVertex(Color c, Point3D v) {
        vertices.add(new PointWithColor(v, c));
    }

    public void addVertices(Color c, Iterable<Point3D> list) {
        for (Point3D point3D : list) {
            addVertex(c, point3D);
        }
    }

    public void addEdge(Color c, Point3D start, Point3D end) {
        edges.add(new EdgeWithColor(start, end, c));
    }

    public void addEdges(Color c, Iterable<Point3D[]> list) {
        for (Point3D[] edge3D : list) {
            addEdge(c, edge3D[0], edge3D[1]);
        }
    }

    /**
     * Add the given polyhedron to the view.
     */
    void placeOnView(SimpleGraphView3D view) {
        for (PointWithColor vertex : vertices) {
            view.addVertex(vertex.point, vertex.color);
        }
        for (EdgeWithColor edge : edges) {
            view.addEdge(edge.start, edge.end, edge.color);
        }
    }
}

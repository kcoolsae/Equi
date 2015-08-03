/* MouseHandler3D.java
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


import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;


/**
 * Event handler for mouse events
 */
class MouseHandler3D implements EventHandler<MouseEvent> {


    private Node3D node;

    public MouseHandler3D(Node3D node) {
        this.node = node;
    }

    private double xOrig;

    private double yOrig;

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            mousePressed(event);
        } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            mouseReleased(event);
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            mouseDragged(event);
        }
    }

    private TransformMatrix computeTransform(MouseEvent event) {
        double factor = event.isAltDown() ? 0.1 : 1.0;
        double xD = factor * (event.getSceneX() - xOrig);
        double yD = factor * (event.getSceneY() - yOrig);

        Rotate rotX = new Rotate(yD, Rotate.X_AXIS);
        Rotate rotY = new Rotate(-xD, Rotate.Y_AXIS);
        return new TransformMatrix(rotX).before(new TransformMatrix(rotY));

    }

    private void mouseDragged(MouseEvent event) {
        node.setDynamicTransform(computeTransform(event));
    }

    private void mouseReleased(MouseEvent event) {
        node.adjustStaticTransform(computeTransform(event));
    }

    private void mousePressed(MouseEvent event) {
        xOrig = event.getSceneX();
        yOrig = event.getSceneY();
    }
}

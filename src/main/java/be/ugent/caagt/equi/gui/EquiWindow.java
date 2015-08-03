/* EquiWindow.java
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

package be.ugent.caagt.equi.gui;

import be.ugent.caagt.equi.grp.Symmetries;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Window that displays the user interface for a single graph /polyhedron, This is the window where
 * most of the user interaction will occur.
 */
public class EquiWindow extends Stage {


    public EquiWindow(Symmetries symmetries) throws IOException {

        EquiPanelCompanion companion = new EquiPanelCompanion(symmetries, this);
        FXMLLoader loader = new FXMLLoader(EquiWindow.class.getResource("/be/ugent/caagt/equi/gui/EquiPanel.fxml"));
        loader.setController(companion);
        setScene(new Scene(loader.load()));
        setTitle("Equi - " + symmetries.getGraph().getName());
    }

}

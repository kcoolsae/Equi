/* MainPanel.java
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

package be.ugent.caagt.equi.gui;

import be.ugent.caagt.equi.PlanarGraph;
import be.ugent.caagt.equi.fx.GraphListPanel;
import be.ugent.caagt.equi.grp.Symmetries;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contents of the main window of the application. Contains a graph list panel and some menus.
 */
public class MainPanel extends BorderPane {

    private static MenuItem createMenuItemExit() {
        MenuItem exit = new MenuItem ("_Exit");
        exit.setOnAction(event -> Platform.exit());
        return exit;
    }

    private  static void processSelectedGraph (PlanarGraph graph, Stage owner) {
        try {
            Symmetries symmetries = new Symmetries(graph);
            EquiWindow window = new EquiWindow(symmetries);
            window.initOwner(owner);
            window.setTitle(symmetries.getGraph().getName());
            window.show();
        } catch (IOException e) {
            throw new RuntimeException("Could not create window", e);
        }
    }

    public MainPanel(Stage owner) {
        GraphListPanel panel = new GraphListPanel();
        panel.setGraphProcessor(graph -> processSelectedGraph(graph, owner));

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("_File");
        fileMenu.getItems().addAll(
                panel.createMenuItemOpen(owner),
                new SeparatorMenuItem(),
                createMenuItemExit()
        );

        menuBar.getMenus().addAll(fileMenu);

        setTop(menuBar);
        setCenter(panel);
    }
}

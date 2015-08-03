/* GraphListPanel.java
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

import be.ugent.caagt.equi.EmbeddedPlanarGraph;
import be.ugent.caagt.equi.PlanarGraph;
import be.ugent.caagt.equi.io.WriteGraphInputStream;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * Panel displays a list of graphs (loaded from file) and allows to select one.
 */
public class GraphListPanel extends VBox {

    private GraphListPanelCompanion companion;

    public GraphListPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GraphListPanel.fxml"));
            loader.setRoot(this);
            loader.load();
            companion = loader.getController();
        } catch (IOException ex) {
            throw new RuntimeException("Could not read FXML file", ex);
        }

        this.setOnDragOver(event -> {
            /* data is dragged over the target */
            /* accept it only if it is not dragged from the same node
             * and if it has a string data */
            event.acceptTransferModes(TransferMode.COPY);
            event.consume();
        });

        this.setOnDragDropped(event -> {
            /* data dropped */
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                db.getFiles().forEach(this::readGraphsFromFile);
            }
            event.setDropCompleted(true);
            event.consume();
        });
    }

    public void setGraphProcessor(Consumer<PlanarGraph> graphProcessor) {
        companion.setGraphProcessor(graphProcessor);
    }

    private FileChooser chooser = null;

    private File lastFile = null;

    public void doOpen(Window stage) {
        // TODO: use WriteGraphDialog
        if (chooser == null) {
            chooser = new FileChooser();
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("WriteGraph files", "*.w?d")
            );
            chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }

        if (lastFile != null) {
            chooser.setInitialDirectory(lastFile.getParentFile());
        }
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            readGraphsFromFile(file);
        }
    }

    private void readGraphsFromFile(File file) {
        try (InputStream in = new FileInputStream(file);
             WriteGraphInputStream stream = new WriteGraphInputStream(in)) {
            int count = 0;
            String fileName = file.getName();
            EmbeddedPlanarGraph graph = stream.readGraph();
            EmbeddedPlanarGraph firstGraph = graph;
            while (graph != null) {
                count++;
                graph.setName(fileName + " - " + count);
                companion.addGraph(graph);
                graph = stream.readGraph();
            }
            lastFile = file;
            if (count == 1) {
                firstGraph.setName(fileName); // remove suffix if there was only one graph in the file
            }
        } catch (IOException e) {
            // should signal error
        }
    }

    /**
     * Returns a menu item which can be used to load a graph from file.
     */
    public MenuItem createMenuItemOpen(Window window) {
        MenuItem open = new MenuItem("_Open");
        open.setOnAction(event -> doOpen(window));
        return open;
    }
}

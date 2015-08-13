/* GraphListPanelCompanion.java
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
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.util.EventListener;
import java.util.function.Consumer;

public class GraphListPanelCompanion {

    public TableView<PlanarGraph> tableView;

    public TableColumn<PlanarGraph, String> nameColumn;
    public TableColumn<PlanarGraph, Integer> orderColumn;
    public TableColumn<PlanarGraph, Integer> sizeColumn;
    public TableColumn<PlanarGraph, Integer> facesColumn;

    public ObservableList<PlanarGraph> graphs;


    public Button showButton;
    public Button removeButton;

    private TableView.TableViewSelectionModel<PlanarGraph> selectionModel;

    private Consumer<PlanarGraph> graphProcessor;

    public void setGraphProcessor(Consumer<PlanarGraph> graphProcessor) {
        this.graphProcessor = graphProcessor;
    }

    public void initialize() {
        graphs = FXCollections.observableArrayList();

        tableView.setItems(graphs);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        orderColumn.setCellValueFactory(new PropertyValueFactory<>("order"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        facesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfFaces"));

        tableView.setRowFactory(view -> {
            TableRow<PlanarGraph> result = new TableRow<>();
            result.setOnMouseClicked(event -> {
                if (event.getClickCount() > 1) {
                    graphProcessor.accept(result.getItem());
                }
            });
            return result;
        });

        tableView.setOnKeyReleased(new KeyHandler());

        this.selectionModel = tableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        selectionModel.selectedIndexProperty().addListener(new SelectionListener());

        showButton.setDisable(true);
        removeButton.setDisable(true);
    }

    class SelectionListener implements InvalidationListener {

        @Override
        public void invalidated(Observable observable) {
            boolean empty = selectionModel.isEmpty();
            showButton.setDisable(empty);
            removeButton.setDisable(empty);
        }
    }

    class KeyHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            switch (event.getCode()) {
                case DELETE:
                case BACK_SPACE:
                    doRemove();
                    break;
                case ENTER:
                    doShow();
                    break;
                default:
                    // do not consume event
                    return;
            }
            event.consume();
        }
    }

    /**
     * Shows all selected items.
     */
    public void doShow() {
        if (graphProcessor != null) {
            for (PlanarGraph graph : tableView.getSelectionModel().getSelectedItems()) {
                graphProcessor.accept(graph);
            }
        }
    }

    /**
     * Deletes all selected items from list
     */
    public void doRemove() {
        ObservableList<Integer> indices = tableView.getSelectionModel().getSelectedIndices();
        for (int i = indices.size() - 1; i >= 0; i--) {
            tableView.getItems().remove(indices.get(i).intValue());
        }
    }

    public void addGraph(EmbeddedPlanarGraph graph) {
        int size = graphs.size();
        graphs.add(graph);
        tableView.getSelectionModel().select(size);
    }
}

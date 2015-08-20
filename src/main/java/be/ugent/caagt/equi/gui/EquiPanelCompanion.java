/* EquiPanelCompanion.java
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

import be.ugent.caagt.equi.EmbeddedPlanarGraph;
import be.ugent.caagt.equi.engine.PlanarizationEngine;
import be.ugent.caagt.equi.fx.Polyhedron;
import be.ugent.caagt.equi.grp.CombinatorialGroup;
import be.ugent.caagt.equi.grp.CombinedGroup;
import be.ugent.caagt.equi.grp.Symmetries;
import be.ugent.caagt.equi.PlanarGraph;
import be.ugent.caagt.equi.fx.SimpleGraphView3D;
import be.ugent.caagt.equi.io.SpinputOutputStream;
import javafx.geometry.Point3D;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * Companion class for {@code EquiPanel.fxml}
 */
public class EquiPanelCompanion {

    public Label nrOfVertices;

    public Label nrOfEdges;

    public Label nrOfFaces;

    public VBox groupPane;

    public SimpleGraphView3D view3D;


    private PlanarizationEngine engine;

    private Stage stage;

    public Save3DDialog saveDialog;

    public Label accuracy;

    private PlanarGraph graph;

    private CombinatorialGroup group;

    public ChoiceBox<String> atomicNumber;

    public TextField scaleFactor;

    public VBox leftPane;

    public ProgressBar progressBar;

    private EquiPanelLongTask longTask;

    @SuppressWarnings("deprecation")
    public EquiPanelCompanion(Symmetries symmetries, Stage stage) {
        this.graph = symmetries.getGraph();
        this.group = symmetries.getGroup();
        this.stage = stage;
        stage.setOnCloseRequest(e -> {
            if (longTask != null) {
                longTask.kill();
            }
        });
    }

    public void initialize() {
        // Interface related
        int an = Preferences.userNodeForPackage(SpinputOutputStream.class).getInt("atomicNumber", 2);
        for (String s : atomicNumber.getItems()) {
            if (s.startsWith(an + " ")) {
                atomicNumber.setValue(s);
            }
        }
        double sf = Preferences.userNodeForPackage(SpinputOutputStream.class).getDouble("scaleFactor", 5.0);
        scaleFactor.setText(Double.toString(sf));

        // Graph related
        nrOfVertices.setText(Integer.toString(graph.getOrder()));
        nrOfEdges.setText(Integer.toString(graph.getSize()));
        nrOfFaces.setText(Integer.toString(graph.getNumberOfFaces()));

        ToggleGroup toggleGroup = new ToggleGroup();
        for (CombinatorialGroup subgroup : group.getSubgroups()) {
            HBox hbox = new HBox();
            Label caption = new Label(subgroup.toString());
            hbox.getChildren().add(caption);
            FlowPane pane = new FlowPane();
            for (CombinedGroup pointGroup : subgroup.getPointGroups()) {
                ToggleButton button = new SelectGroupButton(pointGroup);
                pane.getChildren().add(button);
                button.setToggleGroup(toggleGroup);
            }
            hbox.getChildren().add(pane);
            hbox.getStyleClass().add("group");
            groupPane.getChildren().add(hbox);
        }
        // add button for trivial group
        HBox hbox = new HBox();
        ToggleButton c1Button = new SelectGroupButton();
        c1Button.setToggleGroup(toggleGroup);
        hbox.getChildren().addAll(
                new Label("1"),
                c1Button
        );
        c1Button.setSelected(true);
        hbox.getStyleClass().add("group");
        groupPane.getChildren().add(hbox);

        toggleGroup.selectedToggleProperty().addListener(ev -> {
                    Toggle toggle = toggleGroup.getSelectedToggle();
                    if (toggle == null) {
                        c1Button.setSelected(true);
                        c1Button.requestFocus();
                    } else {
                        engine.setGroup(((SelectGroupButton) toggle).getGroup());
                    }
                }
        );

        // start with the trivial group
        CombinedGroup grp = CombinedGroup.TRIVIAL_GROUP;
        this.engine = new PlanarizationEngine(graph);
        engine.setGroup(grp);
        this.saveDialog = new Save3DDialog(stage);

        //
        showPolyhedron();
    }

    public void showPolyhedron() {
        // coincident points are colored in red
        double[][] coordinates = engine.getCoordinates();
        boolean[] coincident = new boolean[coordinates.length];

        // maybe we should to this faster?
        for (int i = 0; i < coordinates.length; i++) {
            double[] first = coordinates[i];
            for (int j = i + 1; j < coordinates.length; j++) {
                double[] second = coordinates[j];
                double distance = 0.0;
                for (int k = 0; k < 3; k++) {
                    distance += Math.abs(second[k] - first[k]);
                }
                if (distance < 1E-05) {
                    coincident[i] = true;
                    coincident[j] = true;
                }
            }
        }
        Polyhedron poly = new Polyhedron();
        List<Point3D> vertices = new ArrayList<>();
        for (int i = 0; i < coordinates.length; i++) {
            double[] coordinate = coordinates[i];


            Point3D vertex = new Point3D(
                    coordinate[0],
                    coordinate[1],
                    coordinate[2]);
            vertices.add(vertex);
            poly.addVertex(coincident[i] ? Color.RED : Color.LIGHTSALMON, vertex);
        }
        graph.sweepEdges(
                edge -> poly.addEdge(
                        coincident[edge[0]] && coincident[edge[1]] ? Color.CRIMSON : Color.WHITE,
                        vertices.get(edge[0]),
                        vertices.get(edge[1])
                )
        );

        view3D.reset();
        view3D.add(poly);
        accuracy.setText(String.format("% 7.5g", engine.computeAccuracy()));
    }


    public void doRandom() {
        engine.randomPerturbation(0.2);
        showPolyhedron();
    }

    public void doRandomLarge() {
        engine.initRandomCoordinates();
        engine.randomPerturbation(5.0);
        showPolyhedron();
    }

    public void doSingleStep() {
        runAsLongTask(engine::singleStep);
    }

    public void doRun() {
        runAsLongTask(() -> engine.timedStep(2000L));
    }

    public void doLongRun() {
        runAsLongTask(() -> engine.timedStep(15000L));
    }

    public void do5steps() {
        runAsLongTask(() -> engine.multipleSteps(5));
    }

    public void do10steps() {
        runAsLongTask(() -> engine.multipleSteps(10));
    }

    public void doInflate() {
        engine.rescale(1.5);
        showPolyhedron();
    }


    public void doSkew() {
        engine.skew();
        showPolyhedron();
    }

    public void doSymmetrize() {
        engine.symmetrize();
        showPolyhedron();
    }

    public void doSphere() {
        engine.onSphere();
        showPolyhedron();
    }

    /**
     * Export the current solution as a planar graph.
     */
    private EmbeddedPlanarGraph exportGraph() {
        double[][] coordinates = engine.getCoordinates();
        return new EmbeddedPlanarGraph(graph, coordinates);
    }


    public void doSave() {
        saveDialog.save(exportGraph(), Save3DDialog.OutputType.WRITE_GRAPH);
    }

    public void doSaveSpinput() {
        // atomic number
        String s = atomicNumber.getValue();
        int an = Integer.parseInt(s.substring(0, s.indexOf(' ')));
        saveDialog.setSpinputAtomicNumber(an);
        Preferences.userNodeForPackage(SpinputOutputStream.class).putInt("atomicNumber", an);

        // scale factor
        try {
            double d = Double.parseDouble(scaleFactor.getText());
            saveDialog.setSpinputScaleFactor(d);
            Preferences.userNodeForPackage(SpinputOutputStream.class).putDouble("scaleFactor", d);
        } catch (NumberFormatException e) {
            scaleFactor.setText("5.0");
            Logger.getLogger("be.ugent.caagt.equi").warning("Invalid scale factor for spinput");
            return; // do not save
        }

        saveDialog.save(exportGraph(), Save3DDialog.OutputType.SPINPUT);
    }

    public void doSaveObj() {
        saveDialog.save(exportGraph(), Save3DDialog.OutputType.OBJ);
    }

    private void runAsLongTask(Runnable runnable) {
        longTask = new EquiPanelLongTask(this, runnable);
        longTask.runInThread();
    }

}

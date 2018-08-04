/* EquiPanelLongTask.java
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

import be.ugent.caagt.equi.engine.StepListener;
import be.ugent.caagt.equi.undoredo.UndoStepInfo;
import javafx.concurrent.Task;

import java.util.function.Consumer;

/**
 * Task that takes potentially a long time to run. Before starting the GUI is disabled and
 * a progress bar is shown. At the end the progress bar is hidden and the GUI is restored.
 */

class EquiPanelLongTask extends Task<Void> implements StepListener {

    private EquiPanelCompanion equiPanelCompanion;
    private Consumer<StepListener> consumer;

    EquiPanelLongTask(EquiPanelCompanion equiPanelCompanion, Consumer<StepListener> consumer) {
        this.equiPanelCompanion = equiPanelCompanion;
        this.consumer = consumer;
        messageProperty().addListener(
                p -> {
                    equiPanelCompanion.accuracy.setText(getMessage());
                    equiPanelCompanion.progressLabel.setText(String.format("Steps: % 4d", getStepCount()));
                }
        );

    }

    @Override
    protected Void call() throws Exception {
        consumer.accept(this);
        return null;
    }

    @Override
    protected void running() {
        equiPanelCompanion.setDisable(true);
        equiPanelCompanion.progressLabel.setText("Steps:    0");
        equiPanelCompanion.progressPane.setVisible(true);
    }

    @Override
    protected void succeeded() {
        equiPanelCompanion.progressPane.setVisible(false);
        equiPanelCompanion.setDisable(false);
        equiPanelCompanion.showPolyhedron();
        thread = null;
    }

    @Override
    protected void failed() {
        succeeded();
    }

    @Override
    protected void cancelled() {
        succeeded();
    }


    private Thread thread;

    public void runInThread() {
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    @SuppressWarnings("deprecation")
    public void kill() {
        if (thread != null) {
            // deprecated
            thread.stop(); // :-( I see no other solution except forking the jvm...
        }
    }

    private int stepCount;

    public int getStepCount() {
        return stepCount;
    }

    @Override
    public void step(int number, double accuracy) {
        updateMessage(String.format("% 7.5g", accuracy));
        this.stepCount = number;
        equiPanelCompanion.undoManager.addStep(
                new UndoStepInfo(
                        equiPanelCompanion.engine.getCoordinates(),
                        equiPanelCompanion.engine.computeAccuracy()
                ));
    }
}

package be.ugent.caagt.equi.gui;

import be.ugent.caagt.equi.engine.StepListener;
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

    public void kill() {
        if (thread != null) {
            //noinspection deprecation
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
    }
}

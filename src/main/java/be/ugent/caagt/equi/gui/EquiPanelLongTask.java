package be.ugent.caagt.equi.gui;

import javafx.concurrent.Task;

/**
 * Task that takes potentially a long time to run. Before starting the GUI is disabled and
 * a progress bar is shown. At the end the progress bar is hidden and the GUI is restored.
 */

class EquiPanelLongTask extends Task<Void> {

    private EquiPanelCompanion equiPanelCompanion;
    private Runnable runnable;

    EquiPanelLongTask(EquiPanelCompanion equiPanelCompanion, Runnable runnable) {
        this.equiPanelCompanion = equiPanelCompanion;
        this.runnable = runnable;
    }

    @Override
    protected Void call() throws Exception {
        runnable.run();
        return null;
    }

    @Override
    protected void running() {
        equiPanelCompanion.leftPane.setDisable(true);
        equiPanelCompanion.progressBar.setVisible(true);
    }

    @Override
    protected void succeeded() {
        equiPanelCompanion.progressBar.setVisible(false);
        equiPanelCompanion.leftPane.setDisable(false);
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

}

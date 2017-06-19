package be.ugent.caagt.equi.undoredo;


/**
 * Stores information needed to undo and redo steps
 */
public class UndoInfo {

    private UndoStepInfo[] infos;

    private int size;

    private int current;

    public UndoInfo () {
        size = 0;
        current = -1;
        infos = new UndoStepInfo[20];
    }

    public double[][] getCoordinates () {
        return infos[current].getCoordinates();
    }
    public double getAccuracy () {
        return infos[current].getAccuracy();
    }

    public void add(UndoStepInfo stepInfo) {
        // clear redos
        for (int i=current+1; i < infos.length; i++) {
            infos[i] = null;
        }
        size = current + 1;

        // add element
        if (size < infos.length) {
            infos[size] = stepInfo;
            current ++;
            size ++;
        } else {
            System.arraycopy(infos, 1, infos, 0, infos.length - 1);
            infos[infos.length - 1] = stepInfo;
        }
    }

    public boolean canUndo() {
        return current > 0;
    }

    public UndoStepInfo undo() {
        if (current > 0) {
            current --;
            return infos[current];
        } else {
            return null;
        }
    }

    public boolean canRedo() {
        return current + 1 < size;
    }

    public UndoStepInfo redo() {
        if (current + 1 < size) {
            current ++;
            return infos[current];
        } else {
            return null;
        }
    }


}

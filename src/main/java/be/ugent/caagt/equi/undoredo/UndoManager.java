package be.ugent.caagt.equi.undoredo;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps track of undo and redo information
 */

public class UndoManager implements Observable {

    private  ArrayList<UndoInfo> list = new ArrayList<UndoInfo>();

    private int current = -1;

    public void addCommand() {
        // clear redos
        list.subList(current+1,list.size()).clear();

        // add element
        UndoInfo info = new UndoInfo();
        list.add(info);
        current ++;

        fireInvalidated();
    }

    public void addStep(UndoStepInfo stepInfo) {
        // clear redos
        list.subList(current + 1, list.size()).clear();
        list.get(current).add(stepInfo);
        fireInvalidated();
    }

    public boolean canUndo() {
        return current > 0;
    }

    public UndoInfo undo() {
        if (current > 0) {
            current --;
            fireInvalidated();
            return list.get(current);
        } else {
            return null;
        }
    }

    public boolean canRedo() {
        return current + 1 < list.size();
    }

    public UndoInfo redo() {
        if (current + 1 < list.size()) {
            current ++;
            fireInvalidated();
            return list.get(current);
        } else {
            return null;
        }
    }

    public boolean canUndoStep() {
        return current >= 0 && list.get(current).canUndo();
    }

    public UndoStepInfo undoStep() {
        if (current < 0) {
            return null;
        } else {
            UndoStepInfo result = list.get(current).undo();
            fireInvalidated();
            return result;
        }
    }

    public boolean canRedoStep() {
        return current >= 0 && list.get(current).canRedo();
    }

    public UndoStepInfo redoStep() {
        if (current < 0) {
            return null;
        } else {
            UndoStepInfo result = list.get(current).redo();
            fireInvalidated();
            return result;
        }
    }

    private List<InvalidationListener> listeners = new ArrayList<>();

    @Override
    public void addListener(InvalidationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }

    private void fireInvalidated() {
        for (InvalidationListener listener : listeners) {
            listener.invalidated(this);
        }
    }
}

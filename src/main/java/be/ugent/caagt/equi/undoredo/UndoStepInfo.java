package be.ugent.caagt.equi.undoredo;

/**
 * Stores information needed to undo or redo a single step
 */

public class UndoStepInfo implements Info {

    private double[][] coordinates;

    public double[][] getCoordinates() {
        return coordinates;
    }

    private double accuracyValue;

    public double getAccuracy() {
        return accuracyValue;
    }

    public UndoStepInfo(double[][] orig, double accuracyValue) {
        this.accuracyValue = accuracyValue;
        this.coordinates = new double[orig.length][3];
        double[] center = new double[3];

        for (double[] v : orig) {
            center[0] += v[0];
            center[1] += v[1];
            center[2] += v[2];
        }

        center[0] /= orig.length;
        center[1] /= orig.length;
        center[2] /= orig.length;

        for (int i = 0; i < orig.length; i++) {
            coordinates[i] = new double[]{
                    orig[i][0] - center[0],
                    orig[i][1] - center[1],
                    orig[i][2] - center[2]
            };
        }
    }
}

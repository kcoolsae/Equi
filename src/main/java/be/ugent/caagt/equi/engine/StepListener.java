package be.ugent.caagt.equi.engine;

/**
 * Interface for classes that need to be notified when the engine has performed a single step
 * as part of a multistep operation.
 */
public interface StepListener {

    public void step(int number, double accuracy);
}

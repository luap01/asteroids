package Controller;

public interface SteeringImplementation {

    boolean getShooting();

    boolean getPausePressed();

    void setPausePressed();

    double[] getDir();

    //boolean getInputType(); //private attributes not possible for interfaces TODO: do we really need this method?

}

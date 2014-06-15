package tarea2;

/**
 * Clase Entorno.
 *
 * Representa las direcciones que se puede mover el jugador
 *
 * @author Juan Ignacio Fuentes Quinteros, Rol: 201373067-1
 * @version 1.1 may 8 2014
 */
public class Environment {

    /**
     * Representa Adelante está disponible
     */
    private boolean forward;
    /**
     * Representa Atrás está disponible
     */
    private boolean back;
    /**
     * Representa Derecha está disponible
     */
    private boolean right;
    /**
     * Representa Izquierda está disponible
     */
    private boolean left;

    /**
     * Se utiliza para observar las direcciones posibles del entorno.
     * 
     * Se inicializan todos como falso
     *
     */
    public Environment() {
        this.forward = false;
        this.back = false;
        this.right = false;
        this.left = false;
    }

    /**
     * Indica si la casilla de adelante está disponible
     *
     * @return Boolean si adelante está libre
     */
    public boolean isForward() {
        return forward;
    }

    public boolean stuck(){
        return !forward && !back && !right && !left;
    }
    /**
     * Cambia el estado de forward
     *
     * @param forward True: diponible, False: Inaccesible
     */
    public void setForward(boolean forward) {
        this.forward = forward;
    }

    /**
     * Indica si la casilla de atrás está disponible
     *
     * @return Boolean si atrás está libre
     */
    public boolean isBack() {
        return back;
    }

    /**
     * Cambia el estado de back
     *
     * @param back True: diponible, False: Inaccesible
     */
    public void setBack(boolean back) {
        this.back = back;
    }

    /**
     * Indica si la casilla de derecha está disponible
     *
     * @return Boolean si derecha está libre
     */
    public boolean isRight() {
        return right;
    }

    /**
     * Cambia el estado de right
     *
     * @param right True: diponible, False: Inaccesible
     */
    public void setRight(boolean right) {
        this.right = right;
    }

    /**
     * Indica si la casilla de izquierda está disponible
     *
     * @return Boolean si izquierda está libre
     */
    public boolean isLeft() {
        return left;
    }

    /**
     * Cambia el estado de left
     *
     * @param left True: diponible, False: Inaccesible
     */
    public void setLeft(boolean left) {
        this.left = left;
    }

}

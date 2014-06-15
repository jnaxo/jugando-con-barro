package tarea2;

import java.awt.Point;

/**
 * Clase Elemento del juego
 *
 * @author Juan Ignacio Fuentes Quinteros, Rol: 201373067-1
 * @version 1.1 may 8 2014
 */
public class Game_Element {

    /**
     * Nombre del elemento del juego
     */
    protected String name;
    /**
     * Posición en el tablero
     */
    protected Point location;

    /**
     * Constructor Clase
     */
    public Game_Element() {

    }

    /**
     * Obtiene el nombre del elemento
     * 
     * @return String con el nombre del elemento
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene la posición en el trablero del elemento
     * @return posición "x", posición "y" encapsuladas con la clase Point
     */
    public Point getLocation() {
        return location;
    }

}

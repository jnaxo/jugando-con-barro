package tarea2;

import java.awt.Point;

/**
 * Clase Objeto del juego.
 *
 * Hereda de Game_Element
 *
 * @author Juan Ignacio Fuentes Quinteros, Rol: 201373067-1
 * @version 1.1 may 8 2014
 */
public class Item extends Game_Element {

    /**
     * Tipo de objeto
     */
    private final char item_type;
    /**
     * Modificador asociado al objeto
     */
    private final int mod;
    /**
     * Duración del objeto cuando está activo
     */
    private int duration;

    /**
     * Constructor de la clase item
     *
     * @param _name Nombre del Objeto
     * @param _item_type Tipo del objeto
     * @param _mod Modificador asociado
     * @param _location Posición en el mapa, Null si la tiene el jugador
     */
    public Item(String _name, char _item_type, int _mod, Point _location) {
        this.name = _name;
        this.item_type = _item_type;
        this.mod = _mod;
        this.location = _location;
    }

    /**
     * Se usa para identificar el tipo de objeto instanciado
     *
     * @return Tipo del objeto
     */
    public char getItem_type() {
        return item_type;
    }

    /**
     * Se usa para tomar el valor del modificador del objeto
     *
     * @return Valor del modificador del objeto
     */
    public int getMod() {
        return mod;
    }

    /**
     * Se usa para ver la duración del objeto cuando está activo
     *
     * @return Integer con el valor de la duración
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Se usa para modificar la duración del objeto
     * @param duration duración que se le agrega a la actual. Inicialmente 0
     */
    public void aumentDuration(int duration) {
        this.duration += duration;
    }

}

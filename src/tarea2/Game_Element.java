/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea2;

import java.awt.Point;

/**
 *
 * @author nacho
 */
public class Game_Element {

    /**
     * atributos heredables
     */
    protected String name;
    protected Point location;

    /**
     * constructor clase
     */
    public Game_Element() {

    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }
    
}

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
public class Item extends Game_Element {

    /**
     * atributos
     */
    private final char item_type;
    private final int mod; // modificador asociado
    private int duration;

    /**
     *
     * @param _name
     * @param _item_type
     * @param _mod
     * @param _location
     */
    public Item(String _name, char _item_type, int _mod, Point _location) {
        this.name = _name;
        this.item_type = _item_type;
        this.mod = _mod;
        this.location = _location;
    }

    public boolean use(Player player, Enemy target) {
        if (this.item_type == 'H' || this.item_type == 'D' ||this.item_type == 'O') {
            player.use_item(this, target);
            return true;
        }
        return false;
    }

    public char getItem_type() {
        return item_type;
    }

    public int getMod() {
        return mod;
    }

    @Override
    public String toString() {
        String tipo = new String();
        String str_mod = new String();
        switch (this.item_type) {
            case 'W':
                tipo = "Arma";
                str_mod = ", Daño: +";
                break;
            case 'A':
                tipo = "Armadura";
                str_mod = ", Defensa: +";
                break;
            case 'H':
                tipo = "Curativo";
                str_mod = ", Salud: +";
                break;
            case 'D':
                tipo = "Protector";
                str_mod = ", Escudo: +";
                break;
            case 'O':
                tipo = "Ofensivo";
                str_mod = ", Ataca: ";
                break;
        }
        return "Item: " + this.name + ", Tipo: " + tipo + str_mod + this.mod;
    }

    public int getDuration() {
        return duration;
    }

    public void aumentDuration(int duration) {
        this.duration += duration;
    }

}

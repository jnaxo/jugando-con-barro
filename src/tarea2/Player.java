/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea2;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author nacho
 */
public class Player extends Game_Character {

    /**
     * atributos
     */
    private int max_hp;
    private int level;
    private Item weapon;
    private Item armor;
    private Item defensive_item;
    private ArrayList<Item> inventory; // setter and getter
    private int inventory_cap;
    private boolean running;

    /**
     * Constructor
     *
     * @param _name
     * @param _hp
     * @param _location
     */
    public Player(String _name, int _hp, Point _location) { // implements draw element
        this.name = _name;
        this.hp = _hp;
        this.max_hp = _hp;
        this.location = _location;
        this.attack = 0;
        this.defense = 0;
        this.inventory = new ArrayList();
        this.inventory_cap = 10;
        this.level = 1;
        this.exp_points = 0;
    }

    public boolean take_item(Item item) {
        if (this.inventory.size() < this.inventory_cap) {
            this.inventory.add(item);
            return true;
        }
        return false;
    }

    public boolean equip(Item item) {

        if (item.getItem_type() == 'W') {
            this.inventory.add(this.weapon);
            this.attack -= this.weapon.getMod();
            this.weapon = item;
            this.attack += this.weapon.getMod();
            return true;
        } else if (item.getItem_type() == 'A') {
            this.inventory.add(this.armor);
            this.defense -= this.armor.getMod();
            this.armor = item;
            this.defense += this.armor.getMod();
            return true;
        }
        return false;
    }

    public boolean use_item(Item item, Enemy target) {
        switch (item.getItem_type()) { //consumir
            case 'H':
                this.hp += item.getMod();
                if (this.getHp() > this.max_hp) {
                    this.hp = this.max_hp;
                }
                this.inventory.remove(item);
                return true;
            case 'D':
                item.aumentDuration(3);
                this.defense += item.getMod();
                this.defensive_item = item;
                this.inventory.remove(item);
                return true;
            case 'O':
                target.hurt(item.getMod());
                inventory.remove(item);
                return true;
        }
        return false;
    }

    public boolean clearDefensiveItems(String item_name) {
        if (this.defensive_item.getDuration() == 0) {
            this.defensive_item = null;
            return true;
        }
        this.defensive_item.aumentDuration(-1);
        return false;
    }

    public Point discart(Item item) {
        this.inventory.remove(item);
        return this.location;
    }

    public void aument_exp(int xp) {

    }

    private void levelUp() {
        if (this.level < 100) {
            this.level++;
            this.inventory_cap += 2;
            this.max_hp += 10;
            this.hp += 10;
        }
    }

    public Item getWeapon() {
        return weapon;
    }

    public void setWeapon(Item weapon) {
        this.weapon = weapon;
        this.attack += weapon.getMod();
    }

    public Item getArmor() {
        return armor;
    }

    public void setArmor(Item armor) {
        this.armor = armor;
        this.defense += armor.getMod();
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public boolean setInventory(ArrayList<Item> inventory) {
        if (inventory.size() < this.inventory_cap) {
            this.inventory = inventory;
            return true;
        }
        return false;
    }

    public int getDefense() {
        return defense;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMax_hp() {
        return max_hp;
    }

    public void setMax_hp(int max_hp) {
        this.max_hp = max_hp;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}

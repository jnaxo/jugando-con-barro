/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea2;

/**
 *
 * @author nacho
 */
public class Game_Character extends Game_Element {

    /**
     * Atributos heredables: ...
     */
    protected boolean alive;
    protected int hp;
    protected int defense;
    protected int attack;
    protected int exp_points;

    //...
    /**
     * Constructor Superclase Game_Character
     */
    public Game_Character() {
        this.alive = true;

    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        this.alive = false;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getExp_points() {
        return exp_points;
    }

    public boolean hurt(int damage) {
        this.hp -= damage - this.defense;
        if (this.hp <= 0) {
            this.die();
        }
        return this.alive;
    }

    public boolean fight(Game_Character target) {
        return target.hurt(this.getAttack());
    }
}

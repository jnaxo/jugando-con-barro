/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea2;

import cl.utfsm.inf.lp.sem12014.mud.logic.exceptions.GameLogicException;
import cl.utfsm.inf.lp.sem12014.mud.logic.interfaces.EnemyInterface;
import java.awt.Point;

/**
 *
 * @author nacho
 */
public class Enemy extends Game_Character implements EnemyInterface {

    /**
     * atributos
     */
    private char enemy_type; // s:soilder, g: guardian, .etc
    private boolean aggro;

    /**
     * contructor
     */
    public Enemy() {
        this.defense = 0;
        this.aggro = false;
    }

    @Override
    public void setName(String string) throws GameLogicException {
        try {
            this.name = string;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error on function setName().");
        }
    }

    @Override
    public void setHitPoints(int i) throws GameLogicException {
        try {
            this.hp = i;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error on function setHitPoints().");
        }
    }

    @Override
    public void setAttackPoints(int i) throws GameLogicException {
        try {
            this.attack = i;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error on function setAttackPoints().");
        }
    }

    @Override
    public void setExpPoints(int i) throws GameLogicException {
        try {
            this.exp_points = i;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error on function setExpPoints().");
        }
    }

    @Override
    public void setPosition(Point point) throws GameLogicException {
        try {
            this.location = point;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error on function setPosition().");
        }
    }

    @Override
    public String toString() {
        return "Enemigo: " + this.name + ", HP: " + this.hp + ", Ataque: " + this.attack;
    }

    public boolean isAggro() {
        return aggro;
    }

    public void setAggro(boolean aggro) {
        this.aggro = aggro;
        System.out.println("enemigo provocado");
    }

}

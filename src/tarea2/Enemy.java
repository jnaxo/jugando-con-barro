package tarea2;

import cl.utfsm.inf.lp.sem12014.mud.logic.exceptions.GameLogicException;
import cl.utfsm.inf.lp.sem12014.mud.logic.interfaces.EnemyInterface;
import java.awt.Point;

/**
 * Clase Enemigo
 *
 * @author Juan Ignacio Fuentes Quinteros, Rol: 201373067-1
 * @version 1.1 may 8 2014
 */
public class Enemy extends Game_Character implements EnemyInterface {

    /**
     * Indica si el enemigo está provocado.
     */
    private boolean aggro;

    /**
     * Constructor de la clase Enemy
     */
    public Enemy() {
        this.defense = 0;
        this.aggro = false;
    }

    /**
     * Método para darle nombre al enemigo
     *
     * @param string Nombre del enemigo
     * @throws GameLogicException Representa una excepción genérica dentro de la
     * lógica del juego.
     */
    @Override
    public void setName(String string) throws GameLogicException {
        try {
            this.name = string;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error on function setName().");
        }
    }

    /**
     * Permite cambiar los puntos de impacto (o salud) del enemigo representado
     * por este objeto.
     *
     * @param i Puntos de vida máxima
     * @throws GameLogicException Representa una excepción genérica dentro de la
     * lógica del juego.
     */
    @Override
    public void setHitPoints(int i) throws GameLogicException {
        try {
            this.hp = i;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error on function setHitPoints().");
        }
    }

    /**
     * Permite cambiar los puntos de ataque del enemigo representado por este
     * objeto.
     *
     * @param i Puntos de ataque
     * @throws GameLogicException Representa una excepción genérica dentro de la
     * lógica del juego.
     */
    @Override
    public void setAttackPoints(int i) throws GameLogicException {
        try {
            this.attack = i;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error on function setAttackPoints().");
        }
    }

    /**
     * Permite cambiar los puntos de experiencia que se obtienen de derrotar al
     * enemigo representado por este objeto.
     *
     * @param i
     * @throws GameLogicException Representa una excepción genérica dentro de la
     * lógica del juego.
     */
    @Override
    public void setExpPoints(int i) throws GameLogicException {
        try {
            this.exp_points = i;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error on function setExpPoints().");
        }
    }

    /**
     * Permite cambiar los puntos de experiencia que se obtienen de derrotar al
     * enemigo representado por este objeto.
     *
     * @param point
     * @throws GameLogicException Representa una excepción genérica dentro de la
     * lógica del juego.
     */
    @Override
    public void setPosition(Point point) throws GameLogicException {
        try {
            this.location = point;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error on function setPosition().");
        }
    }

    /*
     * Se utiliza para mostrar los atributos de la clase enemigo como String
     *
     * @return
     *
    @Override
    public String toString() {
        return "Enemigo: " + this.name + ", HP: " + this.hp + ", Ataque: " + this.attack;
    }*/

    /**
     * Se utiliza para ver si el enemigo tiene que atacar al jugador
     *
     * @return Boolean con el estado de aggro, true: tiene que atacar, false: no
     * tiene que atacar
     */
    public boolean isAggro() {
        return aggro;
    }

    /**
     * Permite cambiar el estado del aggro del enemigo
     *
     * @param aggro true: atacar, false: dejar de atacar
     */
    public void setAggro(boolean aggro) {
        this.aggro = aggro;
    }
}

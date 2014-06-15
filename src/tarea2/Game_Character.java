package tarea2;

/**
 * Clase Personaje del juego. Hereda de Game_Element
 *
 * @author Juan Ignacio Fuentes Quinteros, Rol: 201373067-1
 * @version 1.1 may 8 2014
 */
public class Game_Character extends Game_Element {

    /**
     * Representa si el personaje está vivo
     */
    protected boolean alive;
    /**
     * Puntos de Impacto
     */
    protected int hp;
    /**
     * Puntos de defensa
     */
    protected int defense;
    /**
     * Puntos de ataque
     */
    protected int attack;
    /**
     * Puntos de experiencia
     */
    protected int exp_points;

    //...
    /**
     * Constructor de Game_Character
     *
     * Inicia al personaje como vivo
     */
    public Game_Character() {
        this.alive = true;

    }

    /**
     * Método para ver si el personaje está vivo
     *
     * @return true: vivo, false: muerto
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Mata al persojane
     */
    public void die() {
        this.alive = false;
    }

    /**
     * Método para obtener lospuntos de ataque del personaje
     *
     * @return los puntos de ataque del personaje
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Método para obtener los puntos de experiencia
     *
     * @return Integer con los puntos de experiencia
     */
    public int getExp_points() {
        return exp_points;
    }

    /**
     * Daña al personaje.
     *
     * Reduce el hp del personaje segun el daño inflingido y la armadura
     *
     * @param damage Integer con el daño de la fuente
     * @return
     */
    public boolean hurt(int damage) {
        if (damage > this.defense) {
            this.hp -= damage - this.defense;
        }
        if (this.hp <= 0) {
            this.die();
        }
        return this.alive;
    }

    /**
     * Método para pelear contra un personaje del juego.
     *
     * @param target Objetivo a dañar
     * @return Boolean que indica si el target quedó vivo
     */
    public boolean fight(Game_Character target) {
        return target.hurt(this.attack);
    }
}

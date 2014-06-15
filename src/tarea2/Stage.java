package tarea2;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Stage representa una casillla
 *
 * @author Juan Ignacio Fuentes Quinteros, Rol: 201373067-1
 * @version 1.1 may 8 2014
 */
public class Stage {

    /**
     * Indica si la casilla ha sido visitada por el jugador
     */
    private boolean visited;
    /**
     * Lista de objetos presentes en la casilla
     */
    private final ArrayList<Item> items;
    /**
     * Lista de enemigos presentes en la casilla
     */
    private final List<Enemy> enemies;
    /**
     * Indica si la casilla es inaccecible
     */
    private boolean wall;

    /**
     * Constructor de la clase Stage
     */
    public Stage() {
        this.items = new ArrayList();
        this.enemies = new ArrayList<>();
        this.visited = false;
    }

    /**
     * Obtiene los objetos de la casilla
     *
     * @return ArrayList con los items presentes en la casilla
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * Agrega un objeto a la casilla
     *
     * @param item Objeto que se agrega
     */
    public void addItem(Item item) {
        this.items.add(item);
    }

    /**
     * Obtiene los enemigos presentes en la casilla
     *
     * @return List con todos los enemigos de la casilla
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Agrega un enemigo a la casilla
     *
     * @param enemy Enemigo que se agrega
     */
    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }

    /**
     * Quita un enemigo de la casilla
     *
     * @param enemy Enemigo que se quita
     */
    public void removeEnemy(Enemy enemy) {
        this.enemies.remove(enemy);
    }

    /**
     * Se ocupa para ver si el jugador paso por la casilla
     *
     * @return Boolean que indica si la casilla fue visitada
     */
    public boolean isVisited() {
        return this.visited;
    }

    /**
     * Permite cambiar el estado de la casilla a visitada o viceversa
     *
     * @param visited Boolean para indicar si fue visitada la casilla
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Activa/Desactiva el aggro de todos los enemigos de la casilla
     *
     * @param active True: activar, False: desactivar
     */
    public void threat(boolean active) {
        this.enemies.stream().forEach((enemy) -> {
            enemy.setAggro(active);
        });
    }

    /**
     * Permite que la casilla sea accesible o inaccesible
     *
     * @param wall Boolean para Habilitar o deshabilitar la casilla
     */
    public void setWall(boolean wall) {
        this.wall = wall;
    }

    /**
     * Se usa para determinar si la casilla es un muro
     *
     * @return Boolean True: inaccesible, False: Accesible
     */
    public boolean isWall() {
        return this.wall;
    }

    /**
     * Quita un objeto de la casilla
     *
     * @param name_item string nombre del objeto
     * @return Boolean que indica si el objeto es válido
     */
    public boolean removeItem(String name_item) {
        for (Item item : this.items) {
            if (item.getName().equals(name_item)) {
                this.items.remove(item);
                return true;
            }
        }
        return false;
    }

    /**
     * Determina si existe al menos un enemigo provocado
     *
     * @return Boolean true: algun enemigo provocado, false: ningun enemigo
     * provocado
     */
    public boolean isThreat() {
        boolean threat = false;
        for (Enemy enemy : enemies) {
            if (enemy.isAggro()) {
                threat = true;
                break;
            }
        }
        return threat;
    }

}

package tarea2;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Board que representa el tablero del juego. Este contiene al jugador,
 * las casillas, enemigos y objetos.
 *
 * @author Juan Ignacio Fuentes Quinteros, Rol: 201373067-1
 * @version 1.1 may 8 2014
 */
public class Board {

    /**
     * Dimension que encapsula el ancho y largo del tablero medido en casillas
     */
    private final Dimension dimension;
    /**
     * Lista con las coordenadas de los muros.
     */
    private final List<Point> walls;
    /**
     * Arreglo bidimensional de las casillas que representa el tablero
     */
    private final ArrayList<Stage>[] stages;
    /**
     * Indica si los enemigos de todas las casillas están muertos
     */
    private boolean extinct_enemies;
    /**
     * Intancia del jugador.
     */
    private final Player player;
    /**
     * Indica si se exploraron todas las casillas
     */
    private int board_explored;
    /**
     * Turnos que tienen que pasar para que termine el juego con todos los
     * enemigos muertos
     */
    private int e_dead_cond;

    /**
     * Constructor del tablero
     *
     * @param _dimension Dimensión del nuevo tablero
     * @param _walls Coordenadas de las murallas
     * @param items Objetos presentes en el tablero
     * @param enemies Enemigos presentes en el tablero
     * @param _player Jugador
     */
    public Board(Dimension _dimension, List<Point> _walls,
            ArrayList<Item> items, List<Enemy> enemies, Player _player) {
        this.dimension = _dimension;
        this.walls = _walls;
        this.player = _player;
        this.stages = new ArrayList[dimension.height];
        this.board_explored = _dimension.height * _dimension.width;
        this.extinct_enemies = false;
        this.e_dead_cond = 10;

        /* se crean las casillas */
        for (int y = 0; y < dimension.height; y++) {
            this.stages[y] = new ArrayList<>();
            for (int x = 0; x < dimension.width; x++) {
                Stage s = new Stage();
                this.stages[y].add(s);
            }
        }
        /* se agregan las murallas */
        for (Point w : this.walls) {
            this.stages[w.x].get(w.y).setWall(true);
            this.board_explored--;
        }
        /* se agregan los items */
        for (Item i : items) {
            this.stages[i.getLocation().x].get(i.getLocation().y).addItem(i);
        }
        /* se agregan los enemies */
        for (Enemy e : enemies) {
            this.stages[e.getLocation().x].get(e.getLocation().y).addEnemy(e);
        }

        /* player visita stage inicial */
        this.board_explored--;
        this.stages[this.player.getLocation().x].get(this.player.getLocation().y).setVisited(true);
    }

    /**
     * Método que mueve al jugador a otra casilla en el tablero
     *
     * @param dir Dirección a la cual se movera el jugador
     * @param steps Casillas que debería avanzar
     * @return boolean indicando si el movimiento es válido
     */
    public boolean movePlayer(String dir, int steps) {
        switch (dir) {
            case "avanzar":
                if (steps == 2
                        && (this.player.getLocation().x <= 1
                        || this.stages[this.player.getLocation().x - 1].get(this.player.getLocation().y).isWall()
                        || this.stages[this.player.getLocation().x - 2].get(this.player.getLocation().y).isWall())) {
                    steps = 1;
                    this.player.run(false);
                }
                if (this.player.getLocation().x > 0
                        && !this.stages[this.player.getLocation().x - steps].get(this.player.getLocation().y).isWall()) {
                    this.player.setLocation(new Point(this.player.getLocation().x - steps, this.player.getLocation().y));
                    if (!this.getStagePlayer().isVisited()) {
                        this.getStagePlayer().setVisited(true);
                        this.board_explored--;
                    }
                    return true;
                }
                break;

            case "retroceder":
                if (steps == 2
                        && (this.player.getLocation().x >= (this.dimension.height - steps)
                        || this.stages[this.player.getLocation().x + 1].get(this.player.getLocation().y).isWall()
                        || this.stages[this.player.getLocation().x + 2].get(this.player.getLocation().y).isWall())) {
                    steps = 1;
                    this.player.run(false);
                    System.out.println("no hay donde correr");
                }
                if (this.player.getLocation().x < (this.dimension.height - steps)
                        && !this.stages[this.player.getLocation().x + steps].get(this.player.getLocation().y).isWall()) {
                    this.player.setLocation(new Point(this.player.getLocation().x + steps, this.player.getLocation().y));
                    if (!this.getStagePlayer().isVisited()) {
                        this.getStagePlayer().setVisited(true);
                        this.board_explored--;
                    }
                    return true;
                }
                break;
            case "derecha":
                if (steps == 2
                        && (this.player.getLocation().y >= (this.dimension.width - steps)
                        || this.stages[this.player.getLocation().x].get(this.player.getLocation().y + 1).isWall()
                        || this.stages[this.player.getLocation().x].get(this.player.getLocation().y + 2).isWall())) {
                    steps = 1;
                    this.player.run(false);
                    System.out.println("no hay donde correr");
                }
                if (this.player.getLocation().y < (this.dimension.width - steps)
                        && !this.stages[this.player.getLocation().x].get(this.player.getLocation().y + steps).isWall()) {
                    this.player.setLocation(new Point(this.player.getLocation().x, this.player.getLocation().y + steps));
                    if (!this.getStagePlayer().isVisited()) {
                        this.getStagePlayer().setVisited(true);
                        this.board_explored--;
                    }
                    return true;
                }
                break;

            case "izquierda":
                if (steps == 2
                        && (this.player.getLocation().y <= 1
                        || this.stages[this.player.getLocation().x].get(this.player.getLocation().y - 1).isWall()
                        || this.stages[this.player.getLocation().x].get(this.player.getLocation().y - 2).isWall())) {
                    steps = 1;
                    this.player.run(false);
                    System.out.println("no hay donde correr");
                }
                if (this.player.getLocation().y > 0
                        && !this.stages[this.player.getLocation().x].get(this.player.getLocation().y - steps).isWall()) {
                    this.player.setLocation(new Point(this.player.getLocation().x, this.player.getLocation().y - steps));
                    if (!this.getStagePlayer().isVisited()) {
                        this.getStagePlayer().setVisited(true);
                        this.board_explored--;
                    }
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Método para interacturar con la casilla del jugador
     *
     * @return Retorna la casilla donde se encuentra actualmente el jugador
     */
    public Stage getStagePlayer() {
        return stages[this.player.getLocation().x].get(this.player.getLocation().y);
    }

    /**
     * Método para observar casillas accesibles
     *
     * @return String con todas las direcciones de los movimientos posibles del
     * jugador
     */
    public Environment getFreeStages() {
        Environment environment = new Environment();
        Point forward = new Point(player.getLocation().x - 1, player.getLocation().y);
        Point back = new Point(player.getLocation().x + 1, player.getLocation().y);
        Point right = new Point(player.getLocation().x, player.getLocation().y + 1);
        Point left = new Point(player.getLocation().x, player.getLocation().y - 1);

        environment.setForward(forward.x >= 0 && !stages[forward.x].get(forward.y).isWall());
        environment.setBack(back.x < this.dimension.width && !stages[back.x].get(back.y).isWall());
        environment.setRight(right.y < this.dimension.height && !stages[right.x].get(right.y).isWall());
        environment.setLeft(left.y >= 0 && !stages[left.x].get(left.y).isWall());

        return environment;
    }

    /**
     * Método para interactuar con el jugador
     *
     * @return EL jugador
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Método para ver cuantas casillas no han sido exploradas
     *
     * @return La cantidad de casilas que faltan por explorar
     */
    public int getBoard_explored() {
        return board_explored;
    }

    /**
     * Método para contar turnos con todos los enemigos muertos
     *
     * @return True: si pasaron todos los turnos posibles con los enemigos
     * muertos. False: si quedan enemigos o quedan turnos que realizar
     */
    public boolean enemiesDead() {
        if (!this.extinct_enemies) {
            for (int y = 0; y < dimension.height; y++) {
                for (int x = 0; x < dimension.width; x++) {
                    if (!this.stages[y].get(x).getEnemies().isEmpty()) {
                        return false;
                    }
                }
            }
        }
        this.extinct_enemies = true;
        this.e_dead_cond--;
        return this.e_dead_cond == -1;
    }
}

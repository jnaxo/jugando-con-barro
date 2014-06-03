/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea2;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nacho
 */
public class Board {

    /**
     * atributos:
     */
    private final Dimension dimension;
    private final List<Point> walls;
    private final ArrayList<Stage>[] stages;
    private final ArrayList<Item> items;
    private final List<Enemy> enemies;
    private final Player player;

    /**
     * constructor
     *
     * @param _dimension
     * @param _walls
     * @param _items
     * @param _enemies
     * @param _player
     */
    public Board(Dimension _dimension, List<Point> _walls,
            ArrayList<Item> _items, List<Enemy> _enemies, Player _player) {
        this.dimension = _dimension;
        this.walls = _walls;
        this.items = _items;
        this.player = _player;
        this.enemies = _enemies;
        this.stages = new ArrayList[dimension.height];

        /* se crean los stages */
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
        }
        /* se agregan los items */
        for (Item i : this.items) {
            this.stages[i.getLocation().x].get(i.getLocation().y).addItem(i);
        }
        /* se agregan los enemies */
        for (Enemy e : this.enemies) {
            this.stages[e.getLocation().x].get(e.getLocation().y).addEnemy(e);
        }

        /* se agrega el player al mapa*/
        //this.map_cells[this.player.getLocation().x].get(this.player.getLocation().y).setImagen("link.png");
    }

    public Dimension getDimension() {
        return dimension;
    }

    public boolean movePlayer(String dir, boolean run) {
        int steps = 1;
        if (run) {
            steps = 2;            
        }
        this.player.setRunning(run);
        switch (dir) {
            case "avanzar":
                if (steps == 2
                        && (this.player.getLocation().x <= 1
                        || this.stages[this.player.getLocation().x - 1].get(this.player.getLocation().y).isWall()
                        || this.stages[this.player.getLocation().x - 2].get(this.player.getLocation().y).isWall())) {
                    steps = 1;
                    this.player.setRunning(false);
                }
                if (this.player.getLocation().x > 0
                        && !this.stages[this.player.getLocation().x - steps].get(this.player.getLocation().y).isWall()) {
                    this.getStagePlayer().setVisited(true);
                    this.player.setLocation(new Point(this.player.getLocation().x - steps, this.player.getLocation().y));
                    this.getStagePlayer().threat(run);
                    return true;
                }
                break;

            case "retroceder":
                if (steps == 2
                        && (this.player.getLocation().x >= (this.dimension.height - steps)
                        || this.stages[this.player.getLocation().x + 1].get(this.player.getLocation().y).isWall()
                        || this.stages[this.player.getLocation().x + 2].get(this.player.getLocation().y).isWall())) {
                    steps = 1;
                    this.player.setRunning(false);
                }
                if (this.player.getLocation().x < (this.dimension.height - steps)
                        && !this.stages[this.player.getLocation().x + steps].get(this.player.getLocation().y).isWall()) {
                    this.getStagePlayer().setVisited(true);
                    this.player.setLocation(new Point(this.player.getLocation().x + steps, this.player.getLocation().y));
                    this.getStagePlayer().threat(run);
                    return true;
                }
                break;
            case "derecha":
                if (steps == 2
                        && (this.player.getLocation().y >= (this.dimension.width - steps)
                        || this.stages[this.player.getLocation().x].get(this.player.getLocation().y + 1).isWall()
                        || this.stages[this.player.getLocation().x].get(this.player.getLocation().y + 2).isWall())) {
                    steps = 1;
                    this.player.setRunning(false);
                }
                if (this.player.getLocation().y < (this.dimension.width - steps)
                        && !this.stages[this.player.getLocation().x].get(this.player.getLocation().y + steps).isWall()) {
                    this.getStagePlayer().setVisited(true);
                    this.player.setLocation(new Point(this.player.getLocation().x, this.player.getLocation().y + steps));
                    this.getStagePlayer().threat(run);
                    return true;
                }
                break;

            case "izquierda":
                if (steps == 2
                        && (this.player.getLocation().y <= 1
                        || this.stages[this.player.getLocation().x].get(this.player.getLocation().y - 1).isWall()
                        || this.stages[this.player.getLocation().x].get(this.player.getLocation().y - 2).isWall())) {
                    steps = 1;
                    this.player.setRunning(false);
                }
                if (this.player.getLocation().y > 0
                        && !this.stages[this.player.getLocation().x].get(this.player.getLocation().y - steps).isWall()) {
                    this.getStagePlayer().setVisited(true);
                    this.player.setLocation(new Point(this.player.getLocation().x, this.player.getLocation().y - steps));
                    this.getStagePlayer().threat(run);
                    return true;
                }
                break;
        }

        return false;
    }

    public Stage getStagePlayer() {
        return stages[this.player.getLocation().x].get(this.player.getLocation().y);
    }
  //  public Stage getStageFordward(){
    //    return stages[];
    //}

    public Player getPlayer() {
        return player;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

}

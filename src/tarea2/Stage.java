/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea2;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nacho
 */
public class Stage {

    private boolean visited;
    private boolean threat;
    private final ArrayList<Item> items;
    private final List<Enemy> enemies;
    private boolean wall;

    public Stage() {
        this.items = new ArrayList();
        this.enemies = new ArrayList<>();
        this.visited = false;
        this.threat = false;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }

    public void removeEnemy(Enemy enemy) {
        this.enemies.remove(enemy);
    }

    public boolean isVisited() {
        return this.visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void threat(boolean active) {
        this.enemies.stream().forEach((enemy) -> {
            enemy.setAggro(active);
            this.threat = active;
            if (active) {
                System.out.println("\tHas provocado al enemigo " + enemy.getName());
            }
        });
    }

    public void setWall(boolean wall) {
        this.wall = wall;
    }

    public boolean isWall() {
        return this.wall;
    }

    public ArrayList<Game_Element> watch() {
        ArrayList<Game_Element> elements_stage = new ArrayList<>();

        for (Item item : this.items) {
            elements_stage.add(item);
        }

        for (Enemy enemy : this.enemies) {
            elements_stage.add(enemy);
        }
        return elements_stage;
    }

    public boolean removeItem(String name_item) {
        for (Item item : this.items) {
            if (item.getName().equals(name_item)) {
                this.items.remove(item);
                return true;
            }
        }
        return false;
    }

    public boolean isThreat() {
        return threat;
    }

    public void setThreat(boolean threat) {
        this.threat = threat;
    }

}

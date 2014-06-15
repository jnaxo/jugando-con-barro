package tarea2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa al jugador. Hereda de Game_Character
 *
 * @author Juan Ignacio Fuentes Quinteros, Rol: 201373067-1
 * @version 1.1 may 8 2014
 */
public class Player extends Game_Character {

    /**
     * Vida máxima del jugador
     */
    private int max_hp;
    /**
     * Nivel del jugador
     */
    private int level;
    /**
     * Arma del jugador
     */
    private Item weapon;
    /**
     * Armadura del jugador
     */
    private Item armor;
    /**
     * Objeto protector activo. (null si no tiene uno activo).
     */
    private Item defensive_item;
    /**
     * Inventario del jugador
     */
    private ArrayList<Item> inventory;
    /**
     * Capacidad máxima del inventario
     */
    private int inventory_cap;
    /**
     * Turnos corriendo
     */
    private int time_running;
    /**
     * Turnos que no puede correr
     */
    private int weary;
    /**
     * Estado que indica si el jugador está corriendo o caminando
     */
    private boolean running;
    /**
     * Puntos de experiencia necesarios para subir de nivel
     */
    private final int exp_max;

    /**
     * Indica que el jugador está descansando
     */
    private boolean resting;

    /**
     * Constructor que crea un jugador según las reglas del juego.
     *
     * @param _name Nombre del jugador
     * @param _hp Hit Points iniciales del jugador
     * @param _location Posición inicial del jugador
     */
    public Player(String _name, int _hp, Point _location) {
        this.name = _name;
        this.hp = _hp;
        this.max_hp = _hp;
        this.location = _location;
        this.attack = 0;
        this.defense = 0;
        this.inventory = new ArrayList();
        this.inventory_cap = 10;
        this.level = 1;
        this.exp_max = this.setExpMax();
        this.exp_points = 0;
        this.defensive_item = null;
        this.running = false;
        this.time_running = 1;
        this.weary = 0;
        this.resting = false;
    }

    /**
     * Método que se utiliza para tomar un objeto de la casilla actual del
     * jugador
     *
     * @param item Objeto que se toma
     * @return Boolean que indica si el objeto está presente en la casilla.
     */
    public boolean take_item(Item item) {
        if (this.inventory.size() < this.inventory_cap) {
            this.inventory.add(item);
            return true;
        }
        return false;
    }

    /**
     * Método que se utiliza para equipar un arma o armadura del inventario
     *
     * @param item Objeto de tipo Arma(W) o Armadura(A).
     * @return Boolean para indicar si el objeto es del tipo correspondiente
     */
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

    /**
     * Método que se utiliza para usar un objeto consumible
     *
     * @param item Es el objeto que se va a utilizar
     * @return Boolean que indica si el item hizo efecto
     */
    public boolean use_item(Item item) {
        switch (item.getItem_type()) {
            /* Sanar */
            case 'H':
                if (this.hp == this.max_hp) {
                    return false;
                }
                this.hp += item.getMod();
                if (this.hp > this.max_hp) {
                    this.hp = this.max_hp;
                }
                this.inventory.remove(item);
                return true;
            /* Proteger */
            case 'D':
                if (defensive_item == null) {
                    item.aumentDuration(3);
                    this.defense += item.getMod();
                    this.defensive_item = item;
                    this.inventory.remove(item);
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Método que se utiliza para usar un objeto consumible
     *
     * @param item Es el objeto que se va a utilizar
     * @param targets Son los objetivos (Enemigos) que afectara el objeto
     * @return Boolean que indica si el item hizo efecto
     */
    public boolean use_item(Item item, List<Enemy> targets) {
        if (item.getItem_type() == 'O') {
            targets.stream().forEach((enemy) -> {
                enemy.hurt(item.getMod());
            });
            inventory.remove(item);
            return true;
        }
        return false;
    }

    /**
     * Método que cada turno va descontando la duración del objeto protector si
     * está activo
     *
     * @return Boolean que indica se terminó la duración del objeto
     */
    public boolean clearDefensiveItems() {
        if (this.defensive_item == null) {
            return false;
        }
        if (this.defensive_item.getDuration() == 0) {
            this.defensive_item = null;
            return true;
        }
        this.defensive_item.aumentDuration(-1);
        return false;
    }

    /**
     * Método que descarta un objeto del inventario
     *
     * @param key String que indica el nombre del objeto
     * @return Item si se encuentra en el inventario. Null si no lo encuentra
     */
    public Item discart(String key) {
        for (Item i : inventory) {
            if (i.getName().equalsIgnoreCase(key)) {
                this.inventory.remove(i);
                return i;
            }
        }
        return null;
    }

    /**
     * Método privado que aumenta la experiencia del jugador.
     *
     * @param xp Puntos de experiencia.
     * @return Boolean que indica si sube de nivel
     */
    public boolean aument_exp(int xp) {
        this.exp_points += xp;
        if (this.exp_max < this.exp_points && this.levelUp()) {
            this.exp_points -= this.exp_max;
            this.setExpMax();
            return true;
        }
        return false;
    }

    /**
     * Método privado que calcula los puntos necesarios para subir de nivel
     *
     * @return Integer con el valor necesario para subir de nivel
     */
    private int setExpMax() {
        int exp = 0;
        for (int n = 1; n < this.level - 1; n++) {
            exp += Math.round(Math.pow(2, (n / 7)));
        }
        return 9 * exp;
    }

    /**
     * Método privado que sube de nivel al jugador hasta un máximo de nivel 100.
     * Ver README - supuestos.
     *
     * @return Boolean que indica si se logró subir de nivel o si esta en el
     * nivel máximo
     */
    private boolean levelUp() {
        if (this.level < 100) {
            this.level++;
            this.inventory_cap += 2;
            this.max_hp += 10;
            this.hp += 10;
            return true;
        }
        return false;
    }

    /**
     * Método para asignar un arma al jugador
     *
     * @param weapon Objeto tipo arma (W)
     * @return Boolean que indica si se logró equipar el arma
     */
    public boolean setWeapon(Item weapon) {
        if (weapon.getItem_type() == 'W') {
            this.weapon = weapon;
            this.attack += weapon.getMod();
            return true;
        }
        return false;
    }

    /**
     * Método para asignar una armadura al jugador
     *
     * @param armor Objeto tipo armadura (A)
     * @return Boolean que indica si se logró equipar la armadura
     */
    public boolean setArmor(Item armor) {
        if (armor.getItem_type() == 'A') {
            this.armor = armor;
            this.defense += armor.getMod();
            return true;
        }
        return false;
    }

    /**
     * Método para ver los objetos del inventario del jugador
     *
     * @return ArrayList con los items del inventario
     */
    public ArrayList<Item> getInventory() {
        return inventory;
    }

    /**
     * Método para que "setea" el inventario
     *
     * @param inventory Inventario que se implementa
     * @return Boolean que indica si se seteo el inventario del jugador
     * correctamente
     */
    public boolean setInventory(ArrayList<Item> inventory) {
        if (inventory.size() < this.inventory_cap) {
            this.inventory = inventory;
            return true;
        }
        return false;
    }

    /**
     * Método que muestra los puntos de defensa actuales del jugador
     *
     * @return Integer con los puntos de defensa
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Método que cambia de posición al jugador a una casilla accesible
     *
     * @param location Tipo Point que indica la nueva ubicación del jugador
     */
    public void setLocation(Point location) {
        this.location = location;
    }

    /**
     * Método para indicar que el jugador tiene mas de 4 turnos corriendo
     *
     * @return Boolean que indica si está cansado de correr
     */
    public boolean isWeary() {
        return this.time_running > 4;
    }

    /**
     * Método para cambiar el estado de caminar a correr y viceversa. Si se
     * mantiene corriendo se cuentan los turnos en ese estado
     *
     * @param run Boolean para indicar nuevo estado, true: correr, false:
     * caminar
     */
    public void run(boolean run) {
        if (run && this.running) {
            this.time_running++;
        } else {
            this.running = run;
        }
    }

    /**
     * Método que indica si el jugador está corriendo o caminando
     *
     * @return True: corriendo, False: caminando
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Método para que setear que tan cansado esta el jugador luego de correr
     *
     * @param weary Integer que indica los turnos que durará cansado
     */
    public void setWeary(int weary) {
        this.weary = weary;
    }

    /**
     * Método que indica el nivel de cansancio del jugador
     *
     * @return Integer con los turnos que faltan para que el jugador pueda
     * volver a correr
     */
    public int getWeary() {
        return weary;
    }

    /**
     * Método que indica cuantos turnos tiene el jugador corriendo
     *
     * @return Integer con los turnos que el jugador esta corriendo
     */
    public int getTime_running() {
        return time_running;
    }

    /**
     * Método que hace descansar al jugador
     */
    public void rest() {
        if (this.weary > 0) {
            this.weary--;
        }
    }

    /**
     * Método que indica que objeto protector está activo
     *
     * @return Item protector activo
     */
    public Item getDefensive_item() {
        return defensive_item;
    }

    /**
     * sirve para ver si el jugador está descansando
     *
     * @return true: si esta descansando, false: si ya descanzó
     */
    public boolean isResting() {
        return resting;
    }

    /**
     * Modifica el estado de descansando del jugador
     *
     * @param resting true: entra en estado de descanzar, false: ya descanzó
     */
    public void setResting(boolean resting) {
        this.resting = resting;
    }

    /**
     * Modifica el valor del tiempo que lleva corriendo
     *
     * @param time_running Turnos que lleva corriendo
     */
    public void setTime_running(int time_running) {
        this.time_running = time_running;
    }

    /**
     * Obtiene el Hp del jugador
     *
     * @return Hp actual del jugador
     */
    public int getHp() {
        return hp;
    }

    /**
     * obtiene el arma que el jugador tiene equipada actualmente
     *
     * @return Objeto arma del jugador
     */
    public Item getWeapon() {
        return weapon;
    }

    /**
     * Obtiene el Hp máximo del jugador
     *
     * @return Integer con el valor del Hp máximo del jugador
     */
    public int getMax_hp() {
        return max_hp;
    }

    /**
     * Obtiene la capacidad máxima del inventario del jugador
     *
     * @return Integer con el valor de la capacidad máxima del inventario
     */
    public int getInventory_cap() {
        return inventory_cap;
    }

    public int getLevel() {
        return level;
    }

}

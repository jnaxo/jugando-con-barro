/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea2;

import cl.utfsm.inf.lp.sem12014.mud.input.*;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ju2an Ignacio Fuentes Quinteros, Rol: 201373067-1
 */
public class Tarea2 {

    /**
     * atributos
     */
    private Board board;
    private final String dir, p_path, b_path, i_path, e_path, m_path;
    private MessageReader m_reader;

    /**
     * Constructor
     */
    public Tarea2() {
        dir = "/home/nacho/Escritorio/Leng de Prog/tarea2/";
        p_path = "player.mud";
        b_path = "board.mud";
        i_path = "items.mud";
        e_path = "enemies.mud";
        m_path = "messages.mud";
        initElements();
    }

    private void initElements() {
        Player player;
        ArrayList<Item> player_inventory;
        ArrayList<Item> items;
        try {
            PlayerReader p_reader = new PlayerReader(dir + p_path);
            BoardReader b_reader = new BoardReader(dir + b_path);
            ItemReader i_reader = new ItemReader(dir + i_path);
            EnemyReader e_reader = new EnemyReader(dir + e_path, Enemy.class);
            this.m_reader = new MessageReader(dir + m_path);

            /* load player */
            Item player_weapon = new Item(p_reader.getWeapon().getKey(), 'W', p_reader.getWeapon().getValue(), null);
            Item player_armor = new Item(p_reader.getArmor().getKey(), 'A', p_reader.getArmor().getValue(), null);
            player_inventory = new ArrayList<>();

            for (String key : p_reader.getKeys()) {
                char type = p_reader.getType(key);
                int mod = p_reader.getValue(key);
                Item i = new Item(key, type, mod, null);
                player_inventory.add(i);
            }
            player = new Player(p_reader.getName(), p_reader.getHitPoints(), p_reader.getPosition());
            player.setWeapon(player_weapon);
            player.setArmor(player_armor);
            player.setInventory(player_inventory);

            /* load items into board */
            items = new ArrayList<>();
            for (String key : i_reader.getKeys()) {
                char type = i_reader.getType(key);
                int mod = i_reader.getValue(key);
                Point loc = i_reader.getPosition(key);
                Item i = new Item(key, type, mod, loc);
                items.add(i);
            }
            /*enemies*/
            List<Enemy> enemies = e_reader.getEnemies();

            /* load board */
            this.board = new Board(b_reader.getDimension(), b_reader.getImpassablePoints(), items, enemies, player);
            System.out.println(this.m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_INIT));

        } catch (IOException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Tarea2.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(this.m_reader.getMessage(MessageReader.MessageCode.MESSAGE_ERROR));
        }
    }

    /* polimorfismo */
    private boolean runCommand(String command) {
        switch (command) {
            case "observar":
                this.board.getStagePlayer().watch().stream().forEach((element) -> {
                    System.out.println("\t" + element.toString());
                });
                //mostrar direcciones posibles??
                return true;
            case "inventario":
                this.board.getPlayer().getInventory().forEach((item) -> {
                    System.out.println("\t" + item.toString());
                });
                return true;
            default:
                System.out.println("\tInstrucción inválida");
        }
        return false;
    }

    private boolean runCommand(String command, String value) {
        switch (command) {
            case "correr":
                if (this.board.movePlayer(value, true)) {
                    System.out.println("\tTe has movido a " + this.board.getPlayer().getLocation());
                    return true;
                } else {
                    System.out.println("\tMovimiento no permitido u orden no válida");
                }
                break;

            case "caminar":
                if (this.board.movePlayer(value, false)) {
                    System.out.println("\tTe has movido a " + this.board.getPlayer().getLocation());
                    return true;
                } else {
                    System.out.println("\tMovimiento no permitido u orden no válida");
                }
                break;

            case "tomar":
                for (Item item : this.board.getStagePlayer().getItems()) {
                    if (item.getName().equalsIgnoreCase(value)) {
                        if (!this.board.getPlayer().take_item(item)) {
                            System.out.println("\t" + this.m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_INVENTORY_FULL).replace("%s", value));
                        } else {
                            this.board.getStagePlayer().removeItem(item.getName());
                            System.out.println("\tSe agregó el item " + item.toString() + " al inventario");
                            return true;
                        }
                        return false;
                    }
                }
                System.out.println("\tNo se encontraron coincidencias");
                break;

            case "pelear":
                for (Enemy enemy : this.board.getStagePlayer().getEnemies()) {
                    if (enemy.getName().equalsIgnoreCase(value)) {
                        boolean target_alive = this.board.getPlayer().fight(enemy);
                        System.out.println("\t" + value + " ha sufrido " + this.board.getPlayer().getAttack() + " de daño");
                        if (!target_alive) {
                            this.board.getStagePlayer().removeEnemy(enemy);
                            System.out.println("\tHas matado a " + value);
                        } else {
                            enemy.setAggro(true);
                            this.board.getStagePlayer().setThreat(true);
                        }
                        return true;
                    }
                }
                System.out.println("\tNo se encotro Enemigo");
                break;

            case "equipar":
                for (Item item : this.board.getPlayer().getInventory()) {
                    if (item.getName().equalsIgnoreCase(value)) {
                        if (this.board.getPlayer().equip(item)) {
                            this.board.getPlayer().getInventory().remove(item);
                            System.out.println("\tSe equipo " + item.toString());
                            return true;
                        } else {
                            System.out.println("\t" + m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR) + "tipo invalido");
                        }
                        return false;
                    }
                }
                System.out.println("\t" + m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR) + "no se encontro item en el inventario");
                break;

            case "descartar":
                break;
            default:
                System.out.println("\tInstrucción inválida");
        }
        return false;
    }

    private boolean runCommand(String command, String value, Game_Character target) {
        switch (command) {
            case "usar":
                for (Item item : this.board.getPlayer().getInventory()) {
                    if (item.getName().equalsIgnoreCase(value)) {
                        item.use(this.board.getPlayer(), (Enemy) target);
                        return true;
                    }
                }
                break;

            default:
                System.out.println("\tInstrucción inválida");
        }
        return false;
    }

    private void enemyTurn() {
        for (Enemy enemy : this.board.getStagePlayer().getEnemies()) {
            if (enemy.isAggro()) {
                boolean target_alive = enemy.fight(this.board.getPlayer());
                System.out.println("\t" + enemy.getName()
                        + " te ha inflingido "
                        + (enemy.getAttack() - this.board.getPlayer().getDefense())
                        + " de daño");
                if (!target_alive) {
                    System.out.println("Te han matado");
                    return;
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean next_turn;
        String command;
        String defensive_item = new String();
        Tarea2 game = new Tarea2();
        int sp;

        /* Turno player */
        System.out.print("-- Tu turno --\n   ");
        command = input.nextLine();
        while (!command.equals("terminar")) {
            /* runcomand cambia el estado del stage evaluando si el enemigo tiene que atacar */
            if (command.contains(" ")) {
                sp = command.indexOf(" ");
                //command.indexof("->")
                next_turn = game.runCommand(command.substring(0, sp), command.substring(sp + 1));
            } else {
                next_turn = game.runCommand(command);
            }
            if (next_turn) {
                /* turno enemigo si corresponde */
                if (game.board.getStagePlayer().isThreat()) {
                    System.out.println("-- Turno del enemigo --");
                    game.enemyTurn();
                }
                if (game.board.getPlayer().clearDefensiveItems(defensive_item)) {
                    System.out.println("Se acabó la duración del objeto protector " + defensive_item);
                }
                System.out.println("-- Tu turno --");
            }
            System.out.print("   ");
            command = input.nextLine();
        }
        System.out.println("- Saliendo del Juego -");
    }

}

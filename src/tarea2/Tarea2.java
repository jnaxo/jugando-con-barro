package tarea2;

import cl.utfsm.inf.lp.sem12014.mud.input.*;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase Principal que ejecuta el juego.
 *
 * @author Juan Ignacio Fuentes Quinteros, Rol: 201373067-1
 * @version 1.1 may 8 2014
 */
public class Tarea2 {

    /**
     * Variable Privada: Es una instancia del tablero del juego.
     */
    private Board board;
    /**
     * Variable Privada: Indica el directorio del package "working directory"
     * donde se encuentran los archivos necesarios para iniciar el juego
     */
    private final String dir, p_path, b_path, i_path, e_path, m_path;
    private MessageReader m_reader;

    /**
     * Constructor de la clase Tarea2. Requiere los archivos: player.mud,
     * board.mud, items.mud, enemies.mud y messages.mud ubicados en la carpeta
     * working_directory
     */
    public Tarea2() {
        dir = this.getClass().getResource("/working_directory/").getPath();
        p_path = "player.mud";
        b_path = "board.mud";
        i_path = "items.mud";
        e_path = "enemies.mud";
        m_path = "messages.mud";
        initElements();
    }

    /**
     * Método que lee los archivos necesarios para iniciar el juego y agrega los
     * elementos al tablero.
     *
     */
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

            /* Crear Jugador */
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

            /* Crear Items */
            items = new ArrayList<>();
            for (String key : i_reader.getKeys()) {
                char type = i_reader.getType(key);
                int mod = i_reader.getValue(key);
                Point loc = i_reader.getPosition(key);
                Item i = new Item(key, type, mod, loc);
                items.add(i);
            }
            /* Crear Enemigos */
            List<Enemy> enemies = e_reader.getEnemies();

            /* Crea tablero con los todos sus elementos e inicializa el juego */
            this.board = new Board(b_reader.getDimension(), b_reader.getImpassablePoints(), items, enemies, player);
            System.out.println(this.m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_INIT));

        } catch (IOException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Tarea2.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(this.m_reader.getMessage(MessageReader.MessageCode.MESSAGE_ERROR));
        }
    }

    /**
     * Método que ejecuta una instrucción válida del jugador
     *
     * @param command Instrucción que se ejecutará. Comandos válidos: avanzar,
     * retroceder, derecha, izquieda, correr, caminar, observar, inventario,
     * terminar
     * @return Boolean que indica si la instrcción es válida.
     */
    public boolean runCommand(String command) {
       
        switch (command) {
            case "avanzar":
            case "retroceder":
            case "derecha":
            case "izquierda":
                int steps = 1;
                String str_dir = formatDir(command);
                String str_steps = "";
                this.board.getStagePlayer().threat(this.board.getPlayer().isRunning());
                /* Correr si el estado del jugador lo indica y contar turnos corriendo */
                if (this.board.getPlayer().isRunning()) {
                    steps = 2;
                    this.board.getPlayer().run(true);
                }
                /* Dejar de correr por cansancio */
                if (this.board.getPlayer().isRunning() && this.board.getPlayer().getWeary() > 0) {
                    this.board.getPlayer().run(false);
                    this.board.getPlayer().setWeary(-1);
                    System.out.println("\t"
                            + String.format(
                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_MOVE_RUN_END),
                                    board.getPlayer().getName()
                            )
                    );
                    steps = 1;
                }
                /*  Puede volver a correr */
                if (this.board.getPlayer().isResting() && this.board.getPlayer().getWeary() == 0) {
                    this.board.getPlayer().setResting(false);
                    System.out.println("\t"
                            + String.format(
                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_MOVE_COOLDOWN_END),
                                    this.board.getPlayer().getName()
                            )
                    );
                }
                if (this.board.movePlayer(command, steps)) {
                    if (!board.getPlayer().isRunning()) {
                        str_steps = m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_MOVE_ONE);
                    } else if (board.getPlayer().isRunning()) {
                        str_steps = m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_MOVE_TWO);
                    }
                    if (!board.getPlayer().isRunning() && steps == 2) {
                        System.out.println("\t"
                                + String.format(
                                        m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_MOVE_TYPE_WALK),
                                        board.getPlayer().getName()
                                )
                        );
                    }
                    System.out.println("\t"
                            + String.format(
                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_MOVE),
                                    board.getPlayer().getName(),
                                    str_steps,
                                    str_dir
                            )
                    );

                } else {
                    System.out.println("\t"
                            + String.format(
                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR),
                                    str_dir,
                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR_MOVE)
                            )
                    );
                    return false;
                }
                return true;

            case "correr":
                if (this.board.getPlayer().getWeary() > 0) {
                    System.out.println("\t"
                            + String.format(
                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR_RUN_COOLDOWN),
                                    board.getPlayer().getName()
                            )
                    );
                    return false;
                }
                if (!board.getPlayer().isRunning()) {
                    this.board.getPlayer().run(true);
                    System.out.println("\t"
                            + String.format(
                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_MOVE_TYPE_RUN),
                                    board.getPlayer().getName()
                            )
                    );
                    return true;
                } else {
                    System.out.println("\t"
                            + String.format(
                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR_RUN),
                                    board.getPlayer().getName()
                            )
                    );
                }
                break;
            case "caminar":
                if (board.getPlayer().isRunning()) {
                    this.board.getPlayer().run(false);
                    System.out.println("\t"
                            + String.format(
                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_MOVE_TYPE_WALK),
                                    board.getPlayer().getName()
                            )
                    );
                    return true;
                } else {
                    System.out.println("\t"
                            + String.format(
                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR_WALK),
                                    board.getPlayer().getName()
                            )
                    );
                }
                break;
            case "observar":
                String items_output = m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_CELL_ITEM_CONTENT);
                String enemies_output = m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_CELL_ENEMY_CONTENT);//MISMO DE ARRIBA
                String items = new String();
                String str_enemies = new String(); 
                String type;
                /* watch items */
                if (!this.board.getStagePlayer().getItems().isEmpty()) {
                    for (Item item : this.board.getStagePlayer().getItems()) {
                        type = formatItemType(item.getItem_type());
                        items += String.format(m_reader.getMessage(
                                MessageReader.MessageCode.MESSAGE_GAME_INVENTORY_CONTENT_ITEM),
                                item.getName(), type, item.getMod()
                        );
                    }
                    System.out.println("\t" + String.format(items_output, board.getPlayer().getName(), items));
                } else {
                    System.out.println("\t" + m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_CELL_ITEM_EMPTY));
                }
                /* watch enemies */
                if (!this.board.getStagePlayer().getEnemies().isEmpty()) {
                    str_enemies = this.board.getStagePlayer().getEnemies().stream().map(
                            (enemy)
                            -> enemy.getName() + " ").reduce(str_enemies, String::concat
                            );
                    System.out.println("\t" + String.format(enemies_output, board.getPlayer().getName(), str_enemies));
                } else {
                    System.out.println("\t" + m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_CELL_ENEMY_EMPTY));
                }
                /* watch free stages */
                Environment free_stages = this.board.getFreeStages();
                if (free_stages.stuck()) {
                    System.out.println(this.m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_CELL_MOVEMENT_EMPTY));

                } else {
                    System.out.println("\t"
                            + String.format(
                                    this.m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_CELL_MOVEMENT_OPTIONS),
                                    board.getPlayer().getName(),
                                    formatDir(free_stages)
                            )
                    );
                }
                return true;

            case "inventario":
                if (!this.board.getPlayer().getInventory().isEmpty()) {
                    String str_output = m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_INVENTORY_CONTENT);
                    String str_item = new String();
                    String type_obj;
                    for (Item item : board.getPlayer().getInventory()) {
                        type_obj = formatItemType(item.getItem_type());
                        str_item += String.format(m_reader.getMessage(
                                MessageReader.MessageCode.MESSAGE_GAME_INVENTORY_CONTENT_ITEM),
                                item.getName(), type_obj, item.getMod());
                    }
                    System.out.println("\t" + String.format(str_output, board.getPlayer().getName(), str_item));
                    return true;
                }
                System.out.println("\t"
                        + String.format(
                                m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_INVENTORY_EMPTY),
                                board.getPlayer().getName()
                        )
                );
                break;
            case "terminar":
                break;
            case "tomar":
            case "pelear":
            case "equipar":
            case "usar":
            case "descartar":
                System.out.println("\t"
                        + m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR_ARGUMENT
                        )
                );
                break;

            default:
                System.out.println("\t"
                        + String.format(
                                m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR),
                                command,
                                m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR_COMMAND)
                        )
                );
        }
        return false;
    }

    /**
     * Método que ejecuta una instrucción válida del jugador
     *
     * @param command Instrucción que se ejecutará. Comandos válidos: tomar,
     * pelear, equipar, usar, descartar
     * @param value Valor del elemento del juego que vá a hacer efecto la
     * instrucción.
     * @return Boolean que indica si la instrcción es válida.
     */
    public boolean runCommand(String command, String value) {
        switch (command) {
            case "tomar":
                if (!this.board.getStagePlayer().getItems().isEmpty()) {
                    for (Item item : this.board.getStagePlayer().getItems()) {
                        if (item.getName().equalsIgnoreCase(value)) {
                            if (!this.board.getPlayer().take_item(item)) {
                                System.out.println("\t"
                                        + String.format(
                                                this.m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_INVENTORY_FULL),
                                                value
                                        )
                                );
                            } else {
                                this.board.getStagePlayer().removeItem(item.getName());
                                System.out.println("\t"
                                        + String.format(
                                                m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ITEM_PICK),
                                                board.getPlayer().getName(),
                                                item.getName()
                                        )
                                );
                                this.board.getStagePlayer().threat(true);
                                return true;
                            }
                            return false;
                        }
                    }
                    System.out.println("\t"
                            + String.format(
                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR_CELL_ITEM),
                                    value
                            )
                    );
                    break;
                }
                System.out.println("\t"
                        + m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_CELL_ITEM_EMPTY)
                );
                break;

            case "pelear":
                if (!this.board.getStagePlayer().getEnemies().isEmpty()) {
                    for (Enemy enemy : this.board.getStagePlayer().getEnemies()) {
                        if (enemy.getName().equalsIgnoreCase(value)) {
                            System.out.println("\t"
                                    + String.format(
                                            m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ATTACK_PLAYER),
                                            board.getPlayer().getName(),
                                            board.getPlayer().getWeapon().getName(),
                                            enemy.getName()
                                    )
                            );
                            System.out.println("\t"
                                    + String.format(
                                            m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ATTACK_DAMAGE),
                                            enemy.getName(),
                                            board.getPlayer().getAttack()
                                    ));
                            boolean target_alive = this.board.getPlayer().fight(enemy);
                            if (!target_alive) {
                                this.board.getStagePlayer().removeEnemy(enemy);
                                System.out.println("\t"
                                        + String.format(
                                                m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ATTACK_FAINT),
                                                enemy.getName()
                                        )
                                );
                                System.out.println("\t"
                                        + String.format(
                                                m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_XP_EARN),
                                                board.getPlayer().getName(),
                                                enemy.getExp_points()
                                        )
                                );
                                if (this.board.getPlayer().aument_exp(enemy.getExp_points())) {
                                    /* level up */
                                    System.out.println("\t"
                                            + String.format(
                                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_LEVEL_UP),
                                                    board.getPlayer().getName(),
                                                    board.getPlayer().getLevel(),
                                                    board.getPlayer().getMax_hp(),
                                                    board.getPlayer().getInventory_cap()
                                            )
                                    );
                                }

                            } else {
                                enemy.setAggro(true);
                            }
                            return true;
                        }
                    }
                    System.out.println(
                            "\t"
                            + String.format(m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR_ATTACK),
                                    value
                            )
                    );
                    break;
                }
                System.out.println("\t"
                        + m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_CELL_ENEMY_EMPTY)
                );
                break;
            case "equipar":
                String equip_output;
                if (!this.board.getPlayer().getInventory().isEmpty()) {
                    for (Item item : this.board.getPlayer().getInventory()) {
                        if (item.getName().equalsIgnoreCase(value)) {
                            if (this.board.getPlayer().equip(item)) {
                                this.board.getPlayer().getInventory().remove(item);
                                if (item.getItem_type() == 'W') {
                                    equip_output = m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_EQUIP_WEAPON);
                                } else {
                                    equip_output = m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_EQUIP_ARMOR);
                                }
                                System.out.println("\t"
                                        + String.format(equip_output, board.getPlayer().getName(), item.getName())
                                );
                                return true;
                            } else {
                                System.out.println("\t"
                                        + String.format(
                                                m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR_TYPE_EQUIP),
                                                value
                                        )
                                );
                            }
                            return false;
                        }
                    }
                    System.out.println("\t"
                            + String.format(
                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR_INVENTORY),
                                    value
                            )
                    );
                    break;
                }
                System.out.println("\t"
                        + String.format(
                                m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_INVENTORY_EMPTY),
                                board.getPlayer().getName()
                        )
                );
                break;
            case "usar":
                if (!this.board.getPlayer().getInventory().isEmpty()) {
                    for (Item item : this.board.getPlayer().getInventory()) {
                        if (item.getName().equalsIgnoreCase(value)) {
                            switch (item.getItem_type()) {
                                case 'O':
                                    if (!this.board.getStagePlayer().getEnemies().isEmpty()) {
                                        this.board.getPlayer().use_item(item, this.board.getStagePlayer().getEnemies());
                                        Iterator<Enemy> i = this.board.getStagePlayer().getEnemies().iterator();
                                        while (i.hasNext()) {
                                            Enemy enemy = i.next();
                                            System.out.println("\t"
                                                    + String.format(
                                                            m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ATTACK_PLAYER),
                                                            board.getPlayer().getName(),
                                                            item.getName(),
                                                            enemy.name
                                                    )
                                            );
                                            System.out.println("\t"
                                                    + String.format(
                                                            m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ATTACK_DAMAGE),
                                                            enemy.getName(),
                                                            item.getMod()
                                                    ));
                                            if (!enemy.alive) {
                                                System.out.println("\t"
                                                        + String.format(
                                                                m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ATTACK_FAINT),
                                                                enemy.getName()
                                                        )
                                                );
                                                System.out.println("\t"
                                                        + String.format(
                                                                m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_XP_EARN),
                                                                board.getPlayer().getName(),
                                                                enemy.getExp_points()
                                                        )
                                                );
                                                if (this.board.getPlayer().aument_exp(enemy.getExp_points())) {
                                                    /* level up */
                                                    System.out.println("\t"
                                                            + String.format(
                                                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_LEVEL_UP),
                                                                    board.getPlayer().getName(),
                                                                    board.getPlayer().getLevel(),
                                                                    board.getPlayer().getMax_hp(),
                                                                    board.getPlayer().getInventory_cap()
                                                            )
                                                    );
                                                }
                                                i.remove();
                                            } else {
                                                enemy.setAggro(true);
                                            }
                                        }
                                        return true;
                                    }
                                    System.out.println("\t"
                                            + String.format(
                                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ATTACK_NONE),
                                                    board.getPlayer().getName(),
                                                    item.getName()
                                            )
                                    );
                                    break;
                                case 'H':
                                    if (this.board.getPlayer().use_item(item)) {
                                        System.out.println("\t"
                                                + String.format(
                                                        m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ITEM_HEAL),
                                                        board.getPlayer().getName(),
                                                        item.getName(),
                                                        item.getMod(),
                                                        board.getPlayer().getHp()
                                                )
                                        );
                                    } else {
                                        System.out.println("\t"
                                                + String.format(
                                                        m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ITEM_HEAL_FULL),
                                                        board.getPlayer().getName()
                                                )
                                        );
                                        return false;
                                    }
                                    return true;
                                case 'D':
                                    if (this.board.getPlayer().use_item(item)) {
                                        System.out.println("\t"
                                                + String.format(
                                                        m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ITEM_DEFENSE),
                                                        board.getPlayer().getName(),
                                                        item.getName(),
                                                        item.getMod()
                                                )
                                        );
                                    } else {
                                        System.out.println("\t"
                                                + String.format(
                                                        m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ITEM_DEFENSE_FULL),
                                                        board.getPlayer().getName()
                                                )
                                        );
                                        return false;
                                    }
                                    return true;
                                default:
                                    System.out.println("\t"
                                            + String.format(
                                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR_TYPE_USE),
                                                    value
                                            )
                                    );
                            }
                            return true;
                        }
                    }
                    System.out.println("\t"
                            + String.format(
                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR_INVENTORY),
                                    value
                            )
                    );
                }
                System.out.println("\t"
                        + String.format(
                                m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_INVENTORY_EMPTY),
                                board.getPlayer().getName()
                        )
                );
                break;

            case "descartar":
                if (!this.board.getPlayer().getInventory().isEmpty()) {
                    Item i = this.board.getPlayer().discart(value);
                    if (i != null) {
                        System.out.println("\t"
                                + String.format(
                                        m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ITEM_DROP),
                                        board.getPlayer().getName(),
                                        i.getName()
                                )
                        );
                        return true;
                    }
                    System.out.println("\t"
                            + String.format(
                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR_INVENTORY),
                                    value
                            )
                    );
                    break;
                }
                System.out.println("\t"
                        + String.format(
                                m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_INVENTORY_EMPTY),
                                board.getPlayer().getName()
                        )
                );
                break;
            default:
                System.out.println("\t"
                        + String.format(
                                m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR),
                                command,
                                m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ERROR_COMMAND)
                        )
                );
        }
        return false;
    }

    /**
     * Método que representa el turno del enemigo. Hace que cada enemigo de la
     * casilla del jugador pelee si tiene que hacerlo.
     */
    public void enemyTurn() {
        int damage = 0;
        for (Enemy enemy : this.board.getStagePlayer().getEnemies()) {
            if (enemy.isAggro()) {
                boolean target_alive = enemy.fight(this.board.getPlayer());
                if (board.getPlayer().getDefense() < enemy.getAttack()) {
                    damage = enemy.getAttack() - board.getPlayer().getDefense();
                }
                System.out.println("\t"
                        + String.format(
                                m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ATTACK_ENEMY),
                                enemy.getName(),
                                board.getPlayer().getName(),
                                damage
                        )
                );
                if (!target_alive) {
                    System.out.println("\t"
                            + String.format(
                                    m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_ATTACK_FAINT),
                                    board.getPlayer().getName()
                            )
                    );
                    return;
                }
            }
        }
    }

    /**
     * Método para formatear salida del string dirección
     *
     * @param dir Comando de dirección
     * @return String formatedo segun libreria MessageCode
     */
    private String formatDir(String dir) {
        switch (dir) {
            case "avanzar":
                return m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_MOVE_UP);
            case "retroceder":
                return m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_MOVE_DOWN);
            case "derecha":
                return m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_MOVE_RIGHT);
            case "izquierda":
                return m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_MOVE_LEFT);
        }
        return null;
    }

    /**
     * Método para formatear salida del string dirección
     *
     * @param dir Enviroment que representa el entorno del jugador
     * @return String formatedo segun libreria MessageCode
     */
    private String formatDir(Environment dir) {
        String output = new String();
        if (dir.isForward()) {
            output += m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_MOVE_UP) + ", ";
        }
        if (dir.isBack()) {
            output += m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_MOVE_DOWN) + ", ";
        }
        if (dir.isRight()) {
            output += m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_MOVE_RIGHT) + ", ";
        }
        if (dir.isLeft()) {
            output += m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_MOVE_LEFT);
        }
        return output;
    }

    /**
     * Método para formatear salida del string tipo de objeto
     *
     * @param type char con el tipo de objeto
     * @return String formatedo segun libreria MessageCode
     */
    private String formatItemType(char type) {
        switch (type) {
            case 'W':
                return m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_INVENTORY_CONTENT_TYPE_WEAPON);
            case 'A':
                return m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_INVENTORY_CONTENT_TYPE_ARMOR);
            case 'H':
                return m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_INVENTORY_CONTENT_TYPE_HEALING);
            case 'D':
                return m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_INVENTORY_CONTENT_TYPE_DEFENSE);
            case 'O':
                return m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_INVENTORY_CONTENT_TYPE_OFFENSE);
        }
        return null;
    }

    /**
     * Método que instancia el juego y envía las instrucciones al juego
     * siguiendo las reglas y respetando los turnos correspondientes
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean command_exists = true;
        String command = "";
        Tarea2 game = new Tarea2();
        String reason_exit = game.m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_EXIT_USER);
        int sp;

        while (!command.equalsIgnoreCase("terminar")) {
            if (command_exists) {
                /* Turno player */
                if (!game.board.getPlayer().alive) {
                    reason_exit = game.m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_EXIT_DEFEAT);
                    break;
                }
                System.out.println("-- Tu turno --");
                /* Condiociones de termino */
                if (game.board.getBoard_explored() == 0) {
                    reason_exit = game.m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_EXIT_VICTORY_CELLS);
                    break;
                }
                if (game.board.enemiesDead()) {
                    reason_exit = game.m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_EXIT_VICTORY_ENEMIES);
                    break;
                }
                /* variables que cambian por turnos */
                if (game.board.getPlayer().isWeary()) {
                    game.board.getPlayer().setWeary(1);
                    game.board.getPlayer().setResting(true);
                    game.board.getPlayer().setTime_running(1);
                }
                if (!game.board.getPlayer().isRunning() && game.board.getPlayer().getTime_running() > 1) {
                    game.board.getPlayer().setWeary(0);
                    game.board.getPlayer().setResting(true);
                    game.board.getPlayer().setTime_running(1);
                }
                // objeto protector
                if (game.board.getPlayer().clearDefensiveItems()) {
                    System.out.println("\tSe acabó la duración del objeto protector " + game.board.getPlayer().getDefensive_item());
                }
            }

            System.out.print("   ");
            command = input.nextLine();
            /* runcomand cambia el estado del stage evaluando si el enemigo tiene que atacar */
            if (command.contains(" ")) {
                sp = command.indexOf(" ");
                command_exists = game.runCommand(command.substring(0, sp), command.substring(sp + 1));
            } else {
                command_exists = game.runCommand(command);
            }
            if (command_exists) {
                /* turno enemigo si corresponde */
                if (game.board.getStagePlayer().isThreat()) {
                    System.out.println("-- Turno del enemigo --");
                    game.enemyTurn();
                }

                /* descansar*/
                game.board.getPlayer().rest();
            }

        }
        System.out.println(String.format(game.m_reader.getMessage(MessageReader.MessageCode.MESSAGE_GAME_EXIT), reason_exit));
    }
}

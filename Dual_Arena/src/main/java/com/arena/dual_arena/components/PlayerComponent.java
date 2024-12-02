package com.arena.dual_arena.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.arena.dual_arena.Exceptions.InsufficientControllersException;
import de.ralleytn.plugins.jinput.xinput.XInputEnvironmentPlugin;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.getInput;

/**
 * Component that manages all player-related interactions including movement controls, weapon handling,
 * and collision responses such as knockback.
 */
public class PlayerComponent extends Component {
    /**
     * Handle Player logic like its inputs, its visual, its weapon, and its velocity state.
     */
    private static int currentId = 1;
    protected int id;
    protected KeyCode inputUp;
    protected KeyCode inputDown;
    protected KeyCode inputLeft;
    protected KeyCode inputRight;
    protected KeyCode primaryFire;
    protected KeyCode secondaryFire;
    protected KeyCode dropWeapon;
    protected double speed = 2;
    private Controller controller;

    private Entity equippedWeapon = null;
    private boolean isInKnockback = false;
    private Point2D knockbackVelocity = Point2D.ZERO;
    private static final double KNOCKBACK_RESISTANCE = 1f;
    private static final double VELOCITY_DECAY = 0.95f;
    private static final float DEAD_ZONE = 0.3f;
    private float xValue = 0.0f;
    private float yValue = 0.0f;
    private float rbValue = 0.0f;
    private float lbValue = 0.0f;
    private float rightStickX = 0.0f;
    private float rightStickY = 0.0f;

    /**
     * Constructs a PlayerComponent with specific controls for movement and actions.
     *
     * @param inputUp KeyCode for moving up.
     * @param inputDown KeyCode for moving down.
     * @param inputLeft KeyCode for moving left.
     * @param inputRight KeyCode for moving right.
     * @param primaryFire KeyCode for primary fire action.
     * @param secondaryFire KeyCode for secondary fire action.
     * @param dropWeapon KeyCode for dropping the weapon.
     */
    public PlayerComponent(KeyCode inputUp, KeyCode inputDown, KeyCode inputLeft, KeyCode inputRight, KeyCode primaryFire, KeyCode secondaryFire, KeyCode dropWeapon) {
        this.inputUp = inputUp;
        this.inputDown = inputDown;
        this.inputLeft = inputLeft;
        this.inputRight = inputRight;
        this.primaryFire = primaryFire;
        this.secondaryFire = secondaryFire;
        this.dropWeapon = dropWeapon;
        this.id = currentId;
        currentId++;
        try{
            initController();
        } catch (InsufficientControllersException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Initializes player controls when the component is added to an entity.
     * This method sets up input bindings for player actions such as movement and weapon usage.
     */
    @Override
    public void onAdded() {
        initInput();
    }

    /**
     * Updates the player component each frame, processing movement, input, and knockback effects.
     * This method is called every frame to ensure the player's state is updated based on current inputs
     * and interactions such as collisions causing knockback.
     *
     * @param tpf Time per frame, which is used to scale movement and timing calculations.
     */
    @Override
    public void onUpdate(double tpf) {
        if (isInKnockback) {
            entity.translate(knockbackVelocity.multiply(tpf));
            knockbackVelocity = knockbackVelocity.multiply(VELOCITY_DECAY);

            if (knockbackVelocity.magnitude() < 0.1) {
                isInKnockback = false;
                knockbackVelocity = Point2D.ZERO;
            }
        }
        pollController();
    }

    /**
     * Initializes the game controller environment and assigns a controller to this player based on their unique ID.
     * This method sets up the system properties to locate the controller library and identifies available gamepads.
     * Each player component is then linked to a specific controller if available.
     */
    public void initController() throws InsufficientControllersException{
        System.setProperty("net.java.games.input.librarypath",
                new java.io.File("Dual_Arena\\target\\natives").getAbsolutePath());

        ControllerEnvironment env = new XInputEnvironmentPlugin();
        if (!env.isSupported()) {
            env = ControllerEnvironment.getDefaultEnvironment();
        }

        Controller[] controllers = env.getControllers();
        List<Controller> gamepads = new ArrayList<>();

        // Collecte toutes les manettes
        for (Controller controller : controllers) {
            if (controller.getType() == Controller.Type.GAMEPAD) {
                gamepads.add(controller);
            }
        }

        if (!gamepads.isEmpty()) {
            // Vérifie si le nombre de manettes est suffisant pour le nombre de joueurs
            if (gamepads.size() < id) {
                throw new InsufficientControllersException("Pas assez de manettes connectées : " + gamepads.size() + " détectée(s), mais requise(s) pour le joueur " + id);
            }else{
                // Assigne la manette en fonction de l'ID du joueur
                int controllerIndex = id - 1; // ID 1 = première manette, ID 2 = deuxième manette
                controller = gamepads.get(controllerIndex);
                System.out.println("Joueur " + id + " utilise la manette : " + controller.getName());
            }
        }else{
            System.out.println("Pas de manette détectée.");
        }
    }

    /**
     * Polls the assigned game controller for input events and processes these events to control the player.
     * This method updates the player's movement based on joystick positions and triggers actions based on button presses.
     */
    public void pollController() {
        if (controller != null && controller.poll()) {
            var queue = controller.getEventQueue();
            var event = new Event();

            while (queue.getNextEvent(event)) {
                float value = event.getValue();
                var component = event.getComponent();
                String name = component.getName();

                if (component.isAnalog()) {
                    switch (name) {
                        case "lx":
                            xValue = value;
                            break;
                        case "ly":
                            yValue = -value;
                            break;
                        case "rx":
                            rightStickX = value;
                            break;
                        case "ry":
                            rightStickY = -value;
                            break;
                        case "rb":
                            rbValue = value;
                            break;
                        case "lb":
                            lbValue = value;
                            break;
                    }
                }
            }
            move();
        }
    }

    public void move() {
        // Applique les mouvements en fonction des valeurs des axes
        if (Math.abs(xValue) > DEAD_ZONE || Math.abs(yValue) > DEAD_ZONE) {
            double dx = xValue * speed;
            double dy = yValue * speed;

            entity.translate(dx, dy);

            if (xValue > 0) {
                entity.getComponent(PlayerAnimationComponent.class).moveRight();
            }
            else {
                entity.getComponent(PlayerAnimationComponent.class).moveLeft();

            }
            if (yValue > 0) {
                entity.getComponent(PlayerAnimationComponent.class).moveUp();
            }else{
                entity.getComponent(PlayerAnimationComponent.class).moveDown();
            }

        }
        if (rbValue == 1.0){
            fireWithRb();
        }
        if (lbValue == 1.0){
            fireWithLb();
        }
    }

    /**
     * Retrieves the horizontal movement input value.
     * This value is often obtained from a game controller's left joystick x-axis or a keyboard input simulation.
     *
     * @return The current horizontal input value, ranging from -1.0 (left) to 1.0 (right).
     */
    public double getXvalue(){
        return xValue;
    }

    /**
     * Retrieves the vertical movement input value.
     * This value is typically obtained from a game controller's left joystick y-axis or a keyboard input simulation.
     *
     * @return The current vertical input value, ranging from -1.0 (up) to 1.0 (down).
     */
    public double getYvalue(){
        return yValue;
    }

    /**
     * Retrieves the horizontal value of the right joystick.
     * This input is primarily used for aiming or camera control in many games.
     *
     * @return The horizontal position of the right joystick, ranging from -1.0 (left) to 1.0 (right).
     */
    public float getRightStickX(){
        return rightStickX;
    }

    /**
     * Retrieves the vertical value of the right joystick.
     * This input is commonly used for aiming or camera control, particularly in games requiring vertical aim adjustments.
     *
     * @return The vertical position of the right joystick, ranging from -1.0 (up) to 1.0 (down).
     */
    public float getRightStickY(){
        return rightStickY;
    }

    /**
     * Initializes and binds input actions for the player based on predefined key codes.
     * This setup includes bindings for movement, primary and secondary weapon firing, and dropping weapons.
     * Each input action is identified by a unique identifier to support multiple players.
     */
    protected void initInput() {
        Input input = getInput();

        input.addAction(new UserAction("Move Up" + id) {
            @Override
            protected void onAction() {
                entity.getComponent(PlayerAnimationComponent.class).moveUp();
            }
        }, inputUp);

        input.addAction(new UserAction("Move Down" + id) {
            @Override
            protected void onAction() {
                entity.getComponent(PlayerAnimationComponent.class).moveDown();
            }
        }, inputDown);

        input.addAction(new UserAction("Move Left" + id) {
            @Override
            protected void onAction() {
                entity.getComponent(PlayerAnimationComponent.class).moveLeft();
            }
        }, inputLeft);

        input.addAction(new UserAction("Move Right" + id) {
            @Override
            protected void onAction() {
                entity.getComponent(PlayerAnimationComponent.class).moveRight();
            }
        }, inputRight);

        FXGL.getInput().addAction(new UserAction("PrimaryFire" + id) {
            @Override
            protected void onAction() {
                if (equippedWeapon != null) {
                    Point2D mousePosition = FXGL.getInput().getMousePositionWorld();
                    Point2D direction = mousePosition.subtract(entity.getCenter()).normalize();

                    WeaponComponent weaponComponent = equippedWeapon.getComponent(WeaponComponent.class);
                    if (weaponComponent != null) {
                        weaponComponent.primaryFire(direction);
                    }
                }
            }
        }, primaryFire);

        FXGL.getInput().addAction(new UserAction("SecondaryFire" + id) {
            @Override
            protected void onAction() {
                if (equippedWeapon != null) {
                    Point2D mousePosition = FXGL.getInput().getMousePositionWorld();
                    Point2D direction = mousePosition.subtract(entity.getCenter()).normalize();

                    WeaponComponent weaponComponent = equippedWeapon.getComponent(WeaponComponent.class);
                    if (weaponComponent != null) {
                        weaponComponent.secondaryFire(direction);
                    }
                }
            }
        }, secondaryFire);

        // Optional: Add ability to drop weapon
        FXGL.getInput().addAction(new UserAction("Drop Weapon" + id) {
            @Override
            protected void onAction() {
                dropWeapon();
            }
        }, dropWeapon);
    }

    /**
     * Fires the primary weapon associated with the right bumper button on the controller.
     * This method calculates the firing direction based on the current rotation of the equipped weapon
     * and triggers the primary firing mechanism.
     */
    public void fireWithRb() {
        if (equippedWeapon != null){
            double radians = Math.toRadians(equippedWeapon.getRotation());
            double x = Math.cos(radians);
            double y = Math.sin(radians);

            Point2D direction = new Point2D(x,y);
            Point2D normalizedDirection = direction.normalize();

            WeaponComponent weaponComponent = equippedWeapon.getComponent(WeaponComponent.class);
            if (weaponComponent != null) {
                weaponComponent.primaryFire(normalizedDirection);
            }
        }
    }

    /**
     * Fires the secondary weapon associated with the left bumper button on the controller.
     * This method calculates the firing direction similar to `fireWithRb` but triggers the secondary firing mechanism.
     */
    public void fireWithLb() {
        if (equippedWeapon != null){
            double radians = Math.toRadians(equippedWeapon.getRotation());
            double x = Math.cos(radians);
            double y = Math.sin(radians);

            Point2D direction = new Point2D(x,y);
            Point2D normalizedDirection = direction.normalize();

            WeaponComponent weaponComponent = equippedWeapon.getComponent(WeaponComponent.class);
            if (weaponComponent != null) {
                weaponComponent.secondaryFire(normalizedDirection);
            }
        }
    }

    /**
     * Applies a knockback force to the player in a specified direction.
     * This method adjusts the player's velocity based on the direction and magnitude of the knockback force.
     *
     * @param direction The unit vector direction in which the knockback is applied.
     * @param knockbackForce The magnitude of the knockback.
     */
    public void applyKnockback(Point2D direction, float knockbackForce) {
        isInKnockback = true;

        // If already in knockback, add to existing velocity instead of replacing it
        if (knockbackVelocity.magnitude() > 0) {
            knockbackVelocity = knockbackVelocity.add(direction.multiply(knockbackForce));
        } else {
            knockbackVelocity = direction.multiply(knockbackForce);
        }
    }

    public void resetKnockback() {
        isInKnockback = false;
    }

    /**
     * Retrieves a movement modifier based on the player's knockback state.
     * This method adjusts the player's speed to simulate resistance during knockback.
     *
     * @return A Point2D representing the movement modifier.
     */
    public Point2D getMovementModifier() {
        return isInKnockback ? new Point2D (1, 1).multiply(KNOCKBACK_RESISTANCE) : new Point2D(1, 1);
    }

    /**
     * Equips a weapon entity to the player if the weapon is not already picked up.
     * This method also handles the transition of the weapon's state from the world to being equipped by the player.
     *
     * @param weapon The weapon entity to be picked up.
     */
    public void pickupWeapon(Entity weapon) {
        if (equippedWeapon != null) {
            equippedWeapon.removeFromWorld();
        }

        WeaponComponent weaponComponent = weapon.getComponent(WeaponComponent.class);

        if (weaponComponent != null && !weaponComponent.isPickedUp()) {
            equippedWeapon = weapon;
            weaponComponent.pickup(entity);
        }
    }

    /**
     * Drops the currently equipped weapon, if any, and resets its state.
     */
    public void dropWeapon() {
        if (equippedWeapon != null) {
            WeaponComponent weaponComponent = equippedWeapon.getComponent(WeaponComponent.class);
            if (weaponComponent != null) {
                weaponComponent.drop();
            }
            equippedWeapon = null;
        }
    }

    /**
     * Retrieves the currently equipped weapon entity.
     *
     * @return The equipped weapon entity, or null if no weapon is equipped.
     */
    public Entity getEquippedWeapon() {
        return equippedWeapon;
    }

    /**
     * Retrieves the unique identifier for this player.
     *
     * @return The unique ID of the player.
     */
    public int getId(){
        return this.id;
    }

    public void setYvalue(double i) {
        this.yValue = (float) i;
    }

    public void setXvalue(double i) {
        this.xValue = (float) i;
    }
}

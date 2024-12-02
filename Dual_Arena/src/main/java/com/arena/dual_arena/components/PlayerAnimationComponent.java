package com.arena.dual_arena.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.arena.dual_arena.components.WeaponComponent.STICK_DEAD_ZONE;

/**
 * Component that manages animations for a player entity based on its state and movement.
 * This includes walking animations and idle states.
 */
public class PlayerAnimationComponent extends Component {
    private double speedX = 0;
    private double speedY = 0;
    protected double speed = 2;


    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalk;

    /**
     * Initializes animation channels and sets the initial animated texture.
     */
    public PlayerAnimationComponent() {
        animIdle = new AnimationChannel(FXGL.image("player_idle_2.png"), 8, 15*2, 28*2, Duration.seconds(1), 0, 7);
        animWalk = new AnimationChannel(FXGL.image("player_walk_2.png"), 8, 16*2, 27*2, Duration.seconds(1), 0, 7);
        texture = new AnimatedTexture(animIdle);
    }

    /**
     * Called when the component is added to the entity. Initializes the texture and sets it to loop the idle animation.
     */
    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 27));
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(animIdle);
    }

    /**
     * Called every frame, updates the player's animation state based on movement and direction.
     *
     * @param tpf Time per frame; used to adjust movement calculations.
     */
    @Override
    public void onUpdate(double tpf) {
        entity.translateX(speedX);
        entity.translateY(speedY);


        PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
        double axeX = playerComponent.getRightStickX();
        if (playerComponent.getEquippedWeapon() != null) {
            Point2D mousePosition = FXGL.getInput().getMousePositionWorld();
            if (Math.abs(axeX) > STICK_DEAD_ZONE){
                if (axeX < 0){
                    entity.setScaleX(1);
                }else{
                    entity.setScaleX(-1);
                }
            }
        }

        if (speedX != 0 || speedY != 0) {

            if (texture.getAnimationChannel() == animIdle) {
                texture.loopAnimationChannel(animWalk);
            }

            speedX = (float) (speedX * 0.9);
            speedY = (float) (speedY * 0.9);

            if (FXGLMath.abs(speedX) < 1 && FXGLMath.abs(speedY) < 1) {
                speedX = 0;
                speedY = 0;
                texture.loopAnimationChannel(animIdle);
            }
        }
    }

    /**
     * Moves the player entity left by adjusting the horizontal speed and setting the horizontal scale.
     * If no weapon is equipped, the player's sprite is scaled to face left.
     */
    public void moveLeft() {
        PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
        speedX = playerComponent.getXvalue() * speed;

        if (playerComponent.getEquippedWeapon() == null) {
            getEntity().setScaleX(1);
        }
    }

    /**
     * Moves the player entity right by adjusting the horizontal speed and setting the horizontal scale.
     * If no weapon is equipped, the player's sprite is scaled to face right.
     */
    public void moveRight() {
        PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
        speedX = playerComponent.getXvalue() * speed;
        if (playerComponent.getEquippedWeapon() == null) {
            getEntity().setScaleX(-1);
        }
    }

    /**
     * Moves the player entity upwards by adjusting the vertical speed.
     * This method modifies the player's vertical velocity based on input values.
     */
    public void moveUp() {
        PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
        speedY = playerComponent.getYvalue() * speed;
    }

    /**
     * Moves the player entity downwards by adjusting the vertical speed.
     * This method modifies the player's vertical velocity based on input values.
     */
    public void moveDown() {
        PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
        speedY = playerComponent.getYvalue() * speed;
    }
}

package com.arena.dual_arena.models.hazard;

import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.Random;
import com.almasb.fxgl.dsl.FXGL;

/**
 * Abstract class defining a framework for handling random attack events in the game.
 * It uses a {@link Timeline} to schedule attacks after random intervals.
 */
public abstract class RandomAttackEvent {
    protected final Random random;
    protected double dropTime;
    protected double dropTimeMinimum;

    /**
     * Constructs a {@code RandomAttackEvent} with specified drop time and minimum drop time.
     *
     * @param dt  The maximum time in seconds that the actual drop time can vary.
     * @param dtm The minimum time in seconds before the first attack is triggered.
     */
    protected RandomAttackEvent(double dt, double dtm){
        this.random = new Random();
        this.dropTime=dt;
        this.dropTimeMinimum=dtm;
    }

    /**
     * Starts the attack sequence by initializing and playing a single execution {@link Timeline}.
     * The timeline triggers an attack after a random delay that is computed based on the {@code dropTime} and
     * {@code dropTimeMinimum} values.
     */
    protected void startRandomAttack() {
        Duration randomDuration = Duration.seconds(random.nextDouble()*dropTime+dropTimeMinimum);
        FXGL.runOnce(()->{
            triggerAttackEvent();
            startRandomAttack();
        }, randomDuration);
    }

    /**
     * Abstract method to define specific attack events.
     * Implementations of this method should specify what happens when an attack is triggered.
     */
    protected abstract void triggerAttackEvent();
}

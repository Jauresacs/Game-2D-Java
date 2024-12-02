package com.arena.dual_arena.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.arena.dual_arena.components.FireballComponent;
import com.arena.dual_arena.types.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

public class HazardFactory implements EntityFactory {

    /**
     * Spawns an attack zone entity that gradually appears and then triggers an explosion.
     * This method uses animation to fade in the attack zone over a specified duration and removes it after the animation.
     *
     * @param data The spawn data containing parameters like the radius of the attack zone.
     * @return A newly created attack zone entity with specified properties.
     */
    @Spawns("attackZone")
    public Entity createAttackZone(SpawnData data) {
        final int duration = 3;
        int radius = data.get("radius");
        Color customColor = Color.rgb(255, 160, 0, 0.4);
        Circle circle = new Circle(radius, customColor);
        circle.setOpacity(0);

        Entity attackZone = FXGL.entityBuilder(data)
                .type(EntityType.HAZARD)
                .view(circle)
                .build();
        FXGL.animationBuilder()
                .duration(Duration.seconds(duration))
                .fadeIn(circle)
                .buildAndPlay();
        FXGL.getGameTimer().runOnceAfter(()-> {
            attackZone.removeFromWorld();
            createCircleExplosion(attackZone.getX(), attackZone.getY(), radius);
        },Duration.seconds(duration));
        return attackZone;
    }
    @Spawns("fireball")
    public Entity createFireball(SpawnData data) {
        double x = data.getX();
        double y = data.getY();
        int oppositeX = data.get("oppositeX");
        int oppositeY = data.get("oppositeY");
        double scaleFactor = 2;

        Image sprite = FXGL.image("fireball.png");
        AnimationChannel animatedSprite = new AnimationChannel(sprite,5,64,32, Duration.seconds(1),0,4);
        AnimatedTexture texture = new AnimatedTexture(animatedSprite);
        texture.setTranslateX(-37*scaleFactor);
        texture.setTranslateY(-8*scaleFactor);
        texture.getTransforms().add(new Scale(scaleFactor, scaleFactor));

        Entity fireball = FXGL.entityBuilder()
                .at(x,y)
                .type(EntityType.EXPLOSION)
                .view(texture)
                .with(new FireballComponent(oppositeX, oppositeY))
                .bbox(new HitBox(BoundingShape.box(10*scaleFactor,13*scaleFactor)))
                .with(new CollidableComponent(true))
                .build();
        double angle = Math.toDegrees(Math.atan2(
                oppositeY - y,
                oppositeX - x
        ));
        fireball.setRotation(angle);
        texture.loop();
        return fireball;
    }

    /**
     * Helper method to spawn a circular attack zone at a specified location.
     *
     * @param x The x-coordinate for the attack zone.
     * @param y The y-coordinate for the attack zone.
     * @param radius The radius of the attack zone.
     * @return The newly spawned attack zone entity.
     */
    public static Entity spawnCircleZone(int x, int y, int radius) {

        return FXGL.spawn("attackZone", new SpawnData(x, y).put("radius", radius));
    }
    public static Entity spawnFireball(int x, int y, int oppositeX, int oppositeY) {

        return FXGL.spawn("fireball", new SpawnData(x, y).put("oppositeX", oppositeX).put("oppositeY", oppositeY));
    }

    /**
     * Creates a visual explosion effect at a given location using an animated texture.
     *
     * @param x The x-coordinate for the center of the explosion.
     * @param y The y-coordinate for the center of the explosion.
     * @param rad The radius of the explosion effect.
     */
    public static void createCircleExplosion(double x, double y, double rad) {
        double offset=1.5;
        Image sprite = FXGL.image("explosion.png");
        FXGL.play("hazard_explosion.wav");

        AnimationChannel animatedSprite = new AnimationChannel(sprite,16,128,128, Duration.seconds(1),1,16);

        AnimatedTexture texture = new AnimatedTexture(animatedSprite);

        double scaleFactor = (double) rad * offset / 64;
        texture.getTransforms().add(new Scale(scaleFactor, scaleFactor));
        double squareSide = rad * Math.sqrt(2);
        texture.setTranslateX(-(rad * offset));
        texture.setTranslateY(-(rad * offset));
        double rectSide = rad * Math.sqrt(3);


        Entity animatedExplosion = FXGL.entityBuilder()
                .at(x, y)
                .type(EntityType.EXPLOSION)
                .view(texture)
                .bbox(new HitBox("horizontal",new Point2D(-rectSide/2,-rad/2), BoundingShape.box(rectSide,rad)))
                .bbox(new HitBox("square",new Point2D(-squareSide/2,-squareSide/2), BoundingShape.box(squareSide,squareSide)))
                .bbox(new HitBox("vertical",new Point2D(-rad/2,-rectSide/2), BoundingShape.box(rad,rectSide)))
                .with(new CollidableComponent(true))
                .build();
        texture.play();
        texture.setOnCycleFinished(() -> FXGL.getGameWorld().removeEntity(animatedExplosion));
        FXGL.getGameWorld().addEntity(animatedExplosion);
    }

    /*
    public static Polygon2D createCirclePolygon(double centerX, double centerY, double radius, int numSegments) {
        List<Double> points = new ArrayList<>();
        double angleStep = 2 * Math.PI / numSegments;

        for (int i = 0; i < numSegments; i++) {
            double theta = i * angleStep;
            double x = centerX + radius * Math.cos(theta);
            double y = centerY + radius * Math.sin(theta);
            points.add(x);
            points.add(y);
        }

        return new Polygon2D(points.stream().mapToDouble(Double::doubleValue).toArray());
    }

     */
}


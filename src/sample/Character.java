package sample;

import javafx.geometry.Point2D;
import javafx.scene.shape.*;

import java.awt.geom.*;

// use an abstract class for Ship and Asteroids to be subclasses
public abstract class Character {

    private Polygon character;
    private Point2D movement;
    private Boolean alive = true;
    private Boolean outOfBounds = false;

    public Character(Polygon polygon, int x, int y) {
        character = polygon;
        character.setTranslateX(x);
        character.setTranslateY(y);

        movement = new Point2D(0, 0);
    }

    public Boolean isOutOfBounds() {
        return outOfBounds;
    }

    public void setAlive(Boolean status) {
        alive = status;
    }

    public Boolean isAlive() {
        return alive;
    }

    public Polygon getCharacter() {
        return character;
    }

    public void turnLeft() {
        character.setRotate(character.getRotate() - 3);
    }

    public void turnRight() {
        character.setRotate(character.getRotate() + 3);
    }

    public void setMovement(Point2D movement) {
        this.movement = movement;
    }

    public Point2D getMovement() {
        return movement;
    }

    // add x or/and y coordinates to previous coordinates to move
    public void move() {
        character.setTranslateX(character.getTranslateX() + this.movement.getX());
        character.setTranslateY(character.getTranslateY() + this.movement.getY());

        // make sure characters don't go out of bounds
        if (character.getTranslateX() < 0) {
            // goes back to X = 600
            character.setTranslateX(character.getTranslateX() + Main.WIDTH);
        }

        if (character.getTranslateX() > Main.WIDTH) {
            // goes back to X = 0
            character.setTranslateX(character.getTranslateX() % Main.WIDTH);
        }

        if (character.getTranslateY() < 0) {
            // goes back to Y = 400
            character.setTranslateY(character.getTranslateY() + Main.HEIGHT);
        }

        if (character.getTranslateY() > Main.HEIGHT) {
            // goes back to Y = 0
            character.setTranslateY(character.getTranslateY() % Main.HEIGHT);
        }
    }

    // makes projectiles disappear when it goes out of bounds
    public void moveProjectile() {
        character.setTranslateX(character.getTranslateX() + this.movement.getX());
        character.setTranslateY(character.getTranslateY() + this.movement.getY());

        if (character.getTranslateX() < 0 || character.getTranslateX() > Main.WIDTH ||
                character.getTranslateY() < 0 || character.getTranslateY() > Main.HEIGHT) {
           outOfBounds = true;
        }
    }

    public void accelerate() {
        double changeX = Math.cos(Math.toRadians(character.getRotate()));
        double changeY = Math.sin(Math.toRadians(character.getRotate()));

        // reduce acceleration
        changeX *= 0.035;
        changeY *= 0.035;

        movement = movement.add(changeX, changeY);
    }

    // check for collision between ship and asteroids
    // if false is returned there is no collision
    public boolean collide(Character other) {
        Shape collisionArea = Shape.intersect(character, other.getCharacter());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

}

package sample;

import java.util.*;

public class Asteroid extends Character {
    private double rotationalMovement;

    public Asteroid(int x, int y) {
        super(new PolygonMaker().makePolygon(), x, y);

        Random rand = new Random();
        // asteroids are rotated to a random direction
        super.getCharacter().setRotate(rand.nextInt(360));

        // random number of acceleration calls
        int accelerationAmount = 1 + rand.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }

        rotationalMovement = 0.5 - rand.nextDouble();
    }

    // needs to be overridden because the move function
    // in the Character class is only suitable for the ship
    // and the ship does not rotate
    @Override
    public void move() {
        super.move();
        // rotate asteroids while they move
        super.getCharacter().setRotate(super.getCharacter().getRotate() + rotationalMovement);
    }
}

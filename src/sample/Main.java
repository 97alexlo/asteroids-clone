package sample;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

public class Main extends Application {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 300;
    private List<Integer> scores = new ArrayList();

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);
        Text text = new Text(10, 20, "Points: 0");
        pane.getChildren().add(text);
        AtomicInteger points = new AtomicInteger();

        Ship ship = new Ship(WIDTH / 2, HEIGHT / 2); // create triangular ship
        List<Asteroid> asteroids = new ArrayList<>(); // create asteroids of random sizes
        List<Projectile> projectiles = new ArrayList<>(); // 0 projectiles at the start of game

        // create 5 asteroids of random sizes
        for (int i = 0; i < 5; i++) {
            Random rand = new Random();
            Asteroid asteroid = new Asteroid(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
            asteroids.add(asteroid);
        }

        pane.getChildren().add(ship.getCharacter());
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));

        Scene scene = new Scene(pane);

        // use a hashtable to keep a record of pressed keys
        // true if key is pressed, false otherwise
        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

        scene.setOnKeyPressed(keyEvent -> {
            pressedKeys.put(keyEvent.getCode(), Boolean.TRUE);
        });
        scene.setOnKeyReleased(keyEvent -> {
            pressedKeys.put(keyEvent.getCode(), Boolean.FALSE);
        });

        // create animations to rotate the ship using arrow keys
        new AnimationTimer() {
            @Override
            public void handle(long l) {
                // if arrow keys are pressed, rotate in its direction
                if(pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }
                if(pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }

                // press up arrow key to accelerate and ship's direction
                if(pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }
                // create a projectile when space bar is pressed
                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 3) {
                    // get direction of the ship (to shoot the projectile)
                    Projectile projectile = new Projectile(
                            (int) ship.getCharacter().getTranslateX(),
                            (int) ship.getCharacter().getTranslateY()
                    );
                    // set direction of the projectile
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile);

                    projectile.accelerate();
                    // set the speed of the projectile so that there is no acceleration
                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));
                    pane.getChildren().add(projectile.getCharacter());
                }
                ship.move();
                asteroids.forEach(asteroid -> asteroid.move());
                projectiles.forEach(projectile -> projectile.moveProjectile());

                // stop game if a collision occurs
                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                        stop();
                        scores.add(points.get());
                        stage.hide();
                    }
                });

                // set alive status of collided projects/asteroids to false
                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if(projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            asteroid.setAlive(false);
                        }
                    });
                    // increment points when an asteroid is destroyed
                    if(!projectile.isAlive()) {
                        text.setText("Points: " + points.addAndGet(1));
                    }
                });

                // call function to remove collided characters
                removeCharacter(projectiles, pane);
                removeCharacter(asteroids, pane);

                // add asteroids throughout the game only if it does not
                // collide with the ship on spawn
                if(Math.random() < 0.01) {
                    Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
                    if(!asteroid.collide(ship)) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    }
                }

            }
        }.start();

        stage.setTitle("Space Shooter");
        stage.setScene(scene);
        stage.show();
    }

    // remove collided projectiles/asteroids
    public <T extends Character> void removeCharacter(List<T> source, Pane pane) {
        source.stream()
                .filter(character -> (!character.isAlive() || character.isOutOfBounds()))
                .forEach(character -> pane.getChildren().remove(character.getCharacter()));
        source.removeAll(source.stream()
                .filter(character -> (!character.isAlive() || character.isOutOfBounds()))
                .collect(Collectors.toList()));
    }

    public void reset() {

    }
    public static void main(String[] args) {
        launch(args);
    }
}

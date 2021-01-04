package sample;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class PolygonMaker {
    public Polygon makePolygon() {
        // get a random size for each asteroid
        Random rand = new Random();
        double size = 10 + rand.nextInt(10);

        // can reuse c and s by multiplying by -1
        Polygon polygon = new Polygon();
        double c = Math.cos(Math.PI * 1 / 4);
        double s = Math.sin(Math.PI * 1 / 4);

        // use coordinates to form a hexagon
        polygon.getPoints().addAll(
                size, 0.0,
                size * c, size * s,
                0.0, size,
                -1 * size * c, 1 * size * s,
                -1 * size, 0.0,
                -1 * size * c, -1 * size * s,
                0.0, -1 * size,
                size * c, -1 * size * s);

        for (int i = 0; i < polygon.getPoints().size(); i++) {
            int change = rand.nextInt(5) - 2;
            polygon.getPoints().set(i, polygon.getPoints().get(i) + change);
        }

        return polygon;
    }
}

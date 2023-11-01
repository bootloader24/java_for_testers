package ru.training.geometry;

import ru.training.geometry.figures.Rectangle;
import ru.training.geometry.figures.Square;
import ru.training.geometry.figures.Triangle;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Geometry {
    public static void main(String[] args) {
        Supplier<Square> randomSquare = () -> new Square(new Random().nextDouble(100));
        var squares = Stream.generate(randomSquare).limit(5);
        squares.peek(Square::printArea).forEach(Square::printPerimeter);

        Supplier<Rectangle> randomRectangle = () -> new Rectangle(
                new Random().nextDouble(50),
                new Random().nextDouble(50));
        var rectangles = Stream.generate(randomRectangle).limit(5);
        rectangles.peek(Rectangle::printArea).forEach(Rectangle::printPerimeter);

        var triangles = List.of(new Triangle(2.0, 3.0, 4.0), new Triangle(2.0, 2.0, 1.0));
        triangles.forEach(triangle -> {
            Triangle.printArea(triangle);
            Triangle.printPerimeter(triangle);
        });
    }

}
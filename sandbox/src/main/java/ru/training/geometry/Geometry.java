package ru.training.geometry;

import ru.training.geometry.figures.Rectangle;
import ru.training.geometry.figures.Square;
import ru.training.geometry.figures.Triangle;

import java.util.List;
import java.util.function.Consumer;

public class Geometry {
    public static void main(String[] args) {
        var squares = List.of(new Square(5.0), new Square(7.0), new Square(9.0));
        squares.forEach(Square::printSquareArea);

        var rectangles = List.of(new Rectangle(4.0, 5.0), new Rectangle(12.0, 55.0));
        rectangles.forEach(Rectangle::printRectangleArea);

        var triangles = List.of(new Triangle(2.0, 3.0, 4.0), new Triangle(2.0, 2.0, 1.0));
        triangles.forEach(Triangle::printTriangleArea);
    }

}
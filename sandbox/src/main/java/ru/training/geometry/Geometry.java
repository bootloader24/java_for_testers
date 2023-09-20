package ru.training.geometry;

import ru.training.geometry.figures.Rectangle;
import ru.training.geometry.figures.Square;
import ru.training.geometry.figures.Triangle;

public class Geometry {
    public static void main(String[] args) {
        Square.printSquareArea(new Square(5.0));
        Square.printSquareArea(new Square(7.0));
        Square.printSquareArea(new Square(9.0));

        Rectangle.printRectangleArea(new Rectangle(4.0, 5.0));
        Rectangle.printRectangleArea(new Rectangle(12,55));

        Triangle.printTriangleArea(new Triangle(2.0, 3.0, 4.0));
        Triangle.printTriangleArea(new Triangle(2.0, 2.0, 1.0));
    }

}
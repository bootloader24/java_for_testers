package ru.training.geometry;

import ru.training.geometry.figures.Rectangle;
import ru.training.geometry.figures.Square;

public class Geometry {
    public static void main(String[] args) {
        Square.printSquareArea(new Square(5.0));
        Square.printSquareArea(new Square(7.0));
        Square.printSquareArea(new Square(9.0));
        Rectangle.printRectangleArea(4.0, 5.0);
        Rectangle.printRectangleArea(12,55);
    }

}
package ru.training.geometry;

import ru.training.geometry.figures.Rectangle;
import ru.training.geometry.figures.Square;

public class Geometry {
    public static void main(String[] args) {
        Square.printSquareArea(5);
        Square.printSquareArea(7);
        Square.printSquareArea(9);
        Rectangle.printRectangleArea(4.0, 5.0);
        Rectangle.printRectangleArea(12,55);
    }

}
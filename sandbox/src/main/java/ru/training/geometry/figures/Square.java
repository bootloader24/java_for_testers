package ru.training.geometry.figures;

public class Square {
    public static void printSquareArea(double side) {
        String text = String.format("Площадь квадрата со стороной %f = %f", side,  area(side));
        System.out.println(text);
    }

    public static double area(double a) {
        return a * a;
    }

    public static Object perimeter(double a) {
        return a * 4;
    }
}